package com.common.security.interceptor;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.common.security.context.RequestContext;
import com.common.security.service.JwtTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    RequestContext requestContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //get Header
        String reqTokenHeader = request.getHeader("Authorization");
        if(reqTokenHeader != null){
            String jwt = reqTokenHeader.replaceAll("Bearer ", "");
            if (!jwtTokenService.validateToken(jwt)) {
                response.getWriter().write("UNAUTHORIZED");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().flush();
                return;
            }
            DecodedJWT decodedJWT = JWT.decode(jwt);
            requestContext.setRoles(List.of(new ObjectMapper().convertValue(decodedJWT.getClaim("roles").as(ArrayList.class).get(0), Map.class).get("authority").toString()));

        }
        filterChain.doFilter(request, response);
    }
}