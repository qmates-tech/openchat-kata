package tech.qmates.openchat.domain.repository;

import tech.qmates.openchat.domain.entity.RegisteredUser;
import tech.qmates.openchat.domain.entity.UserToRegister;

import java.util.Set;
import java.util.UUID;

public interface UserRepository {
    void store(UserToRegister user);
    boolean isUsernameAlreadyUsed(String username);
    Set<RegisteredUser> getAll();
    RegisteredUser getUserById(UUID uuid);
    void reset();
}
