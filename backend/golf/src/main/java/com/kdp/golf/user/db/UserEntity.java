package com.kdp.golf.user.db;

import com.kdp.golf.user.User;

import javax.persistence.*;

@Entity(name = "User")
@Table(name = "appuser")
public class UserEntity {

    @Id
    @SequenceGenerator(name = "userSeq", sequenceName = "appuser_id_seq")
    @GeneratedValue(generator = "userSeq")
    public Long id;
    public String name;
    public String sessionId;

    public static UserEntity from(User u) {
        var entity = new UserEntity();
        entity.id = u.id();
        entity.name = u.name();
        entity.sessionId = u.sessionId();
        return entity;
    }

    public User toUser() {
        return new User(id, name, sessionId);
    }
}
