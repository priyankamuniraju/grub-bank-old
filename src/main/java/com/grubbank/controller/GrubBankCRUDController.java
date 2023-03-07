package com.grubbank.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grubbank.apimodel.RecipeSearchCriteria;
import com.grubbank.apimodel.Streamable;
import com.grubbank.entity.Recipe;
import com.grubbank.exception.RecipeNotFoundException;
import com.grubbank.response.ErrorPayload;
import com.grubbank.response.GrubResponseBody;
import com.grubbank.service.GrubBankCRUDService;
import com.grubbank.validator.RecipeSearchCriteriaValidator;
import com.grubbank.validator.RecipeValidator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grubbank")
public class GrubBankCRUDController {

  private static final String RECIPE_ADD_SUCCESS = "Successfully saved the recipe !!";
  private static final String RECIPE_ADD_FAILED = "Failed to save the recipe %s";
  private static final String RECIPE_FETCH_SUCCESS = "Successfully fetched the recipe(s) !!";
  private static final String NO_RECIPES_FOUND = "No recipes found!";

  @Autowired private GrubBankCRUDService grubBankCRUDService;

  /** @param recipe the new recipe that needs to be added */
  @PostMapping("/addRecipe")
  public ResponseEntity<GrubResponseBody<? extends Streamable>> addRecipe(
      @RequestBody Recipe recipe) {
    try {
      return new ResponseEntity<>(
          new GrubResponseBody<>(RECIPE_ADD_SUCCESS, grubBankCRUDService.saveRecipe(recipe)),
          HttpStatus.OK);
    } catch (RecipeValidator.InvalidRecipeException invalidRecipeException) {
      return new ResponseEntity<>(
          new GrubResponseBody<>(
              String.format(RECIPE_ADD_FAILED, invalidRecipeException.getMessage()),
              ErrorPayload.builder()
                  .detail(invalidRecipeException.getDetail())
                  .exception(invalidRecipeException)
                  .build()),
          HttpStatus.BAD_REQUEST);
    }
  }

  /** @return the list of all the recipes in the grubbank */
  @GetMapping("/getRecipeList")
  @ResponseBody
  ResponseEntity<GrubResponseBody<List<Recipe>>> getRecipeList() {
    List<Recipe> recipeList = grubBankCRUDService.getAllRecipes();
    return new ResponseEntity<>(
        new GrubResponseBody<>(
            recipeList.size() > 0 ? RECIPE_FETCH_SUCCESS : NO_RECIPES_FOUND, recipeList),
        HttpStatus.OK);
  }

  /**
   * @param recipeByName the name of the requested recipe
   * @return the list of recipes based on the requested recipe name
   */
  @GetMapping("/getRecipeByName/{recipeByName}")
  @ResponseBody
  ResponseEntity<GrubResponseBody<List<Recipe>>> getRecipeByName(
      @PathVariable String recipeByName) {
    List<Recipe> recipeList = grubBankCRUDService.getRecipeByName(recipeByName);
    return new ResponseEntity<>(
        new GrubResponseBody<>(
            recipeList.size() > 0 ? RECIPE_FETCH_SUCCESS : NO_RECIPES_FOUND, recipeList),
        HttpStatus.OK);
  }

  /**
   * @param recipeId the recipe id of the recipe that needs to be updated
   * @param recipe the updated recipe
   * @return the updated recipe
   */
  @PutMapping("/updateRecipeById/{recipeId}")
  @ResponseBody
  ResponseEntity<GrubResponseBody<Recipe>> updateRecipeById(
      @PathVariable int recipeId, @RequestBody Recipe recipe) {
    try {
      return new ResponseEntity<>(
          new GrubResponseBody<>(
              String.format("Successfully updated the recipe with recipe id : %s", recipeId),
              grubBankCRUDService.updateRecipe(recipe, recipeId)),
          HttpStatus.OK);
    } catch (RecipeNotFoundException
        | RecipeValidator.InvalidRecipeException
        | JsonProcessingException exception) {
      return new ResponseEntity<>(
          new GrubResponseBody<>(
              String.format(
                  "Failed to update the recipe with recipe id : %s, exception %s",
                  recipeId, exception.getMessage()),
              null),
          HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * @param recipeId the recipe with recipeId that needs to be deleted
   * @return the success message after deleting recipe based on the requested id
   */
  @DeleteMapping("/deleteRecipeById/{recipeId}")
  ResponseEntity<GrubResponseBody<Void>> deleteRecipeById(@PathVariable int recipeId) {
    try {
      grubBankCRUDService.deleteRecipeById(recipeId);
      return new ResponseEntity<>(
          new GrubResponseBody<>(
              String.format("Successfully deleted the recipe with recipeId!! : %s", recipeId),
              null),
          HttpStatus.OK);
    } catch (RecipeNotFoundException recipeNotFoundException) {
      return new ResponseEntity<>(
          new GrubResponseBody<>(
              String.format(
                  "Failed to delete the recipe with recipeId!! : %s, exception %s ",
                  recipeId, recipeNotFoundException.getMessage()),
              null),
          HttpStatus.OK);
    }
  }

  @PostMapping("/search")
  ResponseEntity<GrubResponseBody<Object>> searchByCriteria(
      @RequestBody RecipeSearchCriteria recipeSearchCriteria) {
    try {
      List<Recipe> recipeList = grubBankCRUDService.searchByCriteria(recipeSearchCriteria);
      return new ResponseEntity<>(
          new GrubResponseBody<>(
              recipeList.size() > 0 ? RECIPE_FETCH_SUCCESS : NO_RECIPES_FOUND, recipeList),
          HttpStatus.OK);
    } catch (
        RecipeSearchCriteriaValidator.InvalidRecipeSearchCriteriaException
            invalidRecipeSearchCriteriaException) {
      return new ResponseEntity<>(
          new GrubResponseBody<>(
              String.format(RECIPE_ADD_FAILED, invalidRecipeSearchCriteriaException.getMessage()),
              ErrorPayload.builder()
                  .detail(invalidRecipeSearchCriteriaException.getDetail())
                  .exception(invalidRecipeSearchCriteriaException)
                  .build()),
          HttpStatus.BAD_REQUEST);
    }
  }
}
