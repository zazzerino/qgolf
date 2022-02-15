package com.kdp.golf.user.db;

import com.kdp.golf.user.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name = "User")
@Table(name = "appuser")
public class UserEntity {

    @Id
    @SequenceGenerator(name = "userSeq", sequenceName = "appuser_id_seq")
    @GeneratedValue(generator = "userSeq")
    public Long id;
    public String name;
    public String sessionId;

    public UserEntity() {}

    public UserEntity(Long id, String name, String sessionId) {
        this.id = id;
        this.name = name;
        this.sessionId = sessionId;
    }

    public static UserEntity from(User u) {
        return new UserEntity(
                u.id(),
                u.name(),
                u.sessionId());
    }

    public User toUser() {
        return new User(id, name, sessionId);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
