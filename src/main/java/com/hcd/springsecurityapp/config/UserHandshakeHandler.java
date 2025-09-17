package com.hcd.springsecurityapp.config;

import com.sun.security.auth.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.WebUtils;

import java.security.Principal;
import java.util.Map;

public class UserHandshakeHandler extends DefaultHandshakeHandler {

    private static final Logger log = LoggerFactory.getLogger(UserHandshakeHandler.class);

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        SecurityContext securityContext = (SecurityContext) WebUtils.getSessionAttribute(servletRequest.getServletRequest(),
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);

        String user = "anonymousUser";
        if (securityContext != null &&
                securityContext.getAuthentication() != null) {
            user = securityContext.getAuthentication().getName();
            log.info("User connected via web socket: {}.", user);
        }
        return new UserPrincipal(user);
    }
}
