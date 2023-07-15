package integration.tech.qmates.openchat.database;

import org.junit.jupiter.api.Test;
import tech.qmates.openchat.database.SQLiteUserRepository;
import tech.qmates.openchat.domain.entity.UserToRegister;
import tech.qmates.openchat.domain.repository.UserRepository;

public class SQLiteUserRepositoryTest {

    private static final String SQLITE_FILEPATH = "src/test/integration.test.db";
    private final UserRepository repository = new SQLiteUserRepository(SQLITE_FILEPATH);

    @Test
    void fooo() {
        UserToRegister userToStore = UserToRegister.newWith("alice90", "pass1234", "About alice user.");
        repository.store(userToStore);
    }
}
