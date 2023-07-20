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
import java.util.stream.Collectors;

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
        ZonedDateTime postDateTime = ZonedDateTime.now(ZoneId.of("UTC")).withNano(0);
        Post toStore = new Post(UUID.randomUUID(), postAuthorUserId, "Text of the post.", postDateTime);

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
        Post post = newPostOfUser(UUID.randomUUID());
        repository.store(post);

        RuntimeException thrownException = assertThrows(
            RuntimeException.class,
            () -> repository.store(post)
        );
        assertEquals("Cannot store post, id value already used.", thrownException.getMessage());
    }

    private static Post newPostOfUser(UUID postAuthorUserId) {
        return new Post(UUID.randomUUID(), postAuthorUserId, "Text of the post.", ZonedDateTime.now());
    }

}
