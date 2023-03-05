package com.grubbank.service;

import com.grubbank.entity.Recipe;
import com.grubbank.repository.GrubBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrubBankCRUDServiceImpl implements GrubBankCRUDService {

    @Autowired
    private GrubBankRepository grubBankRepository;

    /**
     * @param recipe the recipe that needs to be saved in the db
     */
    @Override
    public void saveRecipe(Recipe recipe) {
        grubBankRepository.save(recipe);
    }

    /**
     * @return the list of all the recipes in grubbank
     */

    @Override
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipeList = (List<Recipe>) grubBankRepository.findAll();
        return recipeList;
    }

    /**
     * @param recipeName the name of the requested recipe
     * @return
     */
    @Override
    public List<Recipe> getRecipeByName(String recipeName) {
        return grubBankRepository.searchRecipesByName(recipeName);
    }

    /**
     * @param recipe   the recipe to be updated
     * @param recipeId the id of the recipe to be updated
     * @return the updated recipe
     */
    @Override
    public Recipe updateRecipe(Recipe recipe, int recipeId) {
        Optional<Recipe> recipeInDBMaybe = grubBankRepository.findById(recipeId);
        if (!recipeInDBMaybe.isPresent()) {
            //May be you should create one or send an error response back

        } else if (recipe != null) {
            Recipe recipeInDB = recipeInDBMaybe.get();
            if (recipe.getRecipeType() != null) {
                recipeInDB.setRecipeType(recipe.getRecipeType());
            }
            if (recipe.getInstructions() != null && !recipe.getInstructions().isEmpty()) {
                recipeInDB.setInstructions(recipe.getInstructions());
            }
            if (!recipe.getName().isEmpty()) {
                recipeInDB.setName(recipe.getName());
            }
            if (recipe.getIngredientSet() != null && !recipe.getIngredientSet().isEmpty()) {
                recipeInDB.setIngredientSet(recipe.getIngredientSet());
            }

            recipeInDB.setNumberOfServings(recipe.getNumberOfServings());

            if (recipe.getPreparationTime() != null) {
                recipeInDB.setPreparationTime(recipe.getPreparationTime());
            }
            if (recipe.getTotalTime() != null) {
                recipeInDB.setTotalTime(recipe.getTotalTime());
            }
            return grubBankRepository.save(recipeInDB);
        }
        // may be you should return an error response
        return null;
    }

    /**
     * @param recipeId the id of the recipe to be deleted
     */
    @Override
    public void deleteRecipeById(int recipeId) {
        grubBankRepository.deleteById(recipeId);
    }

    /**
     * @param recipeSearchCriteria the object which holds various search criteria
     * @return
     */
//    @Override
//    public List<Recipe> getRecipeBasedOnCriteria(RecipeSearchCriteria recipeSearchCriteria) {
//        return grubBankRepository.searchRecipeBasedOnCriteria(recipeSearchCriteria);
//    }


}
