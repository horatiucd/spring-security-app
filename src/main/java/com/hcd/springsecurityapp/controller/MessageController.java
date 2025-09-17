package com.hcd.springsecurityapp.controller;

import com.hcd.springsecurityapp.dto.WebSocketMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@Controller
public class MessageController {

    @MessageMapping("/user-messages")
    @SendToUser("/topic/user-messages")
    public WebSocketMessage handlePrivateMessage(WebSocketMessage message, Principal principal) {
        return new WebSocketMessage("To " + principal.getName() +
                ": Respond to - " + HtmlUtils.htmlEscape(message.content()));
    }
}
