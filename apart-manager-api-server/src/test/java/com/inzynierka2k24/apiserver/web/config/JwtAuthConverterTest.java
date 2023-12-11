package com.inzynierka2k24.apiserver.web.config;

import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.Map;
import java.util.List;

class JwtAuthConverterTest {

  @Test
  void testConvert() {
    JwtAuthConverter converter = new JwtAuthConverter();
    converter.resourceId = "testResource";

    Map<String, Object> claims = Map.of("resource_access", Map.of("testResource", Map.of("roles", List.of("USER"))));
    Jwt jwt = mock(Jwt.class);
    when(jwt.getClaim("resource_access")).thenReturn(claims);
    when(jwt.getExpiresAt()).thenReturn(Instant.now());

    AbstractAuthenticationToken token = converter.convert(jwt);

    assertNotNull(token);
    assertFalse(token.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .anyMatch(authority -> authority.equals("ROLE_USER")));
  }

  @Test
  void testGetLoginFromJWT() {
    JwtAuthConverter converter = new JwtAuthConverter();

    // Creating a dummy JWT Token
    String header = Base64.getUrlEncoder().encodeToString("{\"alg\":\"none\"}".getBytes());
    String body = Base64.getUrlEncoder().encodeToString("{\"preferred_username\":\"testUser\"}".getBytes());
    String signature = "dummySignature"; // Signature part is not used in the method, so it can be anything
    String jwt = header + "." + body + "." + signature;

    String result = converter.getLoginFromJWT(jwt);

    assertEquals("testUser", result);
  }
}
