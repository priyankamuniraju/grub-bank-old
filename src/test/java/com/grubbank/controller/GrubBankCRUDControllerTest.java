package com.grubbank.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grubbank.apimodel.RecipeSearchCriteria;
import com.grubbank.entity.Recipe;
import com.grubbank.model.TestInputGenerator;
import com.grubbank.response.GrubBankResponseBody;
import com.grubbank.service.GrubBankCRUDService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest
@ContextConfiguration(classes = GrubBankCRUDController.class)
public class GrubBankCRUDControllerTest {

  @Autowired MockMvc mockMvc;
  @MockBean GrubBankCRUDService grubBankCRUDService;
  @Autowired ObjectMapper objectMapper;

  Recipe recipe = TestInputGenerator.createValidRecipe();
  String exampleJSON = "{\"payload\":[{\"id\": 1,\"numberOfServings\": 3,\"name\":\"Kheer\"}]}";

  @Test
  @DisplayName("Create recipe with invalid input and verify the exception")
  void testCreateRecipeInvalidInput() {}

  @Test
  @DisplayName("Add recipe - positive case")
  void addRecipePositiveCase() throws Exception {
    when(grubBankCRUDService.saveRecipe(any())).thenReturn(recipe);
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post("/grubbank/addRecipe")
            .accept(MediaType.APPLICATION_JSON)
            .content(exampleJSON)
            .contentType(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

    String responseStr = result.getResponse().getContentAsString();
    TypeReference<GrubBankResponseBody<Recipe>> typeToken = new TypeReference<>() {};

    GrubBankResponseBody<Recipe> grubBankResponseBody =
        objectMapper.readValue(responseStr, typeToken);
    Assertions.assertEquals(
        GrubBankCRUDController.RECIPE_ADD_SUCCESS, grubBankResponseBody.getMessage());
    Assertions.assertEquals(recipe, grubBankResponseBody.getPayload());
  }

  @Test
  @DisplayName(("Get all recipes"))
  public void getRecipeListPositiveCase() throws Exception {

    when(grubBankCRUDService.getAllRecipes()).thenReturn(List.of(recipe));
    MvcResult result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/grubbank/getRecipeList")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    String responseStr = result.getResponse().getContentAsString();

    TypeReference<GrubBankResponseBody<List<Recipe>>> typeToken = new TypeReference<>() {};
    GrubBankResponseBody<List<Recipe>> listGrubBankResponseBody =
        objectMapper.readValue(responseStr, typeToken);

    Assertions.assertEquals(1, listGrubBankResponseBody.getPayload().size());
    Assertions.assertEquals(recipe, listGrubBankResponseBody.getPayload().get(0));
    verify(grubBankCRUDService, times(1)).getAllRecipes();
  }

  @Test
  @DisplayName("Get recipe by name - positive case")
  void getRecipeByNamePositiveCase() throws Exception {
    when(grubBankCRUDService.getRecipeByName(anyString())).thenReturn(List.of(recipe));

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get("/grubbank/getRecipeByName/kheer")
            .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    String responseStr = result.getResponse().getContentAsString();

    TypeReference<GrubBankResponseBody<List<Recipe>>> typeToken = new TypeReference<>() {};
    GrubBankResponseBody<List<Recipe>> listGrubBankResponseBody =
        objectMapper.readValue(responseStr, typeToken);

    Assertions.assertEquals(1, listGrubBankResponseBody.getPayload().size());
    Assertions.assertEquals(recipe, listGrubBankResponseBody.getPayload().get(0));
    verify(grubBankCRUDService, times(1)).getRecipeByName(anyString());
  }

  @Test
  @DisplayName("Delete Recipe by recipeId - positive case")
  void deleteRecipeByIdPositiveCase() throws Exception {
    MvcResult result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.delete("/grubbank/deleteRecipeById/" + recipe.getId())
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    String responseStr = result.getResponse().getContentAsString();

    TypeReference<GrubBankResponseBody<List<Recipe>>> typeToken = new TypeReference<>() {};
    GrubBankResponseBody<List<Recipe>> listGrubBankResponseBody =
        objectMapper.readValue(responseStr, typeToken);

    Assertions.assertEquals(200, result.getResponse().getStatus());
    verify(grubBankCRUDService, times(1)).deleteRecipeById(anyInt());
  }

  @Test
  @DisplayName("Update recipe by name - positive case")
  void updateRecipeByIdPositiveCase() throws Exception {
    recipe.setName("kheer1");
    int recipeId = recipe.getId();
    when(grubBankCRUDService.updateRecipe(recipe, recipe.getId())).thenReturn(recipe);
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post("/grubbank/updateRecipeById/0")
            .accept(MediaType.APPLICATION_JSON)
            .content(exampleJSON)
            .contentType(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    String responseStr = result.getResponse().getContentAsString();

    TypeReference<GrubBankResponseBody<Recipe>> typeToken = new TypeReference<>() {};
    GrubBankResponseBody<Recipe> grubBankResponseBody =
        objectMapper.readValue(responseStr, typeToken);
    Assertions.assertEquals(
        String.format("Successfully updated the recipe with recipe id : %s", recipeId),
        grubBankResponseBody.getMessage());
    verify(grubBankCRUDService, times(1)).updateRecipe(any(), anyInt());
  }

  @Test
  @DisplayName(("Search recipe based on certain criteria - positive case"))
  public void searchByCriteriaPositiveCase() throws Exception {

    RecipeSearchCriteria recipeSearchCriteria = new RecipeSearchCriteria();
    recipeSearchCriteria.setNumberOfServings(3);
    when(grubBankCRUDService.searchByCriteria(recipeSearchCriteria)).thenReturn(List.of(recipe));
    MvcResult result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/grubbank/search")
                    .accept(MediaType.APPLICATION_JSON)
                    .content(exampleJSON)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    String responseStr = result.getResponse().getContentAsString();

    TypeReference<GrubBankResponseBody<List<Recipe>>> typeToken = new TypeReference<>() {};
    GrubBankResponseBody<List<Recipe>> listGrubBankResponseBody =
        objectMapper.readValue(responseStr, typeToken);

    Assertions.assertEquals(0, listGrubBankResponseBody.getPayload().size());
    verify(grubBankCRUDService, times(1)).searchByCriteria(any());
  }
}
