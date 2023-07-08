package tech.qmates.openchat.domain.repository;

import tech.qmates.openchat.domain.entity.User;

import java.util.List;

public interface UserRepository {
    void store(User user);
    boolean isUsernameAlreadyUsed(String username);
    List<User> getAll();
}
