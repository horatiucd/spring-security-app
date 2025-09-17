package com.hcd.springsecurityapp.service;

import com.hcd.springsecurityapp.dto.WebSocketMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate template;

    public WebSocketService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void notifyUser(String user, String message) {
        template.convertAndSendToUser(user, "/topic/user-messages",
                new WebSocketMessage(message));
    }
}
