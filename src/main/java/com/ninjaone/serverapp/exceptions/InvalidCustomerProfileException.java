package com.ninjaone.serverapp.exceptions;

/**
 * Thrown when the customer profile contains invalid information
 */
public class InvalidCustomerProfileException extends RuntimeException {

  public InvalidCustomerProfileException() {
    super("Customer contains invalid information.");
  }
}
