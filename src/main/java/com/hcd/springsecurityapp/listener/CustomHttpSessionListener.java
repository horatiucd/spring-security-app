package com.hcd.springsecurityapp.listener;

import com.hcd.springsecurityapp.service.WebSocketService;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

@Component
public record CustomHttpSessionListener(WebSocketService webSocketService) implements HttpSessionListener {

    private static final Logger log = LoggerFactory.getLogger(CustomHttpSessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        log.info("Session (ID: {}) created.", event.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        SecurityContext securityContext = (SecurityContext) event.getSession()
                .getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        if (securityContext != null &&
                securityContext.getAuthentication() != null) {
            Authentication auth = securityContext.getAuthentication();
            String user = auth.getName();
            if (auth.isAuthenticated() &&
                    !"anonymousUser".equals(user)) {
                log.info("Notify {} the session expired.", user);

                webSocketService.notifyUser(user, "Session expired");
            }
        }
        log.info("Session (ID: {}) destroyed.", event.getSession().getId());
    }
}
