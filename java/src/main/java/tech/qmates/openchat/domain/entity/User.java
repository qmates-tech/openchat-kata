package tech.qmates.openchat.domain.entity;

import java.util.Objects;
import java.util.UUID;

public class User {

    private final UUID uuid;
    private final String username;
    private final String password;
    private final String about;

    public User(UUID uuid, String username, String password, String about) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.about = about;
    }

    public UUID uuid() {
        return this.uuid;
    }

    public String username() {
        return this.username;
    }

    public String password() {
        return this.password;
    }

    public String about() {
        return this.about;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(uuid, user.uuid) &&
            Objects.equals(username, user.username) &&
            Objects.equals(password, user.password) &&
            Objects.equals(about, user.about);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, username, password, about);
    }
}
