package com.ninjaone.serverapp.utilities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Utility class for handling JWT token.
 */
@Component
public class JwtTokenUtil implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  public static final long JWT_TOKEN_VALIDITY = 60 * 60;

  @Value("${jwt.secret}")
  private String secret;

  /**
   * Retrieves the username from the token
   *
   * @param token Represents the token containing the authorization information.
   * @return The username from the token.
   */
  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  /**
   * Retrieves the expiration from the token
   *
   * @param token Represents the token containing the authorization information.
   * @return The expiration from the token.
   */
  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  /**
   * Retrieves the claim information from the token
   *
   * @param token          Represents the token containing the authorization information.
   * @param claimsResolver Represents the claims resolver passed with a specified type.
   * @param <T>            The type of the object provided within the claims resolver.
   * @return The claim as an object from the token.
   */
  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Retrieves all claims from the token
   *
   * @param token Represents the token containing the authorization information.
   * @return The claim attached within the token provided.
   */
  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  /**
   * Checks whether the token provided is expired or not.
   *
   * @param token Represents the token containing the authorization information.
   * @return A boolean value denoting whether the token is expired or not.
   */
  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  /**
   * Creates a token using the provided UserDetails object.
   *
   * @param userDetails Represents the user information for generating a token.
   * @return A token generated using the user details provided.
   */
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return doGenerateToken(claims, userDetails.getUsername());
  }

  /**
   * Creates the token by defining the claims, then signing using the secret key and HS512
   * algorithm. Also compacts the token to a URL safe string.
   *
   * @param claims  Represents the claims to be provided for generating a token.
   * @param subject Represents the subject of the token.
   * @return A token value from the data provided.
   */
  private String doGenerateToken(Map<String, Object> claims, String subject) {

    return Jwts.builder().setClaims(claims).setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
        .signWith(SignatureAlgorithm.HS512, secret).compact();
  }

  /**
   * Validates the token provided using the user details attached to the token.
   *
   * @param token       Represents the token containing the authorization information.
   * @param userDetails Represents the user details.
   * @return A boolean value whether the token is valid or not.
   */
  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}