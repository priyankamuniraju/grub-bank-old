package com.grubbank.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import com.grubbank.entity.Recipe;
import com.grubbank.model.TestInputGenerator;
import com.grubbank.repository.RecipeRepository;
import com.grubbank.validator.RecipeValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GrubBankCRUDServiceImplTest {
  @Mock RecipeRepository grubBankRepository;

  @Mock RecipeValidator recipeValidator;

  @InjectMocks GrubBankCRUDServiceImpl grubBankCRUDService;

  @Test
  @DisplayName("When a valid recipe is added")
  public void addRecipeValidCaseTest() throws RecipeValidator.InvalidRecipeException {
    Recipe recipe = TestInputGenerator.createValidRecipe();
    when(grubBankRepository.save(recipe)).thenReturn(recipe);
    assertEquals("Kheer", recipe.getName());
  }
}
