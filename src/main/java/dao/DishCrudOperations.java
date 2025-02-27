package dao;

import dao.mapper.UnitMapper;
import db.DataSource;

public class DishCrudOperations {
    private final DataSource dataSource;
    private final UnitMapper unitMapper;

    public DishCrudOperations(DataSource dataSource, UnitMapper unitMapper) {
        this.dataSource = dataSource;
        this.unitMapper = unitMapper;
    }

    public DishCrudOperations() {
        this.dataSource = new DataSource();
        this.unitMapper = new UnitMapper();
    }


}
