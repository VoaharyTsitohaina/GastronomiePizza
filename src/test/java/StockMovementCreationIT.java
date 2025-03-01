import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import db.DataSource;
import dao.StockMovementDAO;
import entity.Ingredient;
import entity.MovementType;
import entity.StockMovement;
import entity.Unit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import service.StockService;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StockMovementCreationIT {
    private DataSource dataSource;
    private StockMovementDAO stockMovementDAO;
    private StockService stockService;

    @BeforeAll
    public void setUp() throws Exception {
        dataSource = new DataSource();
        stockMovementDAO = new StockMovementDAO(dataSource);
        stockService = new StockService(stockMovementDAO);

        // Insérer les nouveaux ingrédients "sel" et "riz" dans la table ingredient.
        // On suppose ici que ces identifiants n'entrent pas en conflit avec d'autres tests.
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            // Nettoyage préalable pour les id 5 et 6
            stmt.executeUpdate("DELETE FROM stock_movement WHERE id_ingredient IN (5,6)");
            stmt.executeUpdate("DELETE FROM ingredient WHERE id_ingredient IN (5,6)");
            stmt.executeUpdate("INSERT INTO ingredient (id_ingredient, name, unit) VALUES (5, 'sel', 'G')");
            stmt.executeUpdate("INSERT INTO ingredient (id_ingredient, name, unit) VALUES (6, 'riz', 'G')");
        }
    }

    @Test
    public void testCreateAndRetrieveStockMovementsForNewIngredients() {
        // On définit une date "maintenant" pour les mouvements
        LocalDateTime now = LocalDateTime.now();

        // Pour le sel (id 5) : création d'un mouvement d'entrée de 200 g.
        StockMovement selEntry = new StockMovement();
        selEntry.setIngredientId(5);
        selEntry.setMovementType(MovementType.entree);
        selEntry.setQuantity(200.0);
        selEntry.setMovementDateTime(now);
        stockMovementDAO.createStockMovement(selEntry);
        assertNotNull(selEntry.getId(), "L'ID du mouvement pour le sel doit être généré");

        // Pour le riz (id 6) : création d'une entrée de 150 g, puis d'une sortie de 50 g.
        StockMovement rizEntry = new StockMovement();
        rizEntry.setIngredientId(6);
        rizEntry.setMovementType(MovementType.entree);
        rizEntry.setQuantity(150.0);
        rizEntry.setMovementDateTime(now);
        stockMovementDAO.createStockMovement(rizEntry);
        assertNotNull(rizEntry.getId(), "L'ID du mouvement d'entrée pour le riz doit être généré");

        StockMovement rizExit = new StockMovement();
        rizExit.setIngredientId(6);
        rizExit.setMovementType(MovementType.sortie);
        rizExit.setQuantity(50.0);
        rizExit.setMovementDateTime(now.plusHours(1)); // sortie 1 heure après l'entrée
        stockMovementDAO.createStockMovement(rizExit);
        assertNotNull(rizExit.getId(), "L'ID du mouvement de sortie pour le riz doit être généré");

        // Création des objets Ingredient pour "sel" et "riz"
        Ingredient sel = new Ingredient();
        sel.setId(5);
        sel.setName("sel");
        sel.setUnit(Unit.G);

        Ingredient riz = new Ingredient();
        riz.setId(6);
        riz.setName("riz");
        riz.setUnit(Unit.G);

        // Vérifier via StockService l'état des stocks à une date ultérieure (par ex. now + 2 heures)
        double availableSel = stockService.getAvailableQuantity(sel, now.plusHours(2));
        double availableRiz = stockService.getAvailableQuantity(riz, now.plusHours(2));

        // Pour le sel, seul une entrée de 200 g est enregistrée.
        assertEquals(200.0, availableSel, 0.001, "Le stock disponible pour le sel doit être 200 g");
        // Pour le riz, 150 g d'entrée - 50 g de sortie = 100 g.
        assertEquals(100.0, availableRiz, 0.001, "Le stock disponible pour le riz doit être 100 g");
    }
}
