package com.grubbank.exception;

public class MandatoryFieldsMissingException extends Exception {
  public MandatoryFieldsMissingException(String message) {
    super(message);
  }
}
