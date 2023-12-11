package com.inzynierka2k24.apiserver.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KeycloakTokenResponse {

  @JsonProperty("access_token")
  private String accessToken;
}
