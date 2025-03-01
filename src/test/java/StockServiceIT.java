import static org.junit.jupiter.api.Assertions.assertEquals;

import db.DataSource;
import dao.StockMovementDAO;
import entity.Ingredient;
import entity.Unit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import service.StockService;

import java.time.LocalDateTime;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StockServiceIT {
    private DataSource dataSource;
    private StockMovementDAO stockMovementDAO;
    private StockService stockService;

    @BeforeAll
    public void setUp() throws Exception {
        dataSource = new DataSource();
        stockMovementDAO = new StockMovementDAO(dataSource);
        stockService = new StockService(stockMovementDAO);
        // On suppose ici que les scripts de migration ont déjà inséré les données de test dans la base.
    }

    @Test
    public void testAvailableQuantityWithoutExitMovements() {
        // Test pour Sauccise (id 1) et Huile (id 2) qui n'ont pas de sorties.
        Ingredient saucisse = new Ingredient();
        saucisse.setId(1);
        saucisse.setName("saucisse");
        saucisse.setUnit(Unit.G);

        Ingredient huile = new Ingredient();
        huile.setId(2);
        huile.setName("huile");
        huile.setUnit(Unit.L);

        // On utilise une date de test : le 24 février 2025 à 12h
        LocalDateTime testDate = LocalDateTime.of(2025, 2, 24, 12, 0);

        double availableSaucisse = stockService.getAvailableQuantity(saucisse, testDate);
        double availableHuile = stockService.getAvailableQuantity(huile, testDate);

        assertEquals(10000.0, availableSaucisse, 0.001, "La quantité disponible de saucisse doit être 10000 g");
        assertEquals(20.0, availableHuile, 0.001, "La quantité disponible d'huile doit être 20 L");
    }

    @Test
    public void testAvailableQuantityWithExitMovements() {
        // Test pour Oeuf (id 3) et Pain (id 4)
        // Pour oeuf : entrée initiale 100, sorties de 10 et 10 → disponible = 80
        // Pour pain : entrée initiale 50 + entrée supplémentaire 50, sortie de 20 → disponible = 80
        Ingredient oeuf = new Ingredient();
        oeuf.setId(3);
        oeuf.setName("oeuf");
        oeuf.setUnit(Unit.U);

        Ingredient pain = new Ingredient();
        pain.setId(4);
        pain.setName("pain");
        pain.setUnit(Unit.U);

        LocalDateTime testDate = LocalDateTime.of(2025, 2, 24, 12, 0);

        double availableOeuf = stockService.getAvailableQuantity(oeuf, testDate);
        double availablePain = stockService.getAvailableQuantity(pain, testDate);

        assertEquals(80.0, availableOeuf, 0.001, "La quantité disponible d'oeuf doit être 80 unités");
        assertEquals(80.0, availablePain, 0.001, "La quantité disponible de pain doit être 80 unités");
    }
}
