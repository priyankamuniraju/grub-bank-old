package com.grubbank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grubbank.apimodel.RecipeSearchCriteria;
import com.grubbank.entity.Recipe;
import com.grubbank.exception.RecipeNotFoundException;
import java.util.List;

public interface GrubBankCRUDService {

  /**
   *
   * @param recipe
   * @return
   * @throws RecipeValidator.InvalidRecipeException
   */
  Recipe saveRecipe(Recipe recipe) throws RecipeValidator.InvalidRecipeException;

  /** @return the list of all the recipes in the grubbank db */
  List<Recipe> getAllRecipes();

  /**
   * @param recipeName the name of the requested recipe
   * @return the list of recipes based on the requested recipe name
   */
  List<Recipe> getRecipeByName(String recipeName);

  /**
   * Takes as recipe object filled with the fields which need to be updated in the datastore. Only
   * non-null fields in the input are updated on the DB, null values are ignored. This gives
   * flexibility to update only certain fields of the Recipe instead of all the fields. This will
   * also protect from accidentally updating fields which we don't want to update.
   *
   * <p>Alternate update methods can be created for updating each individual field so that the
   * update can be more granular, scope for improvement in that area.
   *
   * @param recipe the updated recipe
   * @param recipeId the recipe id of the recipe that needs to be updated
   * @return The updated Recipe object after it is successfully persisted to the datastore
   * @throws RecipeValidator.InvalidRecipeException If the information passed as part of the input
   *     recipe object are invalid
   * @throws RecipeNotFoundException If the recipe object which is passed to update is not found in
   *     the datastore
   * @throws JsonProcessingException If the parsing of the recipe object passed to extract only the
   *     fields which need to be updated fails. This is done using the json processing so that only
   *     the fields which are sent in the input are updated.
   */
  Recipe updateRecipe(Recipe recipe, int recipeId)
      throws RecipeValidator.InvalidRecipeException, RecipeNotFoundException,
          JsonProcessingException;

  /** @param recipeId the recipe id that needs to be deleted */
  void deleteRecipeById(int recipeId) throws RecipeNotFoundException;

  void searchByCriteria(RecipeSearchCriteria recipeSearchCriteria);

  /**
   * @param recipeSearchCriteria the object which holds various search criteria
   * @return the recipe list based on the requested search criteria
   */
  //    List<Recipe> getRecipeBasedOnCriteria(RecipeSearchCriteria recipeSearchCriteria);
}
