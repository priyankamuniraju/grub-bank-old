package com.grubbank.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grubbank.GrubbankApplication;
import com.grubbank.controller.GrubBankCRUDController;
import com.grubbank.entity.Ingredient;
import com.grubbank.entity.NutritionalValue;
import com.grubbank.entity.Recipe;
import com.grubbank.model.TestInputGenerator;
import com.grubbank.repository.IngredientRepository;
import com.grubbank.repository.NutritionalValueRepository;
import com.grubbank.repository.RecipeRepository;
import com.grubbank.response.GrubResponseBody;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = GrubbankApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@AutoConfigureTestDatabase
public class GrubBankControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private RecipeRepository recipeRepository;
  @Autowired private IngredientRepository ingredientRepository;
  @Autowired NutritionalValueRepository nutritionalValueRepository;

  @Autowired ObjectMapper objectMapper;

  @Test
  public void addRecipeWithValidData_thenStatus200() throws Exception {
    Recipe input = TestInputGenerator.createValidRecipe();

    ResultActions resultActions =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/grubbank/addRecipe")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value(GrubBankCRUDController.RECIPE_ADD_SUCCESS));

    MvcResult result = resultActions.andReturn();
    String responseStr = result.getResponse().getContentAsString();

    TypeReference<GrubResponseBody<Recipe>> typeToken = new TypeReference<>() {};
    GrubResponseBody<Recipe> addedRecipe = objectMapper.readValue(responseStr, typeToken);

    Assertions.assertEquals(
        input.getNumberOfServings(), addedRecipe.getPayload().getNumberOfServings());
    Assertions.assertEquals(input.getName(), addedRecipe.getPayload().getName());

    // Verify whether the Recipe is stored in the datastore.
    List<Recipe> recipeList = (List<Recipe>) recipeRepository.findAll();
    Assertions.assertEquals(1, recipeList.size());

    Recipe recipeFromDB = recipeList.get(0);
    Assertions.assertEquals(input.getName(), recipeFromDB.getName());
    Assertions.assertEquals(input.getPreparationTime(), recipeFromDB.getPreparationTime());
    Assertions.assertEquals(input.getInstructions(), recipeFromDB.getInstructions());
    Assertions.assertEquals(input.getRecipeType(), recipeFromDB.getRecipeType());

    // Verify whether the ingredients are stored in the datastore
    List<Ingredient> ingredientList = (List<Ingredient>) ingredientRepository.findAll();
    Assertions.assertEquals(input.getIngredientSet().size(), ingredientList.size());
    IntStream.range(0, ingredientList.size())
        .forEach(
            i ->
                Assertions.assertEquals(
                    input.getIngredientSet().get(i).getName(), ingredientList.get(i).getName()));

    // Verify whether the NutritionalValue is stored in the datastore
    List<NutritionalValue> nutritionalValueList =
        (List<NutritionalValue>) nutritionalValueRepository.findAll();
    Assertions.assertEquals(1, nutritionalValueList.size());

    NutritionalValue nutritionalValueFromDB = nutritionalValueList.get(0);
    Assertions.assertEquals(
        input.getNutritionalValue().getCalories(), nutritionalValueFromDB.getCalories());
    Assertions.assertEquals(input.getNutritionalValue().getFat(), nutritionalValueFromDB.getFat());
    Assertions.assertEquals(
        input.getNutritionalValue().getCarbs(), nutritionalValueFromDB.getCarbs());
    Assertions.assertEquals(
        input.getNutritionalValue().getProteins(), nutritionalValueFromDB.getProteins());
  }
}
