package com.jjginga.AuthServiceApplication.helper;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.scheduler.Scheduler;

import java.util.Collection;

import static java.util.Collections.emptyMap;
import static reactor.core.scheduler.Schedulers.newBoundedElastic;

public final class AuthenticationUtils {

    public static final int JUST_AUTH_THREAD_POOL_SIZE = 50;
    public static final Scheduler AUTH_REQUEST_THREAD_POOL = newBoundedElastic(JUST_AUTH_THREAD_POOL_SIZE, 5000, "auth-worker");

    public static Authentication toAuthentication(UserDetails userDetails) {
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return userDetails.getAuthorities();
            }

            @Override
            public Object getCredentials() {
                return "";
            }

            @Override
            public Object getDetails() {
                return emptyMap();
            }

            @Override
            public Object getPrincipal() {
                return userDetails;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) {
            }

            @Override
            public String getName() {
                return userDetails.getUsername();
            }
        };
    }

}
