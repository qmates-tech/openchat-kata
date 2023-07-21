package unit.tech.qmates.openchat.domain.usecase;

import org.junit.jupiter.api.Test;
import tech.qmates.openchat.domain.entity.Post;
import tech.qmates.openchat.domain.usecase.SubmitPostUseCase;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SubmitPostUseCaseTest {

    private final SubmitPostUseCase usecase = new SubmitPostUseCase();

    @Test
    void returnsTheStoredPost() {
        UUID registeredUserId = UUID.randomUUID();

        Post storedPost = usecase.run(registeredUserId, "The post text.");

        assertNotNull(storedPost.id());
        assertEquals(registeredUserId, storedPost.userId());
        assertEquals("The post text.", storedPost.text());
        assertNotNull(storedPost.dateTime());
    }

}