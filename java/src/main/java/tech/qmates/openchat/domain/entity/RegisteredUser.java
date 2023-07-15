package tech.qmates.openchat.domain.entity;

import java.util.Objects;
import java.util.UUID;

public class RegisteredUser {

    private final UUID uuid;
    private final String username;
    private final String about;

    public RegisteredUser(UUID uuid, String username, String about) {
        this.uuid = uuid;
        this.username = username;
        this.about = about;
    }

    public UUID uuid() {
        return this.uuid;
    }

    public String username() {
        return this.username;
    }

    public String about() {
        return this.about;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisteredUser that = (RegisteredUser) o;
        return Objects.equals(uuid, that.uuid) && Objects.equals(username, that.username) && Objects.equals(about, that.about);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, username, about);
    }
}
