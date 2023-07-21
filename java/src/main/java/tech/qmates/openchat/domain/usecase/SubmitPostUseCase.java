package tech.qmates.openchat.domain.usecase;

import tech.qmates.openchat.domain.UTCClock;
import tech.qmates.openchat.domain.UserNotFoundException;
import tech.qmates.openchat.domain.entity.Post;
import tech.qmates.openchat.domain.entity.RegisteredUser;
import tech.qmates.openchat.domain.repository.PostRepository;
import tech.qmates.openchat.domain.repository.UserRepository;

import java.util.UUID;

public class SubmitPostUseCase {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UTCClock clock;

    public SubmitPostUseCase(PostRepository postRepository, UserRepository userRepository, UTCClock clock) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.clock = clock;
    }

    public Post run(UUID authorUserId, String postText) throws UserNotFoundException {
        RegisteredUser user = userRepository.getUserById(authorUserId);
        if (user == null)
            throw new UserNotFoundException(authorUserId);

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
