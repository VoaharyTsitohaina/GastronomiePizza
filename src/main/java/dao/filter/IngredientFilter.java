package dao.filter;

import entity.Unit;

import java.time.LocalDate;
import java.util.Optional;

public class IngredientFilter {
    private Optional<String> nameContains = Optional.empty();
    private Optional<Unit> unit = Optional.empty();
    private Optional<Double> minPrice = Optional.empty();
    private Optional<Double> maxPrice = Optional.empty();
    private Optional<LocalDate> modifiedAfter = Optional.empty();
    private Optional<LocalDate> modifiedBefore = Optional.empty();
    private Optional<String> sortBy = Optional.empty();
    private Optional<Boolean> sortAscending = Optional.of(true);
    private int page = 0;
    private int pageSize = 10;

    public Optional<String> getNameContains() {
        return nameContains;
    }

    public IngredientFilter setNameContains(String nameContains) {
        this.nameContains = Optional.ofNullable(nameContains);
        return this;
    }

    public Optional<Unit> getUnit() {
        return unit;
    }

    public IngredientFilter setUnit(Unit unit) {
        this.unit = Optional.ofNullable(unit);
        return this;
    }

    public Optional<Double> getMinPrice() {
        return minPrice;
    }

    public IngredientFilter setMinPrice(Double minPrice) {
        this.minPrice = Optional.ofNullable(minPrice);
        return this;
    }

    public Optional<Double> getMaxPrice() {
        return maxPrice;
    }

    public IngredientFilter setMaxPrice(Double maxPrice) {
        this.maxPrice = Optional.ofNullable(maxPrice);
        return this;
    }

    public Optional<LocalDate> getModifiedAfter() {
        return modifiedAfter;
    }

    public IngredientFilter setModifiedAfter(LocalDate modifiedAfter) {
        this.modifiedAfter = Optional.ofNullable(modifiedAfter);
        return this;
    }

    public Optional<LocalDate> getModifiedBefore() {
        return modifiedBefore;
    }

    public IngredientFilter setModifiedBefore(LocalDate modifiedBefore) {
        this.modifiedBefore = Optional.ofNullable(modifiedBefore);
        return this;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public IngredientFilter setSortBy(String sortBy) {
        this.sortBy = Optional.ofNullable(sortBy);
        return this;
    }

    public Optional<Boolean> getSortAscending() {
        return sortAscending;
    }

    public IngredientFilter setSortAscending(Boolean sortAscending) {
        this.sortAscending = Optional.ofNullable(sortAscending);
        return this;
    }

    public int getPage() {
        return page;
    }

    public IngredientFilter setPage(int page) {
        this.page = Math.max(0, page);
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public IngredientFilter setPageSize(int pageSize) {
        this.pageSize = Math.max(1, pageSize);
        return this;
    }
}
