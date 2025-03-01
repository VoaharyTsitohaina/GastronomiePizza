import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import entity.Dish;
import entity.DishIngredient;
import entity.Ingredient;
import entity.Unit;

public class DishCostAndMarginTest {

    @Test
    public void testDefaultDateCostAndMargin() {
        // Simuler la date par défaut comme étant le 2025-01-01
        LocalDate defaultDate = LocalDate.of(2025, 1, 1);

        // Créer les ingrédients avec les prix correspondant à la date par défaut
        Ingredient saucisse = new Ingredient(1, "saucisse", Unit.G, 20.00, defaultDate);
        Ingredient huile = new Ingredient(2, "huile", Unit.L, 10000.00, defaultDate);
        Ingredient oeuf = new Ingredient(3, "oeuf", Unit.U, 1000.00, defaultDate);
        Ingredient pain = new Ingredient(4, "pain", Unit.U, 1000.00, defaultDate);

        // Créer les associations dish-ingredient avec les quantités requises
        DishIngredient di1 = new DishIngredient();
        di1.setIngredient(saucisse);
        di1.setRequiredQuantity(100.0);
        di1.setUnit(Unit.U);

        DishIngredient di2 = new DishIngredient();
        di2.setIngredient(huile);
        di2.setRequiredQuantity(0.15);
        di2.setUnit(Unit.L);

        DishIngredient di3 = new DishIngredient();
        di3.setIngredient(oeuf);
        di3.setRequiredQuantity(1.0);
        di3.setUnit(Unit.U);

        DishIngredient di4 = new DishIngredient();
        di4.setIngredient(pain);
        di4.setRequiredQuantity(1.0);
        di4.setUnit(Unit.U);

        List<DishIngredient> ingredients = Arrays.asList(di1, di2, di3, di4);

        // Créer le plat Hot Dog avec un prix de vente unitaire de 15000
        Dish hotDog = new Dish(1, "Hot Dog", 15000.00, ingredients);

        // Calculer le coût des ingrédients et la marge brute pour la date par défaut
        double ingredientsCost = hotDog.getIngredientsCost(defaultDate);
        double grossMargin = hotDog.getGrossMargin(defaultDate);

        assertEquals(5500.00, ingredientsCost, 0.001, "Le coût des ingrédients pour la date par défaut doit être 5500");
        assertEquals(9500.00, grossMargin, 0.001, "La marge brute pour la date par défaut doit être 9500");
    }

    @Test
    public void testSpecificDateCostAndMargin() {
        // Pour une date donnée, par exemple le 2024-11-15,
        // on utilise les prix qui étaient en vigueur au 2024-11-01.
        LocalDate testDate = LocalDate.of(2024, 11, 15);

        Ingredient saucisse = new Ingredient(1, "saucisse", Unit.G, 15.00, LocalDate.of(2024, 11, 1));
        Ingredient huile = new Ingredient(2, "huile", Unit.L, 9000.00, LocalDate.of(2024, 11, 1));
        Ingredient oeuf = new Ingredient(3, "oeuf", Unit.U, 900.00, LocalDate.of(2024, 11, 1));
        Ingredient pain = new Ingredient(4, "pain", Unit.U, 1000.00, LocalDate.of(2024, 11, 1));

        DishIngredient di1 = new DishIngredient();
        di1.setIngredient(saucisse);
        di1.setRequiredQuantity(100.0);
        di1.setUnit(Unit.U);

        DishIngredient di2 = new DishIngredient();
        di2.setIngredient(huile);
        di2.setRequiredQuantity(0.15);
        di2.setUnit(Unit.L);

        DishIngredient di3 = new DishIngredient();
        di3.setIngredient(oeuf);
        di3.setRequiredQuantity(1.0);
        di3.setUnit(Unit.U);

        DishIngredient di4 = new DishIngredient();
        di4.setIngredient(pain);
        di4.setRequiredQuantity(1.0);
        di4.setUnit(Unit.U);

        List<DishIngredient> ingredients = Arrays.asList(di1, di2, di3, di4);
        Dish hotDog = new Dish(1, "Hot Dog", 15000.00, ingredients);

        double ingredientsCost = hotDog.getIngredientsCost(testDate);
        double grossMargin = hotDog.getGrossMargin(testDate);

        assertEquals(4750.00, ingredientsCost, 0.001, "Le coût des ingrédients pour le 2024-11-15 doit être 4750");
        assertEquals(10250.00, grossMargin, 0.001, "La marge brute pour le 2024-11-15 doit être 10250");
    }
}
