package com.grubbank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grubbank.entity.Recipe;
import com.grubbank.exception.RecipeNotFoundException;
import java.util.List;

public interface GrubBankCRUDService {
  /** @param recipe the recipe that needs to be saved in the db */
  Recipe saveRecipe(Recipe recipe) throws RecipeValidator.InvalidRecipeException;

  /** @return the list of all the recipes in the grubbank db */
  List<Recipe> getAllRecipes();

  /**
   * @param recipeName the name of the requested recipe
   * @return the list of recipes based on the requested recipe name
   */
  List<Recipe> getRecipeByName(String recipeName);

  /**
   * @param recipe the updated recipe
   * @param recipeId the recipe id of the recipe that needs to be updated
   * @return the updated recipe
   */
  Recipe updateRecipe(Recipe recipe, int recipeId)
      throws RecipeValidator.InvalidRecipeException, RecipeNotFoundException,
          JsonProcessingException;

  /** @param recipeId the recipe id that needs to be deleted */
  void deleteRecipeById(int recipeId) throws RecipeNotFoundException;

  /**
   * @param recipeSearchCriteria the object which holds various search criteria
   * @return the recipe list based on the requested search criteria
   */
  //    List<Recipe> getRecipeBasedOnCriteria(RecipeSearchCriteria recipeSearchCriteria);
}
