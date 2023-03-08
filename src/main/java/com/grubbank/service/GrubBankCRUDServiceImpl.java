package com.grubbank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.grubbank.apimodel.RecipeSearchCriteria;
import com.grubbank.entity.Recipe;
import com.grubbank.exception.RecipeNotFoundException;
import com.grubbank.repository.IngredientRepository;
import com.grubbank.repository.NutritionalValueRepository;
import com.grubbank.repository.RecipeRepository;
import com.grubbank.validator.RecipeSearchCriteriaValidator;
import com.grubbank.validator.RecipeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GrubBankCRUDServiceImpl implements GrubBankCRUDService {

  @Autowired private RecipeRepository recipeRepository;
  @Autowired private IngredientRepository ingredientRepository;
  @Autowired private NutritionalValueRepository nutritionalValueRepository;
  @Autowired private RecipeValidator recipeValidator;
  @Autowired private ObjectMapper objectMapper;

  /**
   * This particular implementation saves the corresponding relationships and then saves the parent.
   * In this case the {@link com.grubbank.entity.Ingredient} and {@link
   * com.grubbank.entity.NutritionalValue} are the referenced entities, for better management and to
   * reduce duplicates in the datastore (typically RDBMS sql DBs), we would save the relationships
   * first. In this way if there is already an Ingredient (id) and NutritionalValue (id) present in
   * the datastore, we don't have to recreate them, generally Cascade ALL, PERSIST would try to
   * recreate these records, leading to duplicated data. Either the update or create will fail to
   * with these cascade types if we want to avoid duplicates. So it is better to manage the
   * referenced entities separately. Hence, in this case we save the {@link
   * com.grubbank.entity.Ingredient} and {@link com.grubbank.entity.NutritionalValue} first, if no
   * id is passed in the input these are created, and if an id is passed these are updated/not
   * updated, without creating new duplicate entry in the database. Typically, when the clients
   * choose a referenced entity, such as {@link com.grubbank.entity.Ingredient} or {@link
   * com.grubbank.entity.NutritionalValue} they would either have a list of these to choose from, in
   * which case the corresponding id cane be safely passed, or they would create a new one. In this
   * either case the following API implementation would work.
   *
   * @param recipe The {@link Recipe} to be saved
   * @return Saved {@link Recipe}
   * @throws RecipeValidator.InvalidRecipeException when the passed {@link Recipe} doesn't meet the
   *     required validations see {@link RecipeValidator}
   */
  @Override
  @Transactional
  public Recipe saveRecipe(Recipe recipe) throws RecipeValidator.InvalidRecipeException {
    recipeValidator.validateRecipe(recipe);
    recipe.getIngredientList().forEach(ingredient -> ingredientRepository.save(ingredient));
    nutritionalValueRepository.save(recipe.getNutritionalValue());
    return recipeRepository.save(recipe);
  }

  @Override
  public List<Recipe> getAllRecipes() {
    return (List<Recipe>) recipeRepository.findAll();
  }

  @Override
  public List<Recipe> getRecipeByName(String recipeName) {
    return recipeRepository.searchRecipesByName(recipeName.toUpperCase());
  }

  /**
   * In this particular implementation we manage the referenced entities first and then the
   * referencing entity See {@link GrubBankCRUDServiceImpl#saveRecipe(Recipe)} for details.
   */
  @Override
  @Transactional
  public Recipe updateRecipe(Recipe recipe, int recipeId)
      throws RecipeValidator.InvalidRecipeException, RecipeNotFoundException,
          JsonProcessingException {

    Optional<Recipe> recipeInDBMaybe = recipeRepository.findById(recipeId);
    if (recipeInDBMaybe.isEmpty()) {
      throw new RecipeNotFoundException(
          String.format("No recipe with recipe id :%s found in the grub bank.", recipeId));
    } else {
      // grab only the fields that are to be updated
      ObjectNode recipeFromCallerNode = objectMapper.valueToTree(recipe);
      Recipe recipeInDB = recipeInDBMaybe.get();

      if (recipe.getIngredientList() != null) {
        // Update the ingredients only if sent as part of the input
        recipe.getIngredientList().forEach(ingredient -> ingredientRepository.save(ingredient));
      }

      if (recipe.getNutritionalValue() != null) {
        // Update the nutritionalValue only if sent as part of the input
        nutritionalValueRepository.save(recipe.getNutritionalValue());
      }

      // Convert the recipe from the DB to a Json node so that attributes can be set easily
      ObjectNode recipeFromDBNode = objectMapper.valueToTree(recipeInDB);
      // Iterate through the changed params from the caller (client) and update the same in the
      // recipeFromDBNode
      recipeFromCallerNode
          .fields()
          .forEachRemaining(
              nodeEntry -> recipeFromDBNode.set(nodeEntry.getKey(), nodeEntry.getValue()));
      recipeInDB = objectMapper.treeToValue(recipeFromDBNode, Recipe.class);
      recipeInDB.setId(recipeId);

      // Verify whether the updated values are valid
      recipeValidator.validateRecipe(recipeInDB);

      // Update the recipeInDB with the updated values
      return recipeRepository.save(recipeInDB);
    }
  }

  @Override
  public void deleteRecipeById(int recipeId) throws RecipeNotFoundException {
    Optional<Recipe> recipe = recipeRepository.findById(recipeId);
    if (recipe.isEmpty()) {
      throw new RecipeNotFoundException("No recipe found with recipe id : " + recipeId);
    }
    recipeRepository.deleteById(recipeId);
  }

  @Override
  public List<Recipe> searchByCriteria(RecipeSearchCriteria recipeSearchCriteria)
      throws RecipeSearchCriteriaValidator.InvalidRecipeSearchCriteriaException {
    return recipeRepository.listAllRecipes(recipeSearchCriteria);
  }
}
