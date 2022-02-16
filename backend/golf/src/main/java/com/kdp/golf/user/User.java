package com.kdp.golf.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kdp.golf.user.ImmutableUser;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableUser.class)
public abstract class User {

    @Value.Parameter
    public abstract Long id();
    @Value.Parameter
    public abstract String name();
    @Value.Parameter
    public abstract String sessionId();

    public static final String DEFAULT_NAME = "anon";
}
