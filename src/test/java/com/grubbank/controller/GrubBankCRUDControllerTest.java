package com.grubbank.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GrubBankCRUDControllerTest {

    @InjectMocks
    GrubBankCRUDController grubBankCRUDController;

    @Test
    @DisplayName("Create recipe with valid input")
    void testCreateRecipe() {

    }

    @Test
    @DisplayName("Create recipe with invalid input and verify the exception")
    void testCreateRecipeInvalidInput() {

    }

    @Test
    @DisplayName("Get all recipes")
    void testGetAllRecipes() {

    }

    //and so on...
}
