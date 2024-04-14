package com.jjginga.AuthServiceApplication.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/*
  To capture the login data provided by the user
 */
public class JwtRequest {
    private String username;
    private String password;
}
