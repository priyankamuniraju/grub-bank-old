package com.grubbank.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grubbank.apimodel.RecipeSearchCriteria;
import com.grubbank.apimodel.Streamable;
import com.grubbank.entity.Recipe;
import com.grubbank.exception.RecipeNotFoundException;
import com.grubbank.response.ErrorPayload;
import com.grubbank.response.GrubBankResponseBody;
import com.grubbank.service.GrubBankCRUDService;
import com.grubbank.validator.RecipeSearchCriteriaValidator;
import com.grubbank.validator.RecipeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grubbank")
public class GrubBankCRUDController {

    public static final String RECIPE_ADD_SUCCESS = "Successfully saved the recipe !!";
    public static final String RECIPE_ADD_FAILED = "Failed to save the recipe %s";
    public static final String RECIPE_FETCH_SUCCESS = "Successfully fetched the recipe(s) !!";
    public static final String NO_RECIPES_FOUND = "No recipes found!";

    public static final String NO_RECIPES_FOUND_WITH_MESSAGE = "No recipes found! %s";

    private static final Logger logger = LoggerFactory.getLogger(GrubBankCRUDController.class);

    @Autowired
    private GrubBankCRUDService grubBankCRUDService;

    /**
     * @param recipe the new recipe that needs to be added
     */
    @PostMapping("/addRecipe")
    public ResponseEntity<GrubBankResponseBody<? extends Streamable>> addRecipe(
            @RequestBody Recipe recipe) {
        try {
            return new ResponseEntity<>(
                    new GrubBankResponseBody<>(RECIPE_ADD_SUCCESS, grubBankCRUDService.saveRecipe(recipe)),
                    HttpStatus.OK);
        } catch (RecipeValidator.InvalidRecipeException invalidRecipeException) {
            logger.error(RECIPE_ADD_FAILED);
            return new ResponseEntity<>(
                    new GrubBankResponseBody<>(
                            String.format(RECIPE_ADD_FAILED, invalidRecipeException.getMessage()),
                            ErrorPayload.builder()
                                    .detail(invalidRecipeException.getDetail())
                                    .exception(invalidRecipeException)
                                    .build()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @return the list of all the recipes in the grubbank
     */
    @GetMapping("/getRecipeList")
    @ResponseBody
    ResponseEntity<GrubBankResponseBody<List<Recipe>>> getRecipeList() {
        List<Recipe> recipeList = grubBankCRUDService.getAllRecipes();
        return new ResponseEntity<>(
                new GrubBankResponseBody<>(
                        recipeList.size() > 0 ? RECIPE_FETCH_SUCCESS : NO_RECIPES_FOUND, recipeList),
                HttpStatus.OK);
    }

    /**
     * @param recipeByName the name of the requested recipe
     * @return the list of recipes based on the requested recipe name
     */
    @GetMapping("/getRecipeByName/{recipeByName}")
    @ResponseBody
    ResponseEntity<GrubBankResponseBody<List<Recipe>>> getRecipeByName(
            @PathVariable String recipeByName) {
        List<Recipe> recipeList = grubBankCRUDService.getRecipeByName(recipeByName);
        return new ResponseEntity<>(
                new GrubBankResponseBody<>(
                        recipeList.size() > 0 ? RECIPE_FETCH_SUCCESS : NO_RECIPES_FOUND, recipeList),
                HttpStatus.OK);
    }

    /**
     * @param recipeId the recipe id of the recipe that needs to be updated
     * @param recipe   the updated recipe
     * @return the updated recipe
     */
    @PostMapping("/updateRecipeById/{recipeId}")
    @ResponseBody
    ResponseEntity<GrubBankResponseBody<? extends Streamable>> updateRecipeById(
            @PathVariable int recipeId, @RequestBody Recipe recipe) {
        try {
            return new ResponseEntity<>(
                    new GrubBankResponseBody<>(
                            String.format("Successfully updated the recipe with recipe id : %s", recipeId),
                            grubBankCRUDService.updateRecipe(recipe, recipeId)),
                    HttpStatus.OK);
        } catch (RecipeNotFoundException | JsonProcessingException exception) {
            logger.error(String.format(
                    "Failed to update the recipe with recipe id : %s, exception %s",
                    recipeId, exception.getMessage()));
            return new ResponseEntity<>(
                    new GrubBankResponseBody<>(
                            String.format(
                                    "Failed to update the recipe with recipe id : %s, exception %s",
                                    recipeId, exception.getMessage()),
                            ErrorPayload.builder().detail(exception.getMessage()).exception(exception).build()),
                    HttpStatus.BAD_REQUEST);
        } catch (RecipeValidator.InvalidRecipeException invalidRecipeException) {
            logger.error(String.format(
                    "Failed to update the recipe with recipe id : %s, exception %s",
                    recipeId, invalidRecipeException.getMessage()));
            return new ResponseEntity<>(
                    new GrubBankResponseBody<>(
                            String.format(
                                    "Failed to update the recipe with recipe id : %s, exception %s",
                                    recipeId, invalidRecipeException.getMessage()),
                            ErrorPayload.builder()
                                    .detail(invalidRecipeException.getDetail())
                                    .exception(invalidRecipeException)
                                    .build()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param recipeId the recipe with recipeId that needs to be deleted
     * @return the success message after deleting recipe based on the requested id
     */
    @DeleteMapping("/deleteRecipeById/{recipeId}")
    ResponseEntity<GrubBankResponseBody<Void>> deleteRecipeById(@PathVariable int recipeId) {
        try {
            grubBankCRUDService.deleteRecipeById(recipeId);
            return new ResponseEntity<>(
                    new GrubBankResponseBody<>(
                            String.format("Successfully deleted the recipe with recipeId!! : %s", recipeId),
                            null),
                    HttpStatus.OK);
        } catch (RecipeNotFoundException recipeNotFoundException) {
            logger.error(String.format(
                    "Failed to delete the recipe with recipeId!! : %s, exception %s ",
                    recipeId, recipeNotFoundException.getMessage()));
            return new ResponseEntity<>(
                    new GrubBankResponseBody<>(
                            String.format(
                                    "Failed to delete the recipe with recipeId!! : %s, exception %s ",
                                    recipeId, recipeNotFoundException.getMessage()),
                            null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param recipeSearchCriteria the object which specifies the search criteria for searching recipes
     * @return
     */
    @PostMapping("/search")
    ResponseEntity<GrubBankResponseBody<Object>> searchByCriteria(
            @RequestBody RecipeSearchCriteria recipeSearchCriteria) {
        try {
            List<Recipe> recipeList = grubBankCRUDService.searchByCriteria(recipeSearchCriteria);
            return new ResponseEntity<>(
                    new GrubBankResponseBody<>(
                            recipeList.size() > 0 ? RECIPE_FETCH_SUCCESS : NO_RECIPES_FOUND, recipeList),
                    HttpStatus.OK);
        } catch (
                RecipeSearchCriteriaValidator.InvalidRecipeSearchCriteriaException
                        invalidRecipeSearchCriteriaException) {
            logger.error( String.format(NO_RECIPES_FOUND_WITH_MESSAGE, invalidRecipeSearchCriteriaException.getMessage()));
            return new ResponseEntity<>(
                    new GrubBankResponseBody<>(
                            String.format(NO_RECIPES_FOUND_WITH_MESSAGE, invalidRecipeSearchCriteriaException.getMessage()),
                            ErrorPayload.builder()
                                    .detail(invalidRecipeSearchCriteriaException.getDetail())
                                    .exception(invalidRecipeSearchCriteriaException)
                                    .build()),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
