package tech.qmates.openchat.domain.usecase;

import tech.qmates.openchat.domain.UTCClock;
import tech.qmates.openchat.domain.entity.Post;
import tech.qmates.openchat.domain.repository.PostRepository;

import java.util.UUID;

public class SubmitPostUseCase {

    private final PostRepository postRepository;
    private final UTCClock clock;

    public SubmitPostUseCase(PostRepository postRepository, UTCClock clock) {
        this.postRepository = postRepository;
        this.clock = clock;
    }

    public Post run(UUID authorUserId, String postText) {
        Post postToStore = new Post(
            UUID.randomUUID(),
            authorUserId,
            postText,
            clock.now()
        );
        postRepository.store(postToStore);
        return postToStore;
    }
}
