package com.kdp.golf.user;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnMessage;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class UserControllerTest {

    static LinkedBlockingDeque<String> MESSAGES = new LinkedBlockingDeque<>();

    @TestHTTPResource("/ws")
    URI uri;

    @Test
    @TestTransaction
    void connect() throws DeploymentException, IOException, InterruptedException {
        try (var session = ContainerProvider
                .getWebSocketContainer()
                .connectToServer(Client.class, uri)) {

            var response = MESSAGES.poll(4, TimeUnit.SECONDS);
            assertNotNull(response);
        }

        Thread.sleep(500);
    }

    @ClientEndpoint
    public static class Client {

        @OnMessage
        public void onMessage(String msg) {
            MESSAGES.add(msg);
        }
    }
}