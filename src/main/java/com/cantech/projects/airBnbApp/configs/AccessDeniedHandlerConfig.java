package com.cantech.projects.airBnbApp.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class AccessDeniedHandlerConfig {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;


    @Bean
    public AccessDeniedHandler getAccessDeniedHandler(){
        return (request, response, accessDeniedException) ->
        {
            handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
        };
    }
}
