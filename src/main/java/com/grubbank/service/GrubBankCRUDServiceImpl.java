package com.grubbank.service;

import com.grubbank.entity.Recipe;
import com.grubbank.exception.RecipeNotFoundException;
import com.grubbank.repository.GrubBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrubBankCRUDServiceImpl implements GrubBankCRUDService {

    @Autowired
    private GrubBankRepository grubBankRepository;

    @Autowired
    RecipeValidator recipeValidator;

    /**
     *
     * @param recipe the recipe that needs to be saved in the db
     * @return
     * @throws RecipeValidator.InvalidRecipeException
     */
    @Override
    public Recipe saveRecipe(Recipe recipe) throws RecipeValidator.InvalidRecipeException {
        recipeValidator(recipe);
        return grubBankRepository.save(recipe);
    }

    /**
     *
     * @return the list of recipes from the grub bank db
     * @throws RecipeNotFoundException
     */

    @Override
    public List<Recipe> getAllRecipes() throws RecipeNotFoundException {
        List<Recipe> recipeList = (List<Recipe>) grubBankRepository.findAll();
        if(recipeList.isEmpty()){
           throw new RecipeNotFoundException("No recipes available in the grub bank");
        }
        return recipeList;
    }

    /**
     *
     * @param recipeName the name of the requested recipe
     * @return the requested recipe based on the recipeName
     * @throws RecipeNotFoundException
     */
    @Override
    public List<Recipe> getRecipeByName(String recipeName) throws RecipeNotFoundException {
        List<Recipe> recipeList = grubBankRepository.searchRecipesByName(recipeName.toLowerCase());
        if(recipeList.isEmpty()){
            throw new RecipeNotFoundException("No recipes found");
        }
        return recipeList;
    }

    /**
     *
     * @param recipe   the updated recipe
     * @param recipeId the recipe id of the recipe that needs to be updated
     * @return the updated recipe
     * @throws RecipeValidator.InvalidRecipeException
     */
    @Override
    public Recipe updateRecipe(Recipe recipe, int recipeId) throws RecipeValidator.InvalidRecipeException {
        Optional<Recipe> recipeInDBMaybe = grubBankRepository.findById(recipeId);
        if (recipeInDBMaybe.isEmpty() || recipe == null) {
            throw new RecipeNotFoundException("No recipe with recipe id : "+recipeId+" found in the grub bank.");
        } else{
            //check if any of the fields are null
            recipeValidator.validateRecipe(recipe);
            Recipe recipeInDB = recipeInDBMaybe.get();
            recipeInDB.setRecipeType(recipe.getRecipeType());
            if (!recipe.getInstructions().isEmpty()) {
                recipeInDB.setInstructions(recipe.getInstructions());
            }
            if (!recipe.getName().isEmpty()) {
                recipeInDB.setName(recipe.getName());
            }
//            if (recipe.getIngredientSet() != null && !recipe.getIngredientSet().isEmpty()) {
//                recipeInDB.setIngredientSet(recipe.getIngredientSet());
//            }

            //recipeInDB.setNutritionalValue(recipe.getNutritionalValue());

            recipeInDB.setNumberOfServings(recipe.getNumberOfServings());

            if (recipe.getPreparationTime() != null) {
                recipeInDB.setPreparationTime(recipe.getPreparationTime());
            }
            if (recipe.getTotalTime() != null) {
                recipeInDB.setTotalTime(recipe.getTotalTime());
            }
           //set nutritional value

            return grubBankRepository.save(recipeInDB);
        }
    }

    /**
     *
     * @param recipeId the recipe id that needs to be deleted
     */
    @Override
    public void deleteRecipeById(int recipeId) {
        Optional<Recipe> recipe = grubBankRepository.findById(recipeId);
        if(recipe.isEmpty()){
            throw new RecipeNotFoundException("No recipe found with recipe id : "+recipeId);
        }
        grubBankRepository.deleteById(recipeId);
    }

    public void recipeValidator(Recipe recipe) throws RecipeValidator.InvalidRecipeException {
        recipeValidator.validateRecipe(recipe);
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
