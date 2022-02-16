package com.kdp.golf.user;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnMessage;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

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
            var json = new JsonObject(response);

            var type = json.getString("type");
            assertEquals("User", type);
        }

        Thread.sleep(50); // this keeps the app running long enough to clean up the user after disconnect
    }

    @ClientEndpoint
    public static class Client {

        @OnMessage
        public void onMessage(String msg) {
            MESSAGES.add(msg);
        }
    }
}