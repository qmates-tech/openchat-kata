package tech.qmates.openchat.domain.usecase;

import tech.qmates.openchat.domain.entity.User;
import tech.qmates.openchat.domain.repository.UserRepository;

import java.util.UUID;

public class RegisterUserUseCase {

    private final UserRepository userRepository;

    public RegisterUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID run(String username, String password, String about) throws UsernameAlreadyInUseException {
        if(userRepository.isUsernameAlreadyUsed(username))
            throw new UsernameAlreadyInUseException(username);

        User userToBeStored = new User(UUID.randomUUID(), username, password, about);
        this.userRepository.store(userToBeStored);
        return userToBeStored.uuid();
    }

    public static class UsernameAlreadyInUseException extends Exception {
        public UsernameAlreadyInUseException(String username) {
            super("Username [" + username + "] already in use!");
        }
    }
}
