package com.modernfrontendshtmx.wsdemo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketHandler.class);
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    private final ObjectMapper objectMapper;
    private final SpringTemplateEngine templateEngine;

    public SocketHandler(ObjectMapper objectMapper,
                         SpringTemplateEngine templateEngine) { //<.>
        this.objectMapper = objectMapper;
        this.templateEngine = templateEngine;
    }

    @Override
    public void handleTextMessage(WebSocketSession session,
                                  TextMessage message)
            throws IOException {

        Map<String, Object> value = objectMapper.readValue(message.getPayload(), new TypeReference<>() {
        });
        String userMessage = (String) value.get("chatMessage");
        sendMessageToWebSocketsessions(session, userMessage); //<.>
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    private void sendMessageToWebSocketsessions(WebSocketSession currentSession,
                                                String message) {
        for (WebSocketSession webSocketSession : sessions) {
            try {
                if (!webSocketSession.isOpen()) {
                    continue;
                }

                if (webSocketSession.equals(currentSession)) { //<.>
                    webSocketSession.sendMessage(new TextMessage(userMessageHtml(message)));
                } else {
                    webSocketSession.sendMessage(new TextMessage(incomingMessageHtml(message)));
                }
            } catch (IOException e) {
                LOGGER.debug("Unable to send message to {}", webSocketSession);
            }
        }
    }

    private String userMessageHtml(String message) { //<.>
        Context context = new Context(null, Map.of("message", message)); //<.>
        return templateEngine.process("index", Set.of("user-message"), context); //<.>
    }

    private String incomingMessageHtml(String message) {
        Context context = new Context(null, Map.of("message", message));
        return templateEngine.process("index", Set.of("incoming-message"), context);
    }
}
