package com.StayAt.StayAt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.SessionCookieConfig;

@Configuration
public class CookieConfig {

    @Value("${StayAt.app.rootDomainUrl}")
    private String rootDomainUrl;

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return servletContext -> {
            SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
            sessionCookieConfig.setDomain(rootDomainUrl);
        };
    }
}
