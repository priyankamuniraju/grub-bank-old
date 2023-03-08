package com.grubbank.repository;

import com.grubbank.apimodel.RecipeSearchCriteria;
import com.grubbank.entity.Ingredient;
import com.grubbank.entity.Recipe;
import com.grubbank.validator.RecipeSearchCriteriaValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class RecipeRepositoryImpl implements RecipeRepositoryCustom {

  @PersistenceContext EntityManager entityManager;

  @Autowired RecipeSearchCriteriaValidator recipeSearchCriteriaValidator;

  private void updateIncludeExcludePredicate(
      Root<Recipe> recipe,
      Set<Ingredient> ingredientSet,
      CriteriaQuery<Recipe> criteriaQuery,
      CriteriaBuilder criteriaBuilder,
      List<Predicate> predicateList,
      boolean include) {

    Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
    Root<Recipe> recipeRoot = subquery.from(Recipe.class);
    Join<Ingredient, Recipe> ingredientRecipeJoin = recipeRoot.join("ingredientSet");
    Predicate predicate;
    List<String> ingredientNames =
        ingredientSet.stream().map(Ingredient::getName).collect(Collectors.toList());
    if (include) {
      // Select the Recipe id where one of their ingredients matches
      predicate = ingredientRecipeJoin.get("name").in(ingredientNames);
    } else {
      // Select the Recipe id where one of their ingredients doesn't match
      predicate = ingredientRecipeJoin.get("name").in(ingredientNames).not();
    }
    subquery.select(recipeRoot.get("id")).where(predicate);
    predicateList.add(criteriaBuilder.in(recipe.get("id")).value(subquery));
  }

  @Override
  public List<Recipe> listAllRecipes(RecipeSearchCriteria recipeSearchCriteria)
      throws RecipeSearchCriteriaValidator.InvalidRecipeSearchCriteriaException {
    recipeSearchCriteriaValidator.validate(recipeSearchCriteria);
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Recipe> criteriaQuery = criteriaBuilder.createQuery(Recipe.class);
    Root<Recipe> recipe = criteriaQuery.from(Recipe.class);

    List<Predicate> predicateList = new ArrayList<>();
    // Predicate for number of servings
    int numberOfServings = recipeSearchCriteria.getNumberOfServings();
    if (numberOfServings > 0) {
      // Add the numberOfServings to the predicate only if the value is positive, we cannot have a
      // dish which doesn't serve anybody!
      predicateList.add(criteriaBuilder.equal(recipe.get("numberOfServings"), numberOfServings));
    }

    // predicate for recipeType
    if (recipeSearchCriteria.getRecipeTypeSet() != null) {
      CriteriaBuilder.In<Recipe.RecipeType> recipeTypePredicate =
          criteriaBuilder.in(recipe.get("recipeType"));
      recipeSearchCriteria.getRecipeTypeSet().forEach(recipeTypePredicate::value);
      predicateList.add(recipeTypePredicate);
    }

    // predicate for ingredient Include list
    if (recipeSearchCriteria.getIngredientIncludeSet() != null) {
      updateIncludeExcludePredicate(
          recipe,
          recipeSearchCriteria.getIngredientIncludeSet(),
          criteriaQuery,
          criteriaBuilder,
          predicateList,
          true);
    }

    // Predicate for ingredient Exclude list
    if (recipeSearchCriteria.getIngredientExcludeSet() != null) {
      updateIncludeExcludePredicate(
          recipe,
          recipeSearchCriteria.getIngredientExcludeSet(),
          criteriaQuery,
          criteriaBuilder,
          predicateList,
          false);
    }

    String instructionQuery = recipeSearchCriteria.getInstructionContains();
    if (StringUtils.hasLength(instructionQuery)) {
      predicateList.add(
          criteriaBuilder.like(recipe.get("instructions"), "%" + instructionQuery + "%"));
    }

    criteriaQuery.where(predicateList.toArray(new Predicate[0]));
    List<Recipe> recipeList = entityManager.createQuery(criteriaQuery).getResultList();
    return recipeList;
  }
}
