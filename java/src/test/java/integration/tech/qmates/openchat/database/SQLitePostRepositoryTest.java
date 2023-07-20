package integration.tech.qmates.openchat.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.qmates.openchat.database.SQLitePostRepository;
import tech.qmates.openchat.domain.entity.Post;
import tech.qmates.openchat.domain.repository.PostRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SQLitePostRepositoryTest extends SQLiteRepositoryTest {

    PostRepository repository = new SQLitePostRepository(getSqliteFilePath());

    @BeforeEach
    void setUp() {
        repository.reset();
    }

    @Test
    void storePostAndGetAllByUser() {
        UUID postAuthorUserId = UUID.randomUUID();
        ZonedDateTime postDatetime = ZonedDateTime.of(LocalDateTime.of(2023, Month.JULY, 20, 11, 49, 19), ZoneId.of("UTC"));
        Post toStore = new Post(UUID.randomUUID(), postAuthorUserId, "Text of the post.", postDatetime);

        repository.store(toStore);
        Set<Post> allByUser = repository.getAllByUser(postAuthorUserId);

        assertThat(allByUser).hasSize(1);
        assertEquals(toStore, allByUser.iterator().next());
    }

    @Test
    void dateTimesAreStoredInUTCProperlyConvertedToSameInstant() {
        UUID postAuthorUserId = UUID.randomUUID();
        ZonedDateTime postDatetime = ZonedDateTime.of(LocalDateTime.of(2023, Month.JULY, 20, 11, 49, 19), ZoneId.of("Europe/Rome"));
        Post toStore = new Post(UUID.randomUUID(), postAuthorUserId, "any", postDatetime);

        repository.store(toStore);
        Post storedPost = repository.getAllByUser(postAuthorUserId).iterator().next();

        ZonedDateTime expected = ZonedDateTime.of(LocalDateTime.of(2023, Month.JULY, 20, 9, 49, 19), ZoneId.of("UTC"));
        assertEquals(expected, storedPost.dateTime());
        assertEquals(expected.toInstant(), postDatetime.toInstant());
        assertEquals(expected.toEpochSecond(), postDatetime.toEpochSecond());
    }

    @Test
    void cannotStoreAlreadyUsedPostId() {
        Post post = new Post(UUID.randomUUID(), UUID.randomUUID(), "any", ZonedDateTime.now());
        repository.store(post);

        RuntimeException thrownException = assertThrows(
            RuntimeException.class,
            () -> repository.store(post)
        );
        assertEquals("Cannot store post, id value already used.", thrownException.getMessage());
    }

}
