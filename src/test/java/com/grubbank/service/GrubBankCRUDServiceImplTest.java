package com.grubbank.service;

import com.grubbank.entity.Recipe;
import com.grubbank.model.TestInputGenerator;
import com.grubbank.repository.GrubBankRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;


@RunWith(MockitoJUnitRunner.class)
public class GrubBankCRUDServiceImplTest {
    @Mock
    GrubBankRepository grubBankRepository;

    @Mock
    RecipeValidator recipeValidator;

    @InjectMocks GrubBankCRUDServiceImpl grubBankCRUDService;

    @Test
    @DisplayName("When a valid recipe is added")
    public void addRecipeValidCaseTest() throws RecipeValidator.InvalidRecipeException {
    Recipe recipe = TestInputGenerator.createValidRecipe();
    when(grubBankRepository.save(recipe)).thenReturn(recipe);
    assertEquals("Kheer",recipe.getName());
    }
}
