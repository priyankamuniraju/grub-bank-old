package com.grubbank.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grubbank.apimodel.Streamable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorPayload implements Streamable {
  private String detail;

  @JsonIgnore private Exception exception;
}
