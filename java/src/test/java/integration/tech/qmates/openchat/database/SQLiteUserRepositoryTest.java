package integration.tech.qmates.openchat.database;

import org.junit.jupiter.api.Test;
import tech.qmates.openchat.database.SQLiteUserRepository;
import tech.qmates.openchat.domain.entity.RegisteredUser;
import tech.qmates.openchat.domain.entity.UserToRegister;
import tech.qmates.openchat.domain.repository.UserRepository;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SQLiteUserRepositoryTest {

    UserRepository repository = buildRepository();

    @Test
    void getUsersFromEmptyRepository() {
        List<RegisteredUser> users = repository.getAll();

        assertEquals(0, users.size());
    }

    @Test
    void fillGetAllAndResetRepository() {
        UserToRegister alice = UserToRegister.newWith("alice90", "pass1234", "About alice user.");
        UserToRegister bob = UserToRegister.newWith("bob89", "123pass", "About bob user.");
        repository.store(alice);
        repository.store(bob);

        List<RegisteredUser> users = repository.getAll();
        assertEquals(2, users.size());
        assertThat(users).anySatisfy(user -> {
            assertEquals(alice.uuid(), user.uuid());
            assertEquals("alice90", alice.username());
            assertEquals("About alice user.", alice.about());
        });
        assertThat(users).anySatisfy(user -> {
            assertEquals(bob.uuid(), user.uuid());
            assertEquals("bob89", bob.username());
            assertEquals("About bob user.", bob.about());
        });

        repository.reset();
        assertEquals(0, repository.getAll().size());
    }

    private UserRepository buildRepository() {
        URL res = getClass().getClassLoader().getResource("integration.test.db");
        if (res == null)
            throw new RuntimeException("Cannot find integration test database file!");

        try {
            return new SQLiteUserRepository(res.toURI().getPath());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
