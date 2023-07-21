package unit.tech.qmates.openchat.domain.usecase;

import org.junit.jupiter.api.Test;
import tech.qmates.openchat.domain.entity.RegisteredUser;
import tech.qmates.openchat.domain.repository.UserRepository;
import tech.qmates.openchat.domain.usecase.GetTimelineUseCase;
import tech.qmates.openchat.domain.UserNotFoundException;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GetTimelineUseCaseTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final GetTimelineUseCase usecase = new GetTimelineUseCase(userRepository);

    @Test
    void returnsEmptyPostsCollectionForExistingUser() throws UserNotFoundException {
        UUID existingUserId = UUID.randomUUID();
        when(userRepository.getUserById(existingUserId)).thenReturn(new RegisteredUser(existingUserId, "any", "any"));

        List<Object> actual = usecase.run(existingUserId);

        assertThat(actual).isEmpty();
    }

    @Test
    void throwsExceptionForUnexistingUser() {
        when(userRepository.getUserById(any())).thenReturn(null);
        UUID unexistingUserId = UUID.fromString("bfb62439-8711-447a-b0f8-bdbef5ceb4d7");

        UserNotFoundException thrownException = assertThrows(
            UserNotFoundException.class,
            () -> usecase.run(unexistingUserId)
        );

        assertEquals("User with uuid [bfb62439-8711-447a-b0f8-bdbef5ceb4d7] not found!", thrownException.getMessage());
    }
}