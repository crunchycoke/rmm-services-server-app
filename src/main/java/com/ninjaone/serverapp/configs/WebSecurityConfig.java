package com.ninjaone.serverapp.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Web security configuration with JWT token handling.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final UserDetailsService jwtUserDetailsService;
  private final JwtRequestFilter jwtRequestFilter;

  /**
   * Constructor with dependency injection for web security configuration.
   *
   * @param jwtAuthenticationEntryPoint Represents the entry point for JWT token handling.
   * @param jwtUserDetailsService       Represents the service used for JWT authentication.
   * @param jwtRequestFilter            Represents the filter for handling JWT authentication.
   */
  public WebSecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
      UserDetailsService jwtUserDetailsService,
      JwtRequestFilter jwtRequestFilter) {
    this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    this.jwtUserDetailsService = jwtUserDetailsService;
    this.jwtRequestFilter = jwtRequestFilter;
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    // Autowired for AuthenticationManagerBuilder authentication
    // Defined password encoded is used.
    auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
  }

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * {@inheritDoc}
   */
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {

    httpSecurity.headers().frameOptions().disable().and()
        // Disable and ignore h2-console for database console management
        .csrf().ignoringAntMatchers("/h2-console/**").disable()
        // Skip authentication on specific endpoints.
        .authorizeRequests().antMatchers("/h2console/**", "/authenticate", "/register").permitAll()
        // Everything else requires authentication
        .anyRequest().authenticated().and()
        // No sessions and stateless created for managing user state for mockup purposes.
        .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // Add filter class for authentication handling.
    httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }
}