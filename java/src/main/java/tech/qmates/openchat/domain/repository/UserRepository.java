package tech.qmates.openchat.domain.repository;

import tech.qmates.openchat.domain.entity.User;

public interface UserRepository {
    void store(User user);
    boolean isUsernameAlreadyUsed(String username);
}
