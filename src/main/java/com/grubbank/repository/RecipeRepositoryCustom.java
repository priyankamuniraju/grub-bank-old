package com.grubbank.repository;

import com.grubbank.apimodel.RecipeSearchCriteria;
import com.grubbank.entity.Recipe;
import com.grubbank.validator.RecipeSearchCriteriaValidator;
import java.util.List;

public interface RecipeRepositoryCustom {
  List<Recipe> listAllRecipes(RecipeSearchCriteria recipeSearchCriteria)
      throws RecipeSearchCriteriaValidator.InvalidRecipeSearchCriteriaException;
}
