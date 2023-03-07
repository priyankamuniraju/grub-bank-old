package com.grubbank.apimodel;

import com.grubbank.entity.Ingredient;
import com.grubbank.entity.Recipe;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSearchCriteria {
  private int numberOfServings;
  private Set<Recipe.RecipeType> recipeTypeEnum;
  private Set<Ingredient> ingredientIncludeSet;
  private Set<Ingredient> ingredientExcludeSet;
  private String instructionContains;
}
