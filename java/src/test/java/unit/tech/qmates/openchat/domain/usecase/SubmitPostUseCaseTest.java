package unit.tech.qmates.openchat.domain.usecase;

import org.junit.jupiter.api.Test;
import tech.qmates.openchat.domain.UTCClock;
import tech.qmates.openchat.domain.entity.Post;
import tech.qmates.openchat.domain.repository.PostRepository;
import tech.qmates.openchat.domain.usecase.SubmitPostUseCase;

import java.time.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class SubmitPostUseCaseTest {

    private final ZonedDateTime fakeToday = ZonedDateTime.of(
        LocalDate.of(2023, Month.JULY, 21), LocalTime.of(16, 40, 19), ZoneId.of("UTC")
    );
    private final UTCClock fakeClock = () -> fakeToday;
    private final PostRepository postRepository = mock(PostRepository.class);

    private final SubmitPostUseCase usecase = new SubmitPostUseCase(postRepository, fakeClock);

    @Test
    void returnsTheStoredPost() {
        UUID registeredUserId = UUID.randomUUID();

        Post storedPost = usecase.run(registeredUserId, "The post text.");

        assertNotNull(storedPost.id());
        assertEquals(registeredUserId, storedPost.userId());
        assertEquals("The post text.", storedPost.text());
        assertNotNull(storedPost.dateTime());
    }

    @Test
    void returnedPostHasDateTimeFromClock() {
        Post storedPost = usecase.run(UUID.randomUUID(), "any");
        assertEquals(fakeToday, storedPost.dateTime());
    }

    @Test
    void storeThePostInRepository() {
        UUID authorUserId = UUID.randomUUID();

        usecase.run(authorUserId, "The post's text.");

        verify(postRepository, times(1)).store(argThat((p) -> {
            assertNotNull(p.id());
            assertEquals(authorUserId, p.userId());
            assertEquals("The post's text.", p.text());
            assertEquals(fakeToday, p.dateTime());
            return true;
        }));
    }

}