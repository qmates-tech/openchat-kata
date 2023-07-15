package tech.qmates.openchat.database;

import tech.qmates.openchat.domain.entity.User;
import tech.qmates.openchat.domain.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    @Override
    public void store(User user) {
        this.users.add(user);
    }

    @Override
    public boolean isUsernameAlreadyUsed(String username) {
        return users.stream().anyMatch(user -> user.username().equals(username));
    }

    @Override
    public List<User> getAll() {
        return Collections.unmodifiableList(this.users);
    }

    @Override
    public void reset() {
        this.users.clear();
    }
}
