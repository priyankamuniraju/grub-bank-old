package com.grubbank.integration;

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
import com.grubbank.response.GrubBankResponseBody;
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

import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = GrubbankApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@AutoConfigureTestDatabase
public class GrubBankControllerIntegrationTest {

    @Autowired
    NutritionalValueRepository nutritionalValueRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    private GrubBankResponseBody<Recipe> saveOrUpdateApiCall(
            String input, String apiPath, String responseMessage) throws Exception {
        ResultActions resultActions =
                mockMvc
                        .perform(
                                MockMvcRequestBuilders.post(apiPath)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(input))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.message").value(responseMessage));

        MvcResult result = resultActions.andReturn();
        String responseStr = result.getResponse().getContentAsString();

        TypeReference<GrubBankResponseBody<Recipe>> typeToken = new TypeReference<>() {
        };
        return objectMapper.readValue(responseStr, typeToken);
    }

    private GrubBankResponseBody<Recipe> deleteApiCall(String apiPath, String responseMessage) throws Exception {
        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.delete(apiPath))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(responseMessage));
      MvcResult result = resultActions.andReturn();
      String responseStr = result.getResponse().getContentAsString();

      TypeReference<GrubBankResponseBody<Recipe>> typeToken = new TypeReference<>() {
      };
      return objectMapper.readValue(responseStr, typeToken);
    }


    @Test
    public void addRecipeWithValidData_thenStatus200() throws Exception {
        Recipe input = TestInputGenerator.createValidRecipe();
        GrubBankResponseBody<Recipe> addedRecipe =
                saveOrUpdateApiCall(
                        objectMapper.writeValueAsString(input),
                        "/grubbank/addRecipe",
                        GrubBankCRUDController.RECIPE_ADD_SUCCESS);

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

    @Test
    public void addRecipeWithInvalidData_thenStatus400() throws Exception {
        Recipe input = TestInputGenerator.createValidRecipe();

        // set the invalid servings so that the request will fail
        input.setNumberOfServings(-2);
        mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/grubbank/addRecipe")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Failed to save the recipe Invalid input data for the field : NUMBER_SERVING"));
    }

    @Test
    public void updateRecipeValidData_thenStatus200() throws Exception {

        // First add the recipe
        Recipe input = TestInputGenerator.createValidRecipe();
        GrubBankResponseBody<Recipe> responseBody =
                saveOrUpdateApiCall(
                        objectMapper.writeValueAsString(input),
                        "/grubbank/addRecipe",
                        GrubBankCRUDController.RECIPE_ADD_SUCCESS);

        List<Ingredient> inputIngredientList = input.getIngredientSet();

        // assuming the add is successful, grab the id from the add and use it to update.
        Recipe recipeStored = responseBody.getPayload();
        int id = recipeStored.getId();

        // use this id to update the recipe
        // remove an ingredient, change number of servings
        int newNumberOfServings = recipeStored.getNumberOfServings() + 3;
        recipeStored.setNumberOfServings(newNumberOfServings);
        // remove the first ingredient
        recipeStored.getIngredientSet().remove(0);

        // call update api
        GrubBankResponseBody<Recipe> updateResponseBody =
                saveOrUpdateApiCall(
                        objectMapper.writeValueAsString(recipeStored),
                        "/grubbank/updateRecipeById/" + id,
                        "Successfully updated the recipe with recipe id : " + id);

        Recipe recipeUpdated = updateResponseBody.getPayload();
        List<Ingredient> updatedIngredientList = recipeUpdated.getIngredientSet();
        Assertions.assertEquals(id, recipeUpdated.getId());
        Assertions.assertEquals(newNumberOfServings, recipeUpdated.getNumberOfServings());
        int expectedIngredientSize = inputIngredientList.size() - 1;
        Assertions.assertEquals(expectedIngredientSize, updatedIngredientList.size());

        IntStream.range(0, expectedIngredientSize)
                .forEach(
                        i -> {
                            Assertions.assertEquals(
                  /*since we removed the first ingredient from original list,
                  we need to match the i + 1 th entry on original list to
                  the ith entry in the updated list*/
                                    inputIngredientList.get(i + 1).getName(), updatedIngredientList.get(i).getName());
                        });
    }

  @Test
  public void deleteRecipeWithValidRecipeID_thenStatus200() throws Exception{

    // First add the recipe
    Recipe input = TestInputGenerator.createValidRecipe();
    GrubBankResponseBody<Recipe> responseBody =
            saveOrUpdateApiCall(
                    objectMapper.writeValueAsString(input),
                    "/grubbank/addRecipe",
                    GrubBankCRUDController.RECIPE_ADD_SUCCESS);


    // assuming the add is successful, grab the id from the add and use it to delete.
    Recipe recipeStored = responseBody.getPayload();
    int id = recipeStored.getId();

    // call the delete api
    GrubBankResponseBody<Recipe> deleteResponseBody =
            deleteApiCall("/grubbank/deleteRecipeById/"+id,
                    "Successfully deleted the recipe with recipeId!! : "+id);
    Recipe deletedRecipe = deleteResponseBody.getPayload();
    String expectedMessage = deleteResponseBody.getMessage();

    Assertions.assertNull(deletedRecipe);
    Assertions.assertEquals("Successfully deleted the recipe with recipeId!! : "+id, expectedMessage);

    // verify that recipe with recipeId = id not present in the db anymore
    List<Recipe> recipeList = (List<Recipe>) recipeRepository.findAll();
    for (Recipe listRecipe :recipeList
         ) {
      Assertions.assertNotEquals(listRecipe.getId(), id);

      }
    }

    @Test
    public void deleteRecipeWithInvalidData_thenStatus400() throws Exception {

        // First add the recipe (first recipe added)
        Recipe input = TestInputGenerator.createValidRecipe();
        GrubBankResponseBody<Recipe> responseBody =
                saveOrUpdateApiCall(
                        objectMapper.writeValueAsString(input),
                        "/grubbank/addRecipe",
                        GrubBankCRUDController.RECIPE_ADD_SUCCESS);


       // but call delete with recipe id not present in db
        mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/grubbank/deleteRecipeById/1900"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Failed to delete the recipe with recipeId!! : 1900, exception No recipe found with recipe id : 1900 "));
    }
}

