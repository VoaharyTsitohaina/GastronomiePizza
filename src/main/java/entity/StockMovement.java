package entity;

import java.time.LocalDateTime;

public class StockMovement {
    private long id;
    private long ingredientId; // Référence à l'ingrédient
    private MovementType movementType; // Utilisation de l'enum MovementType
    private double quantity;
    private LocalDateTime movementDateTime;

    public StockMovement() {}

    public StockMovement(long id, long ingredientId, MovementType movementType, double quantity, LocalDateTime movementDateTime) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.movementType = movementType;
        this.quantity = quantity;
        this.movementDateTime = movementDateTime;
    }

    // Getters et setters
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getIngredientId() {
        return ingredientId;
    }
    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
    }
    public MovementType getMovementType() {
        return movementType;
    }
    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }
    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    public LocalDateTime getMovementDateTime() {
        return movementDateTime;
    }
    public void setMovementDateTime(LocalDateTime movementDateTime) {
        this.movementDateTime = movementDateTime;
    }
}
