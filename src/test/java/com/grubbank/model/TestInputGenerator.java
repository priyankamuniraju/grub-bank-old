package com.grubbank.model;

import static com.grubbank.entity.NutritionalValue.Unit.GM;
import static com.grubbank.entity.NutritionalValue.Unit.JOULE;

import com.grubbank.entity.Ingredient;
import com.grubbank.entity.NutritionalValue;
import com.grubbank.entity.Recipe;
import java.util.ArrayList;
import java.util.List;

public class TestInputGenerator {

  public static Recipe createValidRecipe() {
    List<Ingredient> ingredientList = createIngredientList();
    Recipe recipe = new Recipe();
    recipe.setName("Kheer");
    recipe.setIngredientSet(ingredientList);
    recipe.setNumberOfServings(4);
    recipe.setRecipeType(Recipe.RecipeType.VEGETARIAN);
    //        recipe.setPreparationTime.;
    recipe.setInstructions(
        "Boil milk in a bowl, fry semolina in a pan, "
            + "add the fried semolina to the boiling milk, add sugar");
    recipe.setNutritionalValue(createNutritionalValue());
    return recipe;
  }

  public static List<Ingredient> createIngredientList() {
    Ingredient ingredientOne = new Ingredient();
    ingredientOne.setName("sugar");
    Ingredient ingredientTwo = new Ingredient();
    ingredientTwo.setName("milk");
    Ingredient ingredientThree = new Ingredient();
    ingredientThree.setName("semolina");

    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(ingredientOne);
    ingredients.add(ingredientTwo);
    ingredients.add(ingredientThree);
    return ingredients;
  }

  public static NutritionalValue createNutritionalValue() {
    NutritionalValue.Calorie calorie = new NutritionalValue.Calorie();
    calorie.setValue(100);
    calorie.setUnit(JOULE);
    NutritionalValue.Nutrient fat = new NutritionalValue.Nutrient();
    fat.setValue(50);
    fat.setUnit(GM);
    NutritionalValue.Nutrient carbs = new NutritionalValue.Nutrient();
    carbs.setValue(50);
    carbs.setUnit(GM);
    NutritionalValue.Nutrient protien = new NutritionalValue.Nutrient();
    protien.setValue(50);
    protien.setUnit(GM);

    calorie.setValue(100);
    calorie.setUnit(JOULE);
    NutritionalValue nutritionalValue = new NutritionalValue();
    nutritionalValue.setCalories(calorie);
    nutritionalValue.setCarbs(carbs);
    nutritionalValue.setProteins(protien);
    return nutritionalValue;
  }
}
