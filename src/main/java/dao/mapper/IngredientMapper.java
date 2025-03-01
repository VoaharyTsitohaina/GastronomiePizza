package dao.mapper;

import entity.Ingredient;
import entity.Unit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class IngredientMapper {
    private final UnitMapper unitMapper;

    public IngredientMapper() {
        this.unitMapper = new UnitMapper();
    }

    public Ingredient mapFromResultSet(ResultSet rs) throws SQLException {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(rs.getLong("id_ingredient"));
        ingredient.setName(rs.getString("name"));

        // Map unit using existing mapper
        String unitString = rs.getString("unit");
        ingredient.setUnit(unitMapper.mapFromResultSet(unitString));

        // Map price and last modified date
        ingredient.setCurrentPrice(rs.getDouble("unit_price"));

        // Get date from ResultSet, could be null
        java.sql.Date sqlDate = rs.getDate("date");
        if (sqlDate != null) {
            ingredient.setLastModified(sqlDate.toLocalDate());
        }

        return ingredient;
    }
}
