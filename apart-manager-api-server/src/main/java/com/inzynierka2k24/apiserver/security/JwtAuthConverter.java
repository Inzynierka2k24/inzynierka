package com.inzynierka2k24.apiserver.security;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private final static String RESOURCE_ACCESS = "resource_access";
  private final static String ROLES = "roles";


  private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

  @Value("${jwt.auth.converter.resource-id}")
  private String resourceId;
  @Override
  public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
    Collection<GrantedAuthority> authorities = Stream.concat(
        jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
        extractResourceRoles(jwt).stream()
    ).collect(Collectors.toSet());

    return new JwtAuthenticationToken(
        jwt,
        authorities
    );
  }
  @SuppressWarnings("unchecked")
  private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
    Map<String, Object> resourceAccess;
    Map<String, Object> resource;
    Collection<String> resourceRoles;

    resourceAccess = jwt.getClaim(RESOURCE_ACCESS);

    if (MapUtils.isEmpty(resourceAccess)) {
      return Set.of();
    }

    resource = (Map<String, Object>) resourceAccess.get(resourceId);

    if (MapUtils.isEmpty(resource)) {
      return Set.of();
    }

    resourceRoles = (Collection<String>) resource.get(ROLES);

    return resourceRoles
        .stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
        .collect(Collectors.toSet());
  }
}

