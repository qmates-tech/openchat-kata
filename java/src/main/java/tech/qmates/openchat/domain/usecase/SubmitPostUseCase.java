package tech.qmates.openchat.domain.usecase;

import tech.qmates.openchat.domain.UTCClock;
import tech.qmates.openchat.domain.UserNotFoundException;
import tech.qmates.openchat.domain.entity.Post;
import tech.qmates.openchat.domain.entity.RegisteredUser;
import tech.qmates.openchat.domain.repository.PostRepository;
import tech.qmates.openchat.domain.repository.UserRepository;

import java.util.List;
import java.util.UUID;

public class SubmitPostUseCase {

    public static final List<String> FORBIDDEN_WORDS = List.of("orange", "elephant");

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UTCClock clock;

    public SubmitPostUseCase(PostRepository postRepository, UserRepository userRepository, UTCClock clock) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.clock = clock;
    }

    public Post run(UUID authorUserId, String postText) throws UserNotFoundException, InappropriateLanguageException {
        RegisteredUser user = userRepository.getUserById(authorUserId);
        if (user == null)
            throw new UserNotFoundException(authorUserId);

        if(containsForbiddenWords(postText))
            throw new InappropriateLanguageException(postText);

        Post postToStore = new Post(
            UUID.randomUUID(),
            authorUserId,
            postText,
            clock.now()
        );
        postRepository.store(postToStore);
        return postToStore;
    }

    private static boolean containsForbiddenWords(String postText) {
        return FORBIDDEN_WORDS.stream()
            .anyMatch((word) -> postText.toLowerCase().contains(word));
    }

    public static class InappropriateLanguageException extends Exception {
        public InappropriateLanguageException(String postText) {
            super("Post text contains inappropriate language: " + postText);
        }
    }
}
