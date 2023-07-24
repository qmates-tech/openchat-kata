package tech.qmates.openchat.domain.usecase;

import tech.qmates.openchat.domain.UserNotFoundException;
import tech.qmates.openchat.domain.entity.Post;
import tech.qmates.openchat.domain.entity.RegisteredUser;
import tech.qmates.openchat.domain.repository.PostRepository;
import tech.qmates.openchat.domain.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GetTimelineUseCase {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public GetTimelineUseCase(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<Post> run(UUID userUUID) throws UserNotFoundException {
        RegisteredUser user = userRepository.getUserById(userUUID);
        if (user == null)
            throw new UserNotFoundException(userUUID);

        return postRepository.getAllByUser(userUUID)
            .stream()
            .sorted(Comparator.comparing(Post::dateTime).reversed())
            .collect(Collectors.toList());
    }

}