package integration.tech.qmates.openchat.database;

import org.junit.jupiter.api.Test;
import tech.qmates.openchat.database.SQLiteUserRepository;
import tech.qmates.openchat.domain.entity.User;
import tech.qmates.openchat.domain.repository.UserRepository;

import java.util.UUID;

public class SQLiteUserRepositoryTest {

    private static final String SQLITE_FILEPATH = "src/test/integration.test.db";
    private final UserRepository repository = new SQLiteUserRepository(SQLITE_FILEPATH);

    @Test
    void fooo() {
        User userToStore = new User(UUID.randomUUID(), "alice90", "pass1234", "About alice user.");
        repository.store(userToStore);
    }
}
