package com.grubbank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grubbank.apimodel.RecipeSearchCriteria;
import com.grubbank.entity.Recipe;
import com.grubbank.exception.RecipeNotFoundException;
import com.grubbank.validator.RecipeSearchCriteriaValidator;
import com.grubbank.validator.RecipeValidator;
import java.util.List;

public interface GrubBankCRUDService {

  /**
   * Persists the recipe in the datastore after processing.
   *
   * @param recipe The {@link Recipe} to be saved
   * @return The successfully saved {@link Recipe}
   * @throws RecipeValidator.InvalidRecipeException when the passed {@link Recipe} doesn't meet the
   *     required validations see {@link RecipeValidator}
   */
  default Recipe saveRecipe(Recipe recipe) throws RecipeValidator.InvalidRecipeException {
    return null;
  }

  /**
   * Returns all the recipes currently present in the datastore. Currently, it doesn't support
   * pagination of results, so if there are huge number of recipes this api should be modified
   * accordingly to support pagination
   *
   * @return List of the {@link Recipe} in the datastore
   */
  List<Recipe> getAllRecipes();

  /**
   * Returns list of recipes by name, if there are multiple recipes with same name or matching name
   * it will return all. Similar to the {@link GrubBankCRUDService#getAllRecipes()} this api too is
   * a basic implementation, if there are a lot of recipes with similar name, this api should be
   * upgraded accordingly. See {@link GrubBankCRUDService#getAllRecipes()}
   *
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

  /**
   * Deletes the recipe with the provided id. Only one entry is deleted.
   *
   * @param recipeId The unique id of the recipe which needs to be deleted
   * @throws RecipeNotFoundException If no recipe is found with the passed recipeId
   */
  void deleteRecipeById(int recipeId) throws RecipeNotFoundException;

  /**
   * Searches the datastore with the provided search criteria, see {@link RecipeSearchCriteria} for
   * what criteria are supported currently
   *
   * @param recipeSearchCriteria The search criteria for which the list of {@link Recipe} should be
   *     fetched
   * @return List of {@link Recipe} which match the provided search criteria
   * @throws RecipeSearchCriteriaValidator.InvalidRecipeSearchCriteriaException If an invalid search
   *     criteria is passed.
   */
  List<Recipe> searchByCriteria(RecipeSearchCriteria recipeSearchCriteria)
      throws RecipeSearchCriteriaValidator.InvalidRecipeSearchCriteriaException;
}
