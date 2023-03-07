package com.grubbank.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import com.grubbank.entity.Recipe;
import com.grubbank.model.TestInputGenerator;
import com.grubbank.service.GrubBankCRUDService;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest
@ContextConfiguration(classes = GrubBankCRUDController.class)
public class GrubBankCRUDControllerTest {

  private static final Logger logger = LoggerFactory.getLogger(GrubBankCRUDControllerTest.class);

  @Autowired MockMvc mockMvc;

  @MockBean
  GrubBankCRUDService grubBankCRUDService;

  Recipe recipe = TestInputGenerator.createValidRecipe();
  String exampleJSON2 = "{\"payload\":[{\"id\": 1,\"numberOfServings\": 4,\"name\":\"Kheer\"}]}";

  @Test
  @DisplayName("Create recipe with invalid input and verify the exception")
  void testCreateRecipeInvalidInput() {}

  @Test
  @DisplayName("Add recipe - positive case")
  void testCreateRecipe() throws Exception {
    Mockito.when(grubBankCRUDService.saveRecipe(any())).thenReturn(recipe);
    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/grubbank/addRecipe").accept(MediaType.APPLICATION_JSON)
            .content(exampleJSON2).contentType(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    logger.info("response {}", result.getResponse());
    MockHttpServletResponse response = result.getResponse();
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  @DisplayName("Get all recipes - positive case")
  void testGetRecipeList() throws Exception {
    Mockito.when(grubBankCRUDService.getAllRecipes()).thenReturn(Arrays.asList(recipe));

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get("/grubbank/getRecipeList").accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    System.out.println(result.getResponse());
    String expected = "{\"payload\":[{\"id\": 1,\"numberOfServings\": 4,\"name\":\"Kheer\"}]}";
    JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
  }

  @Test
  @DisplayName("Get recipe by name - positive case")
  void testGetRecipeByName() throws Exception {
    Mockito.when(grubBankCRUDService.getRecipeByName(Mockito.anyString()))
        .thenReturn(Arrays.asList(recipe));

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get("/grubbank/getRecipeByName/kheer")
            .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    System.out.println(result.getResponse());
    String expected = "{\"payload\":[{\"id\": 1,\"numberOfServings\": 4,\"name\":\"Kheer\"}]}";
    JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
  }
}
