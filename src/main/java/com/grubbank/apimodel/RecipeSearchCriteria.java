package com.grubbank.apimodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grubbank.entity.Ingredient;
import com.grubbank.entity.Recipe;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
/*
 The Search criteria for searching {@link Recipe} in the datastore
 Currently it supports search by doing an AND query with each of the supplied params
 If the param type is a list, such as {@link RecipeSearchCriteria#ingredientIncludeList},
 then an in/not in is applied based on the parameter.
*/
public class RecipeSearchCriteria {
  private int numberOfServings;
  private List<Recipe.RecipeType> recipeTypeList;
  private List<Ingredient> ingredientIncludeList;
  private List<Ingredient> ingredientExcludeList;
  private String instructionContains;
}
