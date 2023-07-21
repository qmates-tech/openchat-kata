package unit.tech.qmates.openchat.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.qmates.openchat.domain.UserNotFoundException;
import tech.qmates.openchat.domain.entity.Post;
import tech.qmates.openchat.domain.entity.RegisteredUser;
import tech.qmates.openchat.domain.repository.UserRepository;
import tech.qmates.openchat.domain.usecase.GetTimelineUseCase;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetTimelineUseCaseTest {

    private final RegisteredUser registeredUser = new RegisteredUser(UUID.randomUUID(), "alice90", "any");
    private final UserRepository userRepository = mock(UserRepository.class);
    private final GetTimelineUseCase usecase = new GetTimelineUseCase(userRepository);

    @BeforeEach
    void setUp() {
        when(userRepository.getUserById(registeredUser.uuid())).thenReturn(registeredUser);
    }

    @Test
    void returnsEmptyPostsCollectionForExistingUser() throws UserNotFoundException {
        List<Post> actual = usecase.run(registeredUser.uuid());
        assertThat(actual).isEmpty();
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
}