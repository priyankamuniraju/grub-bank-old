package com.grubbank.controller;

import com.grubbank.entity.Recipe;
import com.grubbank.service.GrubBankCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
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
    Collection<Recipe> getRecipeList() {
        return grubBankCRUDService.getAllRecipes();
    }

    /**
     * @param recipeByName the name of the requested recipe
     * @return the list of recipes based on the requested recipe name
     */
    @GetMapping("/getRecipeByName/{recipeByName}")
    @ResponseBody
    List<Recipe> getRecipeById(@PathVariable String recipeByName) {
        return grubBankCRUDService.getRecipeByName(recipeByName);
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
    String deleteRecipeById(@PathVariable int id) {
        grubBankCRUDService.deleteRecipeById(id);
        return "Recipe with id : " + id + " deleted successfully";
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




