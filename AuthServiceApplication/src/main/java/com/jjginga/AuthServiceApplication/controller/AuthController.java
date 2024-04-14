package com.jjginga.AuthServiceApplication.controller;

import com.jjginga.AuthServiceApplication.entity.MyUser;
import com.jjginga.AuthServiceApplication.helper.JwtUtils;
import com.jjginga.AuthServiceApplication.model.JwtRequest;
import com.jjginga.AuthServiceApplication.model.JwtResponse;
import com.jjginga.AuthServiceApplication.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private CustomUserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(value ="/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) throws Exception{
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        } catch (AuthenticationException e){
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Authentication");
        }

        UserDetails userDetails = this.userService.loadUserByUsername(jwtRequest.getUsername());
        String token = this.jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @RequestMapping(value ="/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody MyUser user) throws Exception {

        return ResponseEntity.ok(this.userService.saveUser(user));
    }

}
