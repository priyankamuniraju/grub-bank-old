package com.grubbank.service;

import com.grubbank.entity.Recipe;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/** Validates the recipe which is sent for updating and creating a recipe */
@Component
public class RecipeValidator {

  public void validateRecipe(Recipe recipe) throws InvalidRecipeException {
    if (!StringUtils.hasText(recipe.getInstructions())) {
      throw new InvalidRecipeException(InvalidField.INSTRUCTIONS, "Instructions cannot be empty");
    }
    if (recipe.getNumberOfServings() <= 0) {
      throw new InvalidRecipeException(
          InvalidField.NUMBER_SERVING, "Number of servings should be greater than zero");
    }
    if (recipe.getPreparationTime() == null || recipe.getPreparationTime().isNegative()) {
      throw new InvalidRecipeException(
          InvalidField.PREPARATION_TIME, "Preparation time should be positive");
    }
    if (recipe.getIngredientSet() == null || recipe.getIngredientSet().isEmpty()) {
      throw new InvalidRecipeException(InvalidField.INGREDIENT, "Ingredients cannot be empty");
    }
    if (!StringUtils.hasText(recipe.getName())) {
      throw new InvalidRecipeException(InvalidField.NAME, "Recipe name cannot be empty");
    }
  }

  public enum InvalidField {
    NAME,
    INGREDIENT,
    NUMBER_SERVING,
    PREPARATION_TIME,
    INSTRUCTIONS
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  public static class InvalidRecipeException extends Exception {
    private final InvalidField invalidField;
    private final String detail;

    public InvalidRecipeException(InvalidField invalidField, String message) {
      super(String.format("Invalid input data for the field : %s", invalidField.name()));
      this.invalidField = invalidField;
      this.detail = message;
    }
  }
}
