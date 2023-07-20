package tech.qmates.openchat.domain.repository;

import tech.qmates.openchat.domain.entity.Post;

import java.util.Set;
import java.util.UUID;

public interface PostRepository {
    void store(Post post);
    Set<Post> getAllByUser(UUID userId);
    void reset();
}
