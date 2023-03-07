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
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GrubBankCRUDServiceImpl implements GrubBankCRUDService {

  @Autowired private RecipeRepository recipeRepository;
  @Autowired private IngredientRepository ingredientRepository;
  @Autowired private NutritionalValueRepository nutritionalValueRepository;

  @Autowired private RecipeValidator recipeValidator;

  @Autowired private ObjectMapper objectMapper;

  @Override
  @Transactional
  public Recipe saveRecipe(Recipe recipe) throws RecipeValidator.InvalidRecipeException {
    recipeValidator.validateRecipe(recipe);
    recipe.getIngredientSet().forEach(ingredient -> ingredientRepository.save(ingredient));
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

      if (recipe.getIngredientSet() != null) {
        // Update the ingredients only if sent as part of the input
        recipe.getIngredientSet().forEach(ingredient -> ingredientRepository.save(ingredient));
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
