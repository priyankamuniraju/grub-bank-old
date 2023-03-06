package com.grubbank.controller;

import com.grubbank.entity.Recipe;
import com.grubbank.exception.MandatoryFieldsMissingException;
import com.grubbank.exception.RecipeNotFoundException;
import com.grubbank.response.ResponseHandler;
import com.grubbank.service.GrubBankCRUDService;
import com.grubbank.service.RecipeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/grubbank")
public class GrubBankCRUDController {

    @Autowired
    private GrubBankCRUDService grubBankCRUDService;

    /**
     * @param recipe the new recipe that needs to be added
     */
    @PostMapping("/addRecipe")
    public ResponseEntity<Object> addRecipe(@RequestBody Recipe recipe) throws RecipeValidator.InvalidRecipeException {
        try{
            Recipe savedRecipe = grubBankCRUDService.saveRecipe(recipe);
            return ResponseHandler.generateResponse("Successfully saved the recipe !!",HttpStatus.OK,recipe);
        }catch(RecipeValidator.InvalidRecipeException invalidRecipeException){
            return ResponseHandler.generateResponse("Failed to save the recipe !!",
                    HttpStatus.CONFLICT,invalidRecipeException.getMessage());
        }

    }

    /**
     * @return the list of all the recipes in the grubbank
     */
    @GetMapping("/getRecipeList")
    @ResponseBody
    ResponseEntity<Object> getRecipeList() {
        try{
            List<Recipe> recipeList = grubBankCRUDService.getAllRecipes();
            return ResponseHandler.generateResponse("Successfully fetched the recipe(s) !!",HttpStatus.OK,recipeList);
        }catch(RecipeNotFoundException recipeNotFoundException){
            return ResponseHandler.generateResponse("Failed to fetch the recipe(s) !!",HttpStatus.CONFLICT,recipeNotFoundException.getMessage());
        }
    }

    /**
     * @param recipeByName the name of the requested recipe
     * @return the list of recipes based on the requested recipe name
     */
    @GetMapping("/getRecipeByName/{recipeByName}")
    @ResponseBody
    ResponseEntity<Object> getRecipeByName(@PathVariable String recipeByName) {
        try{
            List<Recipe> recipeList = grubBankCRUDService.getRecipeByName(recipeByName);
            return ResponseHandler.generateResponse("Successfully fetched the recipe !!", HttpStatus.OK,recipeList);

        }catch (RecipeNotFoundException recipeNotFoundException){
            return ResponseHandler.generateResponse("Failed to fetch the recipe !!", HttpStatus.CONFLICT, recipeNotFoundException.getMessage());
        }
    }

    /**
     * @param recipeId the recipe id of the recipe that needs to be updated
     * @param recipe   the updated recipe
     * @return the updated recipe
     */
    @PutMapping("/updateRecipeById/{recipeId}")
    @ResponseBody
    ResponseEntity<Object> updateRecipeById(@PathVariable int recipeId, @RequestBody Recipe recipe) {
        try{
            Recipe updatedRecipe = grubBankCRUDService.updateRecipe(recipe, recipeId);
            return ResponseHandler.generateResponse("Successfully updated the recipe with recipe id : "+recipeId,HttpStatus.OK,updatedRecipe);
        }catch(RecipeNotFoundException | MandatoryFieldsMissingException | RecipeValidator.InvalidRecipeException recipeNotFoundException){
            return ResponseHandler.generateResponse("Failed to update the recipe with recipe id : "+recipeId,HttpStatus.CONFLICT,recipeNotFoundException.getMessage());
        }
    }

    /**
     * @param id the recipe id that needs to be deleted
     * @return the success message after deleting recipe based on the requested id
     */
    @DeleteMapping("/deleteRecipeById/{id}")
    ResponseEntity<Object> deleteRecipeById(@PathVariable int id) {
        try{
            grubBankCRUDService.deleteRecipeById(id);
            return ResponseHandler.generateResponse("Successfully deleted the recipe!!",HttpStatus.OK);
        }catch(RecipeNotFoundException recipeNotFoundException){
            return ResponseHandler.generateResponse("Failed to delete the recipe!!",HttpStatus.CONFLICT,recipeNotFoundException.getMessage());
        }
    }

    /**
     * @param recipeSearchCriteria the object which holds various search criteria
     * @return the recipe list based on the requested search criteria
     */
//    @PostMapping("/getRecipes")
//    List<Recipe> getRecipeBasedOnCriteria(@RequestBody RecipeSearchCriteria recipeSearchCriteria) {
//        return grubBankCRUDService.getRecipeBasedOnCriteria(recipeSearchCriteria);
//    }
}




