package dao;

import dao.filter.IngredientFilter;
import dao.mapper.IngredientMapper;
import dao.pagination.PagedResult;
import db.DataSource;
import entity.Ingredient;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IngredientCrudOperations {
    private final DataSource dataSource;
    private final IngredientMapper ingredientMapper;

    public IngredientCrudOperations(DataSource dataSource, IngredientMapper ingredientMapper) {
        this.dataSource = dataSource;
        this.ingredientMapper = ingredientMapper;
    }

    public IngredientCrudOperations() {
        this.dataSource = new DataSource();
        this.ingredientMapper = new IngredientMapper();
    }

    public PagedResult<Ingredient> findIngredients(IngredientFilter filter) {
        try (Connection connection = dataSource.getConnection()) {
            // Build the query
            StringBuilder queryBuilder = new StringBuilder();
            List<Object> parameters = new ArrayList<>();

            // Select statement with join to get the latest price
            queryBuilder.append("SELECT i.id_ingredient, i.name, i.unit, p.unit_price, p.date ");
            queryBuilder.append("FROM ingredient i ");
            queryBuilder.append("LEFT JOIN (");
            queryBuilder.append("  SELECT id_ingredient, unit_price, date ");
            queryBuilder.append("  FROM price ");
            queryBuilder.append("  WHERE (id_ingredient, date) IN (");
            queryBuilder.append("    SELECT id_ingredient, MAX(date) ");
            queryBuilder.append("    FROM price ");
            queryBuilder.append("    GROUP BY id_ingredient");
            queryBuilder.append("  )");
            queryBuilder.append(") p ON i.id_ingredient = p.id_ingredient ");

            // Where clause with filters
            StringBuilder whereClause = new StringBuilder();

            // Filter by name
            filter.getNameContains().ifPresent(name -> {
                appendWhereCondition(whereClause);
                whereClause.append("LOWER(i.name) LIKE LOWER(?)");
                parameters.add("%" + name + "%");
            });

            // Filter by unit
            filter.getUnit().ifPresent(unit -> {
                appendWhereCondition(whereClause);
                whereClause.append("i.unit = ?::unit");
                parameters.add(unit.toString());
            });

            // Filter by price range
            filter.getMinPrice().ifPresent(minPrice -> {
                appendWhereCondition(whereClause);
                whereClause.append("p.unit_price >= ?");
                parameters.add(minPrice);
            });

            filter.getMaxPrice().ifPresent(maxPrice -> {
                appendWhereCondition(whereClause);
                whereClause.append("p.unit_price <= ?");
                parameters.add(maxPrice);
            });

            // Filter by last modified date range
            filter.getModifiedAfter().ifPresent(modifiedAfter -> {
                appendWhereCondition(whereClause);
                whereClause.append("p.date >= ?");
                parameters.add(java.sql.Date.valueOf(modifiedAfter));
            });

            filter.getModifiedBefore().ifPresent(modifiedBefore -> {
                appendWhereCondition(whereClause);
                whereClause.append("p.date <= ?");
                parameters.add(java.sql.Date.valueOf(modifiedBefore));
            });

            // Add where clause to query
            if (whereClause.length() > 0) {
                queryBuilder.append(" WHERE ").append(whereClause);
            }

            // Add order by clause
            filter.getSortBy().ifPresent(sortBy -> {
                queryBuilder.append(" ORDER BY ");

                // Map sort field to column name
                String columnName;
                switch (sortBy.toLowerCase()) {
                    case "name":
                        columnName = "i.name";
                        break;
                    case "unit":
                        columnName = "i.unit";
                        break;
                    case "price":
                        columnName = "p.unit_price";
                        break;
                    case "date":
                    case "lastmodified":
                        columnName = "p.date";
                        break;
                    default:
                        columnName = "i.id_ingredient";
                        break;
                }

                queryBuilder.append(columnName);
                queryBuilder.append(filter.getSortAscending().orElse(true) ? " ASC" : " DESC");
            });

            // Count total number of items for pagination
            String countQuery = "SELECT COUNT(*) FROM (" + queryBuilder.toString() + ") AS count_query";
            long totalItems;

            try (PreparedStatement countStmt = connection.prepareStatement(countQuery)) {
                for (int i = 0; i < parameters.size(); i++) {
                    countStmt.setObject(i + 1, parameters.get(i));
                }

                try (ResultSet countRs = countStmt.executeQuery()) {
                    countRs.next();
                    totalItems = countRs.getLong(1);
                }
            }

            // Add limit and offset for pagination
            queryBuilder.append(" LIMIT ? OFFSET ?");
            parameters.add(filter.getPageSize());
            parameters.add(filter.getPage() * filter.getPageSize());

            // Execute query
            try (PreparedStatement stmt = connection.prepareStatement(queryBuilder.toString())) {
                for (int i = 0; i < parameters.size(); i++) {
                    stmt.setObject(i + 1, parameters.get(i));
                }

                try (ResultSet rs = stmt.executeQuery()) {
                    List<Ingredient> ingredients = new ArrayList<>();
                    while (rs.next()) {
                        ingredients.add(ingredientMapper.mapFromResultSet(rs));
                    }

                    return new PagedResult<>(ingredients, filter.getPage(), filter.getPageSize(), totalItems);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding ingredients", e);
        }
    }

    private void appendWhereCondition(StringBuilder whereClause) {
        if (whereClause.length() > 0) {
            whereClause.append(" AND ");
        }
    }
}
