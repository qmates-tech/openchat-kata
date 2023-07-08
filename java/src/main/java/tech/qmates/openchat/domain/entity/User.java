package tech.qmates.openchat.domain.entity;

import java.util.Objects;

public class User {

    private final String username;
    private final String password;
    private final String about;

    public User(String username, String password, String about) {
        this.username = username;
        this.password = password;
        this.about = about;
    }

    public String username() {
        return this.username;
    }

    public String password() {
        return password;
    }

    public String about() {
        return about;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
            Objects.equals(password, user.password) &&
            Objects.equals(about, user.about);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, about);
    }
}
