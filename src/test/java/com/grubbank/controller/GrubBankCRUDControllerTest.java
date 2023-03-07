package com.grubbank.controller;

import com.grubbank.entity.Recipe;
import com.grubbank.model.TestInputGenerator;
import com.grubbank.service.GrubBankCRUDService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GrubBankCRUDController.class)
public class GrubBankCRUDControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  GrubBankCRUDService grubBankCRUDService;

  Recipe recipe = TestInputGenerator.createValidRecipe();
  String exampleJSON2 = "{\"payload\":[{\"id\": 1,\"numberOfServings\": 4,\"name\":\"Kheer\"}]}";

  @Test
  @DisplayName("Create recipe with invalid input and verify the exception")
  void testCreateRecipeInvalidInput() {
  }

  @Test
  @DisplayName("Get all recipes - positive case")
  void testGetRecipeList() throws Exception {
    Mockito.when(grubBankCRUDService.getAllRecipes()).thenReturn(Arrays.asList(recipe));

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/grubbank/getRecipeList").accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    System.out.println(result.getResponse());
    String expected = "{\"payload\":[{\"id\": 1,\"numberOfServings\": 4,\"name\":\"Kheer\"}]}";
    JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
  }

  @Test
  @DisplayName("Get recipe by name - positive case")
  void testGetRecipeByName() throws Exception {
    Mockito.when(grubBankCRUDService.getRecipeByName(Mockito.anyString())).thenReturn(Arrays.asList(recipe));

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/grubbank/getRecipeByName/kheer").accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    System.out.println(result.getResponse());
    String expected = "{\"payload\":[{\"id\": 1,\"numberOfServings\": 4,\"name\":\"Kheer\"}]}";
    JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

  }

  @Test
  @DisplayName("Add recipe - positive case")
  void testCreateRecipe() throws Exception {
    Mockito.when(grubBankCRUDService.saveRecipe(any())).thenReturn(recipe);
    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/grubbank/addRecipe").accept(MediaType.APPLICATION_JSON)
            .content(exampleJSON2).contentType(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    System.out.println(result.getResponse());

    MockHttpServletResponse response = result.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());

//    assertEquals("http://localhost/grubbank/addRecipe",
//            response.getHeader(HttpHeaders.LOCATION));
  }
}