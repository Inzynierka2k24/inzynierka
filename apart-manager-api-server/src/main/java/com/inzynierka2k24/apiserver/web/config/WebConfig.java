package com.inzynierka2k24.apiserver.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry corsRegistry) {
    corsRegistry
        .addMapping("/**")
        .allowedOrigins(
            "https://salmon-moss-003628103-34.westeurope.4.azurestaticapps.net/",
            "http://salmon-moss-003628103-34.westeurope.4.azurestaticapps.net/",
            "http://localhost:80",
            "http://localhost",
            "*.azurestaticapps.net")
        .allowedMethods("*")
        .maxAge(3600L)
        .allowedHeaders("*")
        .exposedHeaders("Authorization")
        .allowCredentials(true);
  }
}
