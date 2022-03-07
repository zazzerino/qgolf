package com.kdp.golf.user;

public record UserDto(Long id,
                      String name) {

    public static UserDto from(User u) {
        return new UserDto(u.id(), u.name());
    }
}
