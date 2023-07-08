package tech.qmates.openchat.domain.usecase;

import tech.qmates.openchat.domain.entity.User;
import tech.qmates.openchat.domain.repository.UserRepository;

public class RegisterUserUsecase {

    private final UserRepository userRepository;

    public RegisterUserUsecase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void run(String username) throws UsernameAlreadyInUseException {
        if(userRepository.isUsernameAlreadyUsed(username))
            throw new UsernameAlreadyInUseException(username);

        this.userRepository.store(new User(username));
    }

    public static class UsernameAlreadyInUseException extends Exception {
        public UsernameAlreadyInUseException(String username) {
            super("Username [" + username + "] already in use!");
        }
    }
}
