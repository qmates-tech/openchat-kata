package tech.qmates.openchat.domain.usecase;

import tech.qmates.openchat.domain.entity.RegisteredUser;
import tech.qmates.openchat.domain.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GetTimelineUseCase {
    private final UserRepository userRepository;

    public GetTimelineUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Object> run(UUID userUUID) throws UserNotFoundException {
        RegisteredUser user = userRepository.getUserById(userUUID);
        if (user == null)
            throw new UserNotFoundException(userUUID);

        return new ArrayList<>();
    }

    public static class UserNotFoundException extends Exception {
        public UserNotFoundException(UUID uuid) {
            super("User with uuid [" + uuid + "] not found!");
        }
    }
}
