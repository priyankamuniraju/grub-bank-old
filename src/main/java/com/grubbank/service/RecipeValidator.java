package com.grubbank.service;

import com.grubbank.entity.Ingredient;
import com.grubbank.entity.Recipe;
import com.grubbank.exception.MandatoryFieldsMissingException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;

/**
 * Validates the recipe which is sent for updating and creating a recipe
 */
@Component
public class RecipeValidator {

    public void validateRecipe(Recipe recipe) throws InvalidRecipeException {
        if(recipe.getName() == null || recipe.getRecipeType()==null || recipe.getIngredientSet()==null || recipe.getInstructions()==null){
            throw new MandatoryFieldsMissingException("One or more of the mandatory fields : name/recipeType/ingredientSet/instructions is missing");
        }
        if (!StringUtils.hasText(recipe.getInstructions())) {
            throw new InvalidRecipeException(InvalidField.INSTRUCTIONS, "Instructions cannot be empty");
        }
        if (recipe.getRecipeType().name().isEmpty()) {
            throw new InvalidRecipeException(InvalidField.RECIPE_TYPE, "Recipe type cannot be empty");
        }
        if (recipe.getNumberOfServings() <= 0) {
            throw new InvalidRecipeException(InvalidField.NUMBER_SERVING, "Number of servings should be greater than zero");
        }
        if (recipe.getPreparationTime() != null && recipe.getPreparationTime().isNegative() ) {
            throw new InvalidRecipeException(InvalidField.PREPARATION_TIME, "Preparation time should be positive");
        }
        if (recipe.getIngredientSet().isEmpty()) {
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
        RECIPE_TYPE,
        INSTRUCTIONS
    }

    public static class InvalidRecipeException extends Exception {
        private final InvalidField invalidField;

        public InvalidRecipeException(InvalidField invalidField, String message) {
            super(String.format("Invalid input data for the field : %s", invalidField.name()));
            this.invalidField = invalidField;
        }
    }
}


