package unit.tech.qmates.openchat.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.qmates.openchat.domain.UserNotFoundException;
import tech.qmates.openchat.domain.entity.Post;
import tech.qmates.openchat.domain.entity.RegisteredUser;
import tech.qmates.openchat.domain.repository.PostRepository;
import tech.qmates.openchat.domain.repository.UserRepository;
import tech.qmates.openchat.domain.usecase.GetTimelineUseCase;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GetTimelineUseCaseTest {

    private final RegisteredUser registeredUser = new RegisteredUser(UUID.randomUUID(), "alice90", "any");
    private final UserRepository userRepository = mock(UserRepository.class);
    private final PostRepository postRepository = mock(PostRepository.class);
    private final ZonedDateTime now = ZonedDateTime.now();
    private final GetTimelineUseCase usecase = new GetTimelineUseCase(postRepository, userRepository);

    @BeforeEach
    void setUp() {
        when(userRepository.getUserById(registeredUser.uuid())).thenReturn(registeredUser);
        when(postRepository.getAllByUser(any())).thenReturn(Collections.emptySet());
    }

    @Test
    void returnsEmptyPostsCollectionForExistingUser() throws UserNotFoundException {
        List<Post> actual = usecase.run(registeredUser.uuid());
        assertThat(actual).isEmpty();
    }

    @Test
    void returnsPostsFromRepositoryInDescendingOrderByDateTime() throws UserNotFoundException {
        when(postRepository.getAllByUser(registeredUser.uuid())).thenReturn(
            Set.of(
                newPostWith("Second post", now.plus(1, ChronoUnit.MINUTES)),
                newPostWith("First post", now),
                newPostWith("Third post", now.plus(5, ChronoUnit.MINUTES)),
                newPostWith("Last post", now.plus(1, ChronoUnit.HOURS))
            )
        );

        List<Post> posts = usecase.run(registeredUser.uuid());

        assertThat(posts).hasSize(4);
        assertEquals("Last post", posts.get(0).text());
        assertEquals(now.plus(1, ChronoUnit.HOURS), posts.get(0).dateTime());
        assertEquals("Third post", posts.get(1).text());
        assertEquals("Second post", posts.get(2).text());
        assertEquals("First post", posts.get(3).text());
        assertEquals(now, posts.get(3).dateTime());
    }

    @Test
    void throwsExceptionForUnexistingUser() {
        UUID unexistingUserId = UUID.fromString("bfb62439-8711-447a-b0f8-bdbef5ceb4d7");

        UserNotFoundException thrownException = assertThrows(
            UserNotFoundException.class,
            () -> usecase.run(unexistingUserId)
        );

        assertEquals("User with uuid [bfb62439-8711-447a-b0f8-bdbef5ceb4d7] not found!", thrownException.getMessage());
    }

    private static Post newPostWith(String text, ZonedDateTime dateTime) {
        return new Post(UUID.randomUUID(), UUID.randomUUID(), text, dateTime);
    }
}