package com.ninjaone.serverapp.data;

import com.ninjaone.serverapp.exceptions.InvalidCustomerProfileException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Transforms the exception thrown to a specific HTTP status and body for user handling.
 */
@ControllerAdvice
public class InvalidCustomerProfileAdvice {

  @ResponseBody
  @ExceptionHandler(InvalidCustomerProfileException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String invalidCustomerProfileHandler(InvalidCustomerProfileException ex) {
    return ex.getMessage();
  }
}
