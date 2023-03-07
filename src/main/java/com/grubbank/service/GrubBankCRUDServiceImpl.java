package com.grubbank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.grubbank.entity.Recipe;
import com.grubbank.exception.RecipeNotFoundException;
import com.grubbank.repository.GrubBankRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GrubBankCRUDServiceImpl implements GrubBankCRUDService {

  @Autowired private GrubBankRepository grubBankRepository;

  @Autowired private RecipeValidator recipeValidator;

  @Autowired
  private ObjectMapper objectMapper;

  /**
   * @param recipe the recipe that needs to be saved in the db
   * @return
   * @throws RecipeValidator.InvalidRecipeException
   */
  @Override
  public Recipe saveRecipe(Recipe recipe) throws RecipeValidator.InvalidRecipeException {
    recipeValidator(recipe);
    return grubBankRepository.save(recipe);
  }

  /** @return the list of recipes from the grub bank db */
  @Override
  public List<Recipe> getAllRecipes() {
    return (List<Recipe>) grubBankRepository.findAll();
  }

  /**
   * @param recipeName the name of the requested recipe
   * @return the requested recipe based on the recipeName
   * @throws RecipeNotFoundException
   */
  @Override
  public List<Recipe> getRecipeByName(String recipeName) {
    return grubBankRepository.searchRecipesByName(recipeName.toUpperCase());
  }

  /**
   * @param recipe the updated recipe
   * @param recipeId the recipe id of the recipe that needs to be updated
   * @return the updated recipe
   * @throws RecipeValidator.InvalidRecipeException
   */
  @Override
  @Transactional
  public Recipe updateRecipe(Recipe recipe, int recipeId)
      throws RecipeValidator.InvalidRecipeException, RecipeNotFoundException,
          JsonProcessingException {

    Optional<Recipe> recipeInDBMaybe = grubBankRepository.findById(recipeId);
    if (recipeInDBMaybe.isEmpty()) {
      throw new RecipeNotFoundException(
          String.format("No recipe with recipe id :%s found in the grub bank.", recipeId));
    } else {
      // grab only the fields that are to be updated
      ObjectNode recipeFromCallerNode = objectMapper.valueToTree(recipe);
      Recipe recipeInDB = recipeInDBMaybe.get();
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
      return grubBankRepository.save(recipeInDB);
    }
  }

  /** @param recipeId the recipe id that needs to be deleted */
  @Override
  public void deleteRecipeById(int recipeId) throws RecipeNotFoundException {
    Optional<Recipe> recipe = grubBankRepository.findById(recipeId);
    if (recipe.isEmpty()) {
      throw new RecipeNotFoundException("No recipe found with recipe id : " + recipeId);
    }
    grubBankRepository.deleteById(recipeId);
  }

  public void recipeValidator(Recipe recipe) throws RecipeValidator.InvalidRecipeException {
    recipeValidator.validateRecipe(recipe);
  }
}
