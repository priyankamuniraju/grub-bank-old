package com.grubbank.model;

import com.grubbank.entity.Ingredient;
import com.grubbank.entity.NutritionalValue;
import com.grubbank.entity.Recipe;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.grubbank.entity.NutritionalValue.Unit.GM;
import static com.grubbank.entity.NutritionalValue.Unit.JOULE;

public class TestInputGenerator {

  public static Recipe createValidRecipe() {
    List<Ingredient> ingredientList = createIngredientList();
    NutritionalValue nutritionalValue =
        NutritionalValue.builder()
            .calories(NutritionalValue.Calorie.builder().value(100).unit(JOULE).build())
            .carbs(
                NutritionalValue.Nutrient.builder()
                    .value(100)
                    .unit(NutritionalValue.Unit.MG)
                    .build())
            .fat(
                NutritionalValue.Nutrient.builder().value(2).unit(NutritionalValue.Unit.MG).build())
            .proteins(
                NutritionalValue.Nutrient.builder()
                    .value(20)
                    .unit(NutritionalValue.Unit.MG)
                    .build())
            .build();
    return Recipe.builder()
        .name("Kheer")
        .ingredientSet(ingredientList)
        .recipeType(Recipe.RecipeType.VEGETARIAN)
        .nutritionalValue(nutritionalValue)
        .numberOfServings(3)
        .preparationTime(Duration.of(20, ChronoUnit.MINUTES))
        .instructions(
            "Boil milk in a bowl, fry semolina in a pan, add the fried semolina to the boiling milk, add sugar")
        .build();
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
