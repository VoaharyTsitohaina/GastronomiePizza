package dao;

import entity.MovementType;
import entity.StockMovement;
import db.DataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StockMovementDAO {
    private final DataSource dataSource;

    public StockMovementDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Récupère les mouvements de stock pour un ingrédient donné jusqu'à une date spécifiée (inclus)
    public List<StockMovement> getStockMovementsForIngredient(long ingredientId, LocalDateTime until) {
        List<StockMovement> movements = new ArrayList<>();
        String sql = "SELECT id_stock, id_ingredient, movement_type, quantity, movement_datetime " +
                "FROM stock_movement " +
                "WHERE id_ingredient = ? AND movement_datetime <= ? " +
                "ORDER BY movement_datetime ASC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, ingredientId);
            ps.setTimestamp(2, Timestamp.valueOf(until));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StockMovement sm = new StockMovement();
                    sm.setId(rs.getLong("id_stock"));
                    sm.setIngredientId(rs.getLong("id_ingredient"));
                    // Conversion de la chaîne en minuscule pour correspondre aux constantes de l'énumération
                    String movementTypeStr = rs.getString("movement_type");
                    sm.setMovementType(MovementType.valueOf(movementTypeStr.toLowerCase()));
                    sm.setQuantity(rs.getDouble("quantity"));
                    sm.setMovementDateTime(rs.getTimestamp("movement_datetime").toLocalDateTime());
                    movements.add(sm);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving stock movements", e);
        }
        return movements;
    }

    public void createStockMovement(StockMovement stockMovement) {
        String sql = "INSERT INTO stock_movement (id_ingredient, movement_type, quantity, movement_datetime) " +
                "VALUES (?, ?::movement_type, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, stockMovement.getIngredientId());
            // On insère la valeur en minuscules pour correspondre au type ENUM dans la base
            ps.setString(2, stockMovement.getMovementType().toString().toLowerCase());
            ps.setDouble(3, stockMovement.getQuantity());
            ps.setTimestamp(4, Timestamp.valueOf(stockMovement.getMovementDateTime()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    stockMovement.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating stock movement", e);
        }
    }
}
