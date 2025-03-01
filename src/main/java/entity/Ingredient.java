package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Ingredient {
    private long id;
    private String name;
    private Unit unit;
    private Double currentPrice;
    private LocalDate lastModified;

    public Ingredient() {
    }

    public Ingredient(long id, String name, Unit unit, Double currentPrice, LocalDate lastModified) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.currentPrice = currentPrice;
        this.lastModified = lastModified;
    }

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

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    // Méthode permettant d'obtenir la quantité disponible à une date donnée via le service StockService.
    // Par défaut, on peut surcharger en utilisant la date du jour.
    public double getAvailableQuantity(service.StockService stockService, LocalDateTime date) {
        return stockService.getAvailableQuantity(this, date);
    }

    // Version par défaut qui utilise la date du jour
    public double getAvailableQuantity(service.StockService stockService) {
        return getAvailableQuantity(stockService, LocalDateTime.now());
    }
}
