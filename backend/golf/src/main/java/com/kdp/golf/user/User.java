package com.kdp.golf.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record User(Long id,
                   String name,
                   @JsonIgnore String sessionId) {

    public static final String DEFAULT_NAME = "anon";
}
