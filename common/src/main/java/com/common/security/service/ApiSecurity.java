package com.common.security.service;

import com.common.security.context.RequestContext;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ApiSecurity {
    @Autowired
    RequestContext requestContext;

    public boolean hasUserRole(){
        if(ObjectUtils.isNotEmpty(requestContext.getRoles()) && requestContext.getRoles().get(0).equalsIgnoreCase("ROLE_USER"))
            return true;
        else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"UNAUTHORIZED");

    }
}
