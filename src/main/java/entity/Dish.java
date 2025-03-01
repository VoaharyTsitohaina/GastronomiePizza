package entity;

import java.time.LocalDate;
import java.util.List;

public class Dish {
    private long id;
    private String name;
    private double unitPrice; // prix de vente unitaire
    private List<DishIngredient> dishIngredients;

    public Dish() {
    }

    public Dish(long id, String name, double unitPrice, List<DishIngredient> dishIngredients) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.dishIngredients = dishIngredients;
    }

    // Renvoie le coût total des ingrédients en utilisant la date spécifiée.
    // Pour chaque ingrédient, on suppose que l'objet Ingredient contient déjà
    // la valeur du prix pertinent (c'est-à-dire le prix le plus récent inférieur ou égal à la date donnée).
    public double getIngredientsCost(LocalDate date) {
        double total = 0;
        for (DishIngredient di : dishIngredients) {
            // On utilise ici la quantité requise et le prix courant de l'ingrédient
            total += di.getRequiredQuantity() * di.getIngredient().getCurrentPrice();
        }
        return total;
    }

    // Méthode par défaut qui utilise la date du jour
    public double getIngredientsCost() {
        return getIngredientsCost(LocalDate.now());
    }

    // Calcule la marge brute pour une date donnée : prix de vente unitaire - coût des ingrédients
    public double getGrossMargin(LocalDate date) {
        return unitPrice - getIngredientsCost(date);
    }

    // Calcule la marge brute en utilisant la date du jour
    public double getGrossMargin() {
        return getGrossMargin(LocalDate.now());
    }

    // Getters et setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public List<DishIngredient> getDishIngredients() {
        return dishIngredients;
    }

    public void setDishIngredients(List<DishIngredient> dishIngredients) {
        this.dishIngredients = dishIngredients;
    }
}
