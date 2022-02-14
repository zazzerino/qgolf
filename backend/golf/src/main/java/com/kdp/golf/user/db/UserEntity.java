package com.kdp.golf.user.db;

import com.kdp.golf.user.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.ResultSet;
import java.sql.SQLException;

@Entity(name = "User")
@Table(name = "appuser")
public class UserEntity {

    @Id
    @SequenceGenerator(name = "userSeq", sequenceName = "appuser_id_seq")
    @GeneratedValue(generator = "userSeq")
    public Long id;
    public String name;
    public String sessionId;

    public static UserEntity of(Long id, String name, String sessionId) {
        var entity = new UserEntity();
        entity.id = id;
        entity.name = name;
        entity.sessionId = sessionId;
        return entity;
    }

    public static UserEntity of(String name, String sessionId) {
        return UserEntity.of(null, name, sessionId);
    }

    public static UserEntity from(User u) {
        return UserEntity.of(
                u.id(),
                u.name(),
                u.sessionId());
    }

    public static UserEntity from(ResultSet rs) throws SQLException {
        var id = rs.getLong("id");
        var name = rs.getString("name");
        var sessionId = rs.getString("session_id");

        return UserEntity.of(id, name, sessionId);
    }

    public User toUser() {
        return new User(id, name, sessionId);
    }
}
