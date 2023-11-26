package com.modernfrontendshtmx.wsdemo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket //<.>
public class WebSocketConfig implements WebSocketConfigurer { //<.>
    private final SocketHandler socketHandler;

    public WebSocketConfig(SocketHandler socketHandler) { //<.>
        this.socketHandler = socketHandler;
    }

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler, "/chatroom"); //<.>
    }
}
