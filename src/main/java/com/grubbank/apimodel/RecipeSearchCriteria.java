package com.grubbank.apimodel;

import com.grubbank.entity.Ingredient;
import com.grubbank.entity.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

/*
 The Search criteria for searching {@link Recipe} in the datastore
 Currently it supports search by doing an AND query with each of the supplied params
 If the param type is a list/set, such as {@link RecipeSearchCriteria#ingredientIncludeSet},
 then an in/not in is applied based on the parameter.
*/
public class RecipeSearchCriteria {
  private int numberOfServings;
  private Set<Recipe.RecipeType> recipeTypeSet;
  private Set<Ingredient> ingredientIncludeSet;
  private Set<Ingredient> ingredientExcludeSet;
  private String instructionContains;
}
