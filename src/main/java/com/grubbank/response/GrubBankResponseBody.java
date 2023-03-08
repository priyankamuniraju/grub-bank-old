package com.grubbank.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrubBankResponseBody<T> {
  private String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T payload;
}
