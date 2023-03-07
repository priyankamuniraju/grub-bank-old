package com.grubbank.validator;

import com.grubbank.apimodel.RecipeSearchCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@Component
public class RecipeSearchCriteriaValidator {

  public void validate(RecipeSearchCriteria recipeSearchCriteria)
      throws InvalidRecipeSearchCriteriaException {
    if (recipeSearchCriteria.getNumberOfServings() < 0) {
      throw new InvalidRecipeSearchCriteriaException(
          InvalidField.NUMBER_SERVING, "Number of servings should be greater than zero");
    }
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  public static class InvalidRecipeSearchCriteriaException extends Exception {

    private InvalidField invalidField;
    private String detail;

    public InvalidRecipeSearchCriteriaException(InvalidField invalidField, String message) {
      super(
          String.format(
              "Invalid ReciperSearchCriteria passed invalid field %s", invalidField.name()));
      this.invalidField = invalidField;
      this.detail = message;
    }
  }
}
