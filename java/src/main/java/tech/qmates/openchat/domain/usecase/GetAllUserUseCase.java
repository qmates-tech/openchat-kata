package tech.qmates.openchat.domain.usecase;

import tech.qmates.openchat.domain.entity.RegisteredUser;
import tech.qmates.openchat.domain.repository.UserRepository;

import java.util.Set;

public class GetAllUserUseCase {
    private final UserRepository userRepository;

    public GetAllUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Set<RegisteredUser> run() {
        return userRepository.getAll();
    }
}
