package com.hcd.springsecurityapp.listener;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

public class CustomHttpSessionListener implements HttpSessionListener {

    private static final Logger log = LoggerFactory.getLogger(CustomHttpSessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        log.info("Session (ID: {}) created.", event.getSession().getId());

        // At this point, the user has not been authenticated yet
        // Authentication happens after the session creation
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        String sessionId = event.getSession().getId();

        SecurityContext securityContext = (SecurityContext) event.getSession()
                .getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        if (securityContext != null &&
                securityContext.getAuthentication() != null) {
            Authentication auth = securityContext.getAuthentication();
            if (auth.isAuthenticated() &&
                    !"anonymousUser".equals(auth.getName())) {
                log.info("Authenticated user is: {}.", auth.getName());
            }
        }

        log.info("Session (ID: {}) destroyed.", sessionId);
    }
}
