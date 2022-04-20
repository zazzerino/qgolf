package com.kdp.golf.websocket.response;

import io.vertx.core.json.Json;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class ResponseEncoder implements Encoder.Text<Response> {

    @Override
    public String encode(Response response) {
        return Json.encode(response);
    }

    @Override
    public void init(EndpointConfig config) {}

    @Override
    public void destroy() {}
}
