package com.jjginga.AuthServiceApplication.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
/*
  To encapsulate the response that includes a JWT token
 */
public class JwtResponse {
    private String token;
    private String userId;
}
