package com.grubbank.apimodel;

import com.grubbank.entity.Ingredient;
import com.grubbank.entity.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSearchCriteria {
    private int numberOfServings;
    private Recipe.RecipeType recipeTypeEnum;
    private Set<Ingredient> ingredientIncludeSet;
    private Set<Ingredient> ingredientExcludeSet;
    private String instructionContains;
}
