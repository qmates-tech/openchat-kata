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
import java.util.Collections;
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
    void storeSinglePostAndGetAllByUser() {
        UUID postAuthorUserId = UUID.randomUUID();
        Post toStore = newPostOfUser(postAuthorUserId);

        repository.store(toStore);
        Set<Post> allByUser = repository.getAllByUser(postAuthorUserId);

        assertThat(allByUser).hasSize(1);
        Post first = allByUser.iterator().next();
        assertEquals(toStore, first);
    }

    @Test
    void getAllByUserReturnsOnlyUsersPosts() {
        UUID firstUserId = UUID.randomUUID();
        UUID secondUserId = UUID.randomUUID();
        repository.store(newPostOfUser(firstUserId));
        repository.store(newPostOfUser(secondUserId));
        repository.store(newPostOfUser(firstUserId));
        repository.store(newPostOfUser(firstUserId));
        repository.store(newPostOfUser(secondUserId));

        Set<Post> firstUserPosts = repository.getAllByUser(firstUserId);
        Set<Post> secondUserPosts = repository.getAllByUser(secondUserId);

        assertThat(firstUserPosts).hasSize(3);
        assertThat(firstUserPosts).allSatisfy(p -> assertEquals(firstUserId, p.userId()));
        assertThat(secondUserPosts).hasSize(2);
        assertThat(secondUserPosts).allSatisfy(p -> assertEquals(secondUserId, p.userId()));
    }

    @Test
    void allPostsOfUnexistingUserIdIsAnEmptyList() {
        Set<Post> posts = repository.getAllByUser(UUID.randomUUID());
        assertEquals(Collections.emptySet(), posts);
    }

    @Test
    void dateTimesAreStoredInUTCProperlyConvertedToSameInstant() {
        UUID postAuthorUserId = UUID.randomUUID();
        ZonedDateTime postDatetime = ZonedDateTime.of(
            LocalDateTime.of(2023, Month.JULY, 20, 11, 49, 19), ZoneId.of("Europe/Rome")
        );
        Post toStore = new Post(UUID.randomUUID(), postAuthorUserId, "any", postDatetime);

        repository.store(toStore);
        ZonedDateTime storedDateTime = repository.getAllByUser(postAuthorUserId).iterator().next().dateTime();

        ZonedDateTime expected = ZonedDateTime.of(LocalDateTime.of(2023, Month.JULY, 20, 11 - 2, 49, 19), ZoneId.of("UTC"));
        assertEquals(expected, storedDateTime);
        assertEquals(ZoneId.of("UTC"), storedDateTime.getZone());
        assertEquals(expected.toInstant(), storedDateTime.toInstant());
        assertEquals(expected.toEpochSecond(), storedDateTime.toEpochSecond());
    }

    @Test
    void dateTimeDoNotLoseNanosecondsAccuracy() {
        UUID postAuthorUserId = UUID.randomUUID();
        ZonedDateTime postDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
        Post toStore = new Post(UUID.randomUUID(), postAuthorUserId, "any", postDateTime);

        repository.store(toStore);
        Post stored = repository.getAllByUser(postAuthorUserId).iterator().next();

        assertEquals(postDateTime, stored.dateTime());
    }

    @Test
    void cannotStoreAlreadyUsedPostId() {
        Post post = newPostOfUser(UUID.randomUUID());
        repository.store(post);

        RuntimeException thrownException = assertThrows(
            RuntimeException.class,
            () -> repository.store(post)
        );
        assertEquals("Cannot store post, id value already used.", thrownException.getMessage());
    }

    private static Post newPostOfUser(UUID postAuthorUserId) {
        return new Post(UUID.randomUUID(), postAuthorUserId, "Text of the post.", ZonedDateTime.now(ZoneId.of("UTC")));
    }

}
