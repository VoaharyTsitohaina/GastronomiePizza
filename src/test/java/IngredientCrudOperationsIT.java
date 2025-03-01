import dao.IngredientCrudOperations;
import dao.filter.IngredientFilter;
import dao.mapper.IngredientMapper;
import dao.pagination.PagedResult;
import db.DataSource;
import entity.Ingredient;
import entity.Unit;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IngredientCrudOperationsIT {

    IngredientCrudOperations subject = new IngredientCrudOperations();

    @Test
    void testFilterByName() {
        // On filtre par nom contenant "tom" (non sensible à la casse)
        IngredientFilter filter = new IngredientFilter().setNameContains("sau");
        PagedResult<Ingredient> result = subject.findIngredients(filter);

        // On attend qu'un seul ingrédient soit retourné : "Tomate"
        assertEquals(1, result.getItems().size());
        assertEquals("saucisse", result.getItems().get(0).getName());
    }

    @Test
    void testFilterByUnit() {
        IngredientFilter filter = new IngredientFilter().setUnit(Unit.U);
        PagedResult<Ingredient> result = subject.findIngredients(filter);

        assertEquals(2, result.getItems().size());
    }

    @Test
    void testFilterByPriceRange() {
        // On filtre pour les ingrédients avec un prix entre 2.0 et 2.5
        IngredientFilter filter = new IngredientFilter().setMinPrice(900.00).setMaxPrice(9000.00);
        PagedResult<Ingredient> result = subject.findIngredients(filter);

        // Tomate (2.5) et Carotte (2.0) devraient être retournés
        assertEquals(2, result.getItems().size());
    }

    @Test
    void testFilterByDateRange() {
        // On filtre pour les ingrédients dont la date de modification est postérieure ou égale au 2024-01-02
        IngredientFilter filter = new IngredientFilter().setModifiedAfter(LocalDate.of(2024, 11, 2));
        PagedResult<Ingredient> result = subject.findIngredients(filter);

        assertEquals(4, result.getItems().size());
    }

    @Test
    void testSortingAndPagination() {
        // On trie par prix en ordre décroissant et on pagine (page 0, taille 2)
        IngredientFilter filter = new IngredientFilter()
                .setSortBy("price")
                .setSortAscending(false)
                .setPage(0)
                .setPageSize(2);
        PagedResult<Ingredient> result = subject.findIngredients(filter);

        // Ordre attendu par prix décroissant :
        // Oeuf (3.0), Tomate (2.5), Carotte (2.0), Lait (1.8)
        List<Ingredient> ingredients = result.getItems();
        assertEquals(2, ingredients.size());
        assertEquals("huile", ingredients.get(0).getName());
        assertEquals("oeuf", ingredients.get(1).getName());
    }
}
