package unit.tech.qmates.openchat.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import tech.qmates.openchat.domain.UTCClock;
import tech.qmates.openchat.domain.UserNotFoundException;
import tech.qmates.openchat.domain.entity.Post;
import tech.qmates.openchat.domain.entity.RegisteredUser;
import tech.qmates.openchat.domain.repository.PostRepository;
import tech.qmates.openchat.domain.repository.UserRepository;
import tech.qmates.openchat.domain.usecase.SubmitPostUseCase;

import java.time.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubmitPostUseCaseTest {

    private final RegisteredUser registeredUser = new RegisteredUser(UUID.randomUUID(), "alice90", "any");
    private final ZonedDateTime fakeToday = ZonedDateTime.of(
        LocalDate.of(2023, Month.JULY, 21), LocalTime.of(16, 40, 19), ZoneId.of("UTC")
    );

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PostRepository postRepository = mock(PostRepository.class);
    private final UTCClock fakeClock = () -> fakeToday;

    private final SubmitPostUseCase usecase = new SubmitPostUseCase(postRepository, userRepository, fakeClock);

    @BeforeEach
    void setUp() {
        when(userRepository.getUserById(registeredUser.uuid())).thenReturn(registeredUser);
    }

    @Test
    void returnsTheStoredPost() throws UserNotFoundException, SubmitPostUseCase.InappropriateLanguageException {
        Post storedPost = usecase.run(registeredUser.uuid(), "The post text.");

        assertNotNull(storedPost.id());
        assertEquals(registeredUser.uuid(), storedPost.userId());
        assertEquals("The post text.", storedPost.text());
        assertNotNull(storedPost.dateTime());
    }

    @Test
    void returnedPostHasDateTimeFromClock() throws UserNotFoundException, SubmitPostUseCase.InappropriateLanguageException {
        Post storedPost = usecase.run(registeredUser.uuid(), "any");
        assertEquals(fakeToday, storedPost.dateTime());
    }

    @Test
    void storeThePostInRepository() throws UserNotFoundException, SubmitPostUseCase.InappropriateLanguageException {
        usecase.run(registeredUser.uuid(), "The post's text.");

        verify(postRepository, times(1)).store(argThat((p) -> {
            assertNotNull(p.id());
            assertEquals(registeredUser.uuid(), p.userId());
            assertEquals("The post's text.", p.text());
            assertEquals(fakeToday, p.dateTime());
            return true;
        }));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "I like orange juice !",
        "Orange is my favorite fruit.",
        "I LOVE ORANGE !",
        "I collected many oranges today.",
        "I saw an elephant in africa.",
        "Elephants are so cute!",
        "ELEPHANTS ARE GIANT !",
        "I want an ice cream.",
        "I'll have an ICE Cream tonight."
    })
    void throwsExceptionForInappropriateLanguageInPostText(String postText) {
        SubmitPostUseCase.InappropriateLanguageException thrownException = assertThrows(
            SubmitPostUseCase.InappropriateLanguageException.class,
            () -> usecase.run(registeredUser.uuid(), postText)
        );

        String expectedExceptionMessage = "Post text contains inappropriate language: " + postText;
        assertEquals(expectedExceptionMessage, thrownException.getMessage());
    }

    @Test
    void throwsExceptionForUnexistingUser() {
        UUID unexistingUserId = UUID.fromString("293307a4-86cd-49a8-ac5f-9347a3276e75");

        UserNotFoundException thrownException = assertThrows(
            UserNotFoundException.class,
            () -> usecase.run(unexistingUserId, "any")
        );

        assertEquals("User with uuid [293307a4-86cd-49a8-ac5f-9347a3276e75] not found!", thrownException.getMessage());
    }

}