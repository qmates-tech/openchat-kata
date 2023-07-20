package tech.qmates.openchat.domain.entity;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public class Post {
    private final UUID id;
    private final UUID userId;
    private final String text;
    private final ZonedDateTime dateTime;

    public Post(UUID id, UUID userId, String text, ZonedDateTime dateTime) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.dateTime = dateTime;
    }

    public UUID id() {
        return id;
    }

    public UUID userId() {
        return userId;
    }

    public String text() {
        return text;
    }

    public ZonedDateTime dateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) && Objects.equals(userId, post.userId)
            && Objects.equals(text, post.text) && Objects.equals(dateTime, post.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, text, dateTime);
    }
}
