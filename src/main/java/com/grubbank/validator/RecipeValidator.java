package com.grubbank.validator;

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
      throw new InvalidRecipeException(
          com.grubbank.validator.InvalidField.INSTRUCTIONS, "Instructions cannot be empty");
    }
    if (recipe.getNumberOfServings() <= 0) {
      throw new InvalidRecipeException(
          com.grubbank.validator.InvalidField.NUMBER_SERVING,
          "Number of servings should be greater than zero");
    }
    if (recipe.getPreparationTime() == null || recipe.getPreparationTime().isNegative()) {
      throw new InvalidRecipeException(
          com.grubbank.validator.InvalidField.PREPARATION_TIME,
          "Preparation time should be positive");
    }
    if (recipe.getIngredientList() == null || recipe.getIngredientList().isEmpty()) {
      throw new InvalidRecipeException(
          com.grubbank.validator.InvalidField.INGREDIENT, "Ingredients cannot be empty");
    }
    if (!StringUtils.hasText(recipe.getName())) {
      throw new InvalidRecipeException(
          com.grubbank.validator.InvalidField.NAME, "Recipe name cannot be empty");
    }
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
