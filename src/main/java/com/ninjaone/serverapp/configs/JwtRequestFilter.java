package com.ninjaone.serverapp.configs;

import com.ninjaone.serverapp.services.JwtUserDetailsService;
import com.ninjaone.serverapp.utilities.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter class for handling JWT tokens during transport.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  private final JwtUserDetailsService jwtUserDetailsService;
  private final JwtTokenUtil jwtTokenUtil;

  /**
   * Request filter object with provided objects.
   *
   * @param jwtUserDetailsService Represents the user detail service for token authentication and
   *                              handling.
   * @param jwtTokenUtil          Represents the token utility class passed in through dependency
   *                              injection.
   */
  public JwtRequestFilter(JwtUserDetailsService jwtUserDetailsService,
      JwtTokenUtil jwtTokenUtil) {
    this.jwtUserDetailsService = jwtUserDetailsService;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {

    final String requestTokenHeader = request.getHeader("Authorization");

    String username = null;
    String jwtToken = null;

    // Remove the bearer section from the provided token by performing
    // substring on seventh character.
    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
      jwtToken = requestTokenHeader.substring(7);

      try {
        username = jwtTokenUtil.getUsernameFromToken(jwtToken);
      } catch (IllegalArgumentException e) {
        System.out.println("Unable to get JWT Token");
      } catch (ExpiredJwtException e) {
        System.out.println("JWT Token has expired");
      }
    } else {
      logger.warn("JWT Token does not begin with Bearer String: " + request.getRequestURI());
    }

    // Check token once username and authentication provided is valid.
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

      // If token is valid, configure Spring Security to manually set authentication
      if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());

        usernamePasswordAuthenticationToken
            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Specify that user is authenticated through the security context
        // holder once everything is successful above.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }
    }

    chain.doFilter(request, response);
  }
}
