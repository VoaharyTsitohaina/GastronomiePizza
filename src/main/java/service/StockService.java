package service;

import dao.StockMovementDAO;
import entity.Ingredient;
import entity.StockMovement;
import entity.MovementType;

import java.time.LocalDateTime;
import java.util.List;

public class StockService {
    private final StockMovementDAO stockMovementDAO;

    public StockService(StockMovementDAO stockMovementDAO) {
        this.stockMovementDAO = stockMovementDAO;
    }

    // Calcule la quantité disponible pour un ingrédient à une date donnée.
    // On part d'un stock initial de 0, puis on ajoute les entrées et soustrait les sorties.
    public double getAvailableQuantity(Ingredient ingredient, LocalDateTime date) {
        List<StockMovement> movements = stockMovementDAO.getStockMovementsForIngredient(ingredient.getId(), date);
        double available = 0;
        for (StockMovement sm : movements) {
            if (sm.getMovementType() == MovementType.entree) {
                available += sm.getQuantity();
            } else if (sm.getMovementType() == MovementType.sortie) {
                available -= sm.getQuantity();
            }
        }
        return available;
    }
}
