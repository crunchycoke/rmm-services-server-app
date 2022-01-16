package com.ninjaone.serverapp.controllers;

import com.ninjaone.serverapp.models.Customer;
import com.ninjaone.serverapp.models.JwtRequest;
import com.ninjaone.serverapp.models.JwtResponse;
import com.ninjaone.serverapp.services.JwtUserDetailsService;
import com.ninjaone.serverapp.utilities.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used for JWT authentication and user registration
 */
@RestController
@CrossOrigin
public class JwtAuthenticationController {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationController.class);

  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;
  private final JwtUserDetailsService userDetailsService;

  /**
   * Constructs the JWT authentication controller class.
   *
   * @param authenticationManager Authentication manager loaded through dependency injection.
   * @param jwtTokenUtil          JWT token utility class loaded through dependency injection.
   * @param userDetailsService    User detail service loaded through dependency injection.
   */
  public JwtAuthenticationController(AuthenticationManager authenticationManager,
      JwtTokenUtil jwtTokenUtil,
      JwtUserDetailsService userDetailsService) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenUtil = jwtTokenUtil;
    this.userDetailsService = userDetailsService;
  }

  /**
   * Creates an authentication token using the authentication request object provided.
   *
   * @param authenticationRequest Represents the authentication request object.
   * @return A token value.
   * @throws Exception May be thrown if there are issues during the authentication process.
   */
  @PostMapping("/authenticate")
  public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
      throws Exception {
    log.info("Attempting to authenticate request");

    authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

    final UserDetails userDetails = userDetailsService
        .loadUserByUsername(authenticationRequest.getUsername());

    final String token = jwtTokenUtil.generateToken(userDetails);

    return ResponseEntity.ok(new JwtResponse(token));
  }

  /**
   * Saves a customer within the database as a registration entry.
   *
   * @param customer Represents the customer to be saved within the database.
   * @return A status representing the state of the respository save.
   */
  @PostMapping("/register")
  public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) {
    log.info("Attempting to register customer " + customer);

    return ResponseEntity.ok(userDetailsService.save(customer));
  }

  /**
   * Authenticates the provided username and password against the token provided.
   *
   * @param username Represents the username provided.
   * @param password Represents the password provided.
   * @throws Exception May be thrown if there are issues during the authentication process.
   */
  private void authenticate(String username, String password) throws Exception {
    log.info("Attempting to authenticate customer credentials");

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new Exception("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new Exception("INVALID_CREDENTIALS", e);
    }
  }
}
