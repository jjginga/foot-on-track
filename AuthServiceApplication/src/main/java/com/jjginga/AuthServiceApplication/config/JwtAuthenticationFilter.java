package com.jjginga.AuthServiceApplication.config;

import com.jjginga.AuthServiceApplication.helper.JwtUtils;
import com.jjginga.AuthServiceApplication.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserService customUserService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String reqTokenHeader = request.getHeaders().getFirst("Authorization");
        String username = null;

        if (reqTokenHeader != null && reqTokenHeader.startsWith("Bearer ")) {
            String tokenWithoutBearer = reqTokenHeader.substring(7);

            try {
                username = this.jwtUtils.getUsernameFromToken(tokenWithoutBearer);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (username != null) {
                return this.customUserService.findByUsername(username).flatMap(userDetails -> {
                    if (userDetails != null && ReactiveSecurityContextHolder.getContext().flatMap(ctx -> Mono.justOrEmpty(ctx.getAuthentication())).block() == null) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        //return ReactiveSecurityContextHolder.getContext().flatMap(ctx -> Mono.justOrEmpty(ctx.getAuthentication())).switchIfEmpty(chain.filter(exchange));
                        return Mono.just(authentication)
                                .flatMap(auth -> chain.filter(exchange));
                    } else {
                        return chain.filter(exchange);
                    }
                });
            }
        }
        return chain.filter(exchange);
    }
}



/**package com.jjginga.AuthServiceApplication.config;

import com.jjginga.AuthServiceApplication.helper.JwtUtils;
import com.jjginga.AuthServiceApplication.service.CustomUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserService customUserService;


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //get Header
        String reqTokenHeader = request.getHeader("Authorization");
        String username = null;

        if(reqTokenHeader != null && reqTokenHeader.startsWith("Bearer")){
            String tokenWithoutBearar = reqTokenHeader.substring(7);

            try {
                username = this.jwtUtils.getUsernameFromToken(tokenWithoutBearar);
            } catch (Exception e) {
                e.printStackTrace();
            }

            UserDetails userDetails = this.customUserService.loadUserByUsername(username);

            if(userDetails != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                System.err.println("Token not validated");
            }
        }

        filterChain.doFilter(request, response);

    }
}
**/