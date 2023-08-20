package com.inzynierka2k24.apiserver.web.config;

// @Configuration
// @EnableWebSecurity
public class WebSecurityConfig {

  //  @Bean
  //  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  //    http.authorizeHttpRequests(
  //            (requests) ->
  //                requests
  //                    .anyRequest()
  //                    .permitAll()
  //                    .requestMatchers("/product/*", "/category/*")
  //                    .hasRole("ADMIN")
  //                    .requestMatchers("/store/*", "/cart/*")
  //                    .hasAnyRole("USER", "ADMIN"))
  //        .formLogin();
  //
  //    return http.build();
  //  }
  //
  //  @Bean
  //  public PasswordEncoder getPasswordEncoder() {
  //    return NoOpPasswordEncoder.getInstance();
  //  }
}
