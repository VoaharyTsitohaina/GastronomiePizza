package dao.mapper;

import entity.Unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnitMapper {
    public Unit mapFromResultSet(String stringValue) {
        if (stringValue == null) {
            return null;
        }
        List<Unit> unitList = Arrays.stream(Unit.values()).toList();
        return unitList.stream().filter(
                unit -> stringValue.equals(unit.toString())
        ).findAny().orElseThrow(() -> new IllegalArgumentException("Unknown Unit value" + stringValue));
    }
}
