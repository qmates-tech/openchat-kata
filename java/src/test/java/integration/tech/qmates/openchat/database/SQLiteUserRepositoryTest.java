package integration.tech.qmates.openchat.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.qmates.openchat.database.SQLiteUserRepository;
import tech.qmates.openchat.domain.entity.RegisteredUser;
import tech.qmates.openchat.domain.entity.UserToRegister;
import tech.qmates.openchat.domain.repository.UserRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SQLiteUserRepositoryTest extends SQLiteRepositoryTest {

    UserRepository repository = new SQLiteUserRepository(getSqliteFilePath());

    @BeforeEach
    void setUp() {
        repository.reset();
    }

    @Test
    void getUsersFromEmptyRepository() {
        Set<RegisteredUser> users = repository.getAll();
        assertEquals(0, users.size());
    }

    @Test
    void fillGetAllAndResetRepository() {
        UserToRegister alice = newUserWith("alice90", "pass1234", "About alice user.");
        UserToRegister bob = newUserWith("bob89", "123pass", "About bob user.");
        repository.store(alice);
        repository.store(bob);

        Set<RegisteredUser> users = repository.getAll();
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

    @Test
    void recognizesAlreadyUsedUsername() {
        UserToRegister alice = newUserWith("alice90");
        repository.store(alice);

        assertTrue(repository.isUsernameAlreadyUsed("alice90"));
        assertFalse(repository.isUsernameAlreadyUsed("bob89"));
    }

    @Test
    void rejectsAlreadyStoredUuidValueAsId() {
        UserToRegister alice = newUserWith("any");
        repository.store(alice);

        RuntimeException thrownException = assertThrows(
            RuntimeException.class,
            () -> repository.store(alice)
        );
        assertEquals("Cannot store user, uuid value already used.", thrownException.getMessage());
    }

    @Test
    void passwordAreStoredHashedInSha256() throws SQLException {
        UserToRegister alice = newUserWith("any", "notcryptedpassword");
        repository.store(alice);

        String storedPassword = getStoredPasswordFromDatabaseForUserId(alice.uuid());
        String expected = "0e667c78b4d937db64bfefbcb572e66095a1c2a41a948b519e52b09638819127"; // sha256 of notcryptedpassword
        assertEquals(expected, storedPassword);
    }

    @Test
    void getExistingUserById() {
        UUID aliceUUID = UUID.randomUUID();
        UserToRegister alice = new UserToRegister(aliceUUID, "alice90", "any", "About alice user.");
        repository.store(alice);

        RegisteredUser userFromDb = repository.getUserById(alice.uuid());

        RegisteredUser expected = new RegisteredUser(aliceUUID, "alice90", "About alice user.");
        assertEquals(expected, userFromDb);
    }

    @Test
    void getUserByIdReturnsNullWithoutUsers() {
        assertNull(repository.getUserById(UUID.randomUUID()));
    }

    private String getStoredPasswordFromDatabaseForUserId(UUID uuid) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + getSqliteFilePath());
        PreparedStatement query = connection.prepareStatement("SELECT password FROM users WHERE id = ?");
        query.setString(1, uuid.toString());
        return query.executeQuery().getString("password");
    }

    private UserToRegister newUserWith(String username) {
        return newUserWith(username, "any");
    }

    private UserToRegister newUserWith(String username, String password) {
        return newUserWith(username, password, "any");
    }

    private UserToRegister newUserWith(String username, String password, String about) {
        return new UserToRegister(UUID.randomUUID(), username, password, about);
    }

}
