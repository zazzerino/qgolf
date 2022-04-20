package com.kdp.golf.user;

import com.google.common.base.Objects;

public class User {

    private final Long id;
    private String name;
    private final String sessionId;

    public static final String DEFAULT_NAME = "anonymous";

    public User(Long id, String name, String sessionId) {
        this.id = id;
        this.name = name;
        this.sessionId = sessionId;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return java.util.Objects.equals(name, DEFAULT_NAME)
                ? DEFAULT_NAME + id
                : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String sessionId() {
        return sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(id, user.id)
                && Objects.equal(name, user.name)
                && Objects.equal(sessionId, user.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, sessionId);
    }

    @Override
    public String toString() {
        return "User[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "sessionId=" + sessionId + ']';
    }
}
