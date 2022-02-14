package com.kdp.golf.websocket;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.server.ServerEndpoint;

@ApplicationScoped
@ServerEndpoint(
        value = "/ws",
        decoders = MessageDecoder.class,
        encoders = ResponseEncoder.class
)
public class WebSocket {
}
