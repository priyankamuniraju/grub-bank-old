package com.grubbank.controller;

import com.grubbank.entity.Recipe;
import com.grubbank.exception.RecipeNotFoundException;
import com.grubbank.response.ResponseHandler;
import com.grubbank.service.GrubBankCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/grubbank")
public class GrubBankCRUDController {

    @Autowired
    private GrubBankCRUDService grubBankCRUDService;

    /**
     * @param recipe the new recipe that needs to be added
     */
    @PostMapping(path = "/addRecipe")
    public Recipe addRecipe(@RequestBody Recipe recipe) {
        return grubBankCRUDService.saveRecipe(recipe);
    }

    /**
     * @return the list of all the recipes in the grubbank
     */
    @GetMapping(path = "/getRecipeList")
    @ResponseBody
    ResponseEntity<Object> getRecipeList() {
        try{
            List<Recipe> recipeList = grubBankCRUDService.getAllRecipes();
            return ResponseHandler.generateResponse("Successfully fetched the recipes !!",HttpStatus.OK,recipeList);
        }catch(RecipeNotFoundException recipeNotFoundException){
            return ResponseHandler.generateResponse("Failed to fetch the recipes !!",HttpStatus.OK,null);
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
            return ResponseHandler.generateResponse("Failed to fetch the recipe !!", HttpStatus.CONFLICT,null);
        }
    }

    /**
     * @param recipeId the recipe id of the recipe that needs to be updated
     * @param recipe   the updated recipe
     * @return the updated recipe
     */
    @PutMapping("/updateRecipeById/{recipeId}")
    @ResponseBody
    Recipe updateRecipeById(@RequestBody int recipeId, Recipe recipe) {
        return grubBankCRUDService.updateRecipe(recipe, recipeId);
    }

    /**
     * @param id the recipe id that needs to be deleted
     * @return the success message after deleting recipe based on the requested id
     */
    @DeleteMapping(path = "/deleteRecipeById/{id}")
    ResponseEntity<Object> deleteRecipeById(@PathVariable int id) {
        try{
            grubBankCRUDService.deleteRecipeById(id);
            return ResponseHandler.generateResponse("Successfully deleted the recipe !!",HttpStatus.OK,null);
        }catch(RecipeNotFoundException recipeNotFoundException){
            return ResponseHandler.generateResponse("Failed to delete the recipe !!",HttpStatus.CONFLICT,null);
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




