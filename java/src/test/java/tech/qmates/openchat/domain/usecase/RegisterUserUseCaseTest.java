package tech.qmates.openchat.domain.usecase;

import org.junit.jupiter.api.Test;
import tech.qmates.openchat.domain.repository.UserRepository;
import tech.qmates.openchat.domain.usecase.RegisterUserUseCase.UsernameAlreadyInUseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterUserUseCaseTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final RegisterUserUseCase usecase = new RegisterUserUseCase(userRepository);

    @Test
    void storeUser() throws UsernameAlreadyInUseException {
        when(userRepository.isUsernameAlreadyUsed("pippo")).thenReturn(false);
        usecase.run("pippo", "password", "about pippo");
        verify(userRepository, times(1)).store(assertArg(user -> {
            assertNotNull(user.uuid());
            assertEquals("pippo", user.username());
            assertEquals("password", user.password());
            assertEquals("about pippo", user.about());
        }));
    }

    @Test
    void throwsExceptionIfUsernameAlreadyInUse() {
        when(userRepository.isUsernameAlreadyUsed("pippo")).thenReturn(true);

        UsernameAlreadyInUseException thrownException = assertThrows(
            UsernameAlreadyInUseException.class,
            () -> usecase.run("pippo", "any", "any")
        );

        assertEquals("Username [pippo] already in use!", thrownException.getMessage());
        verify(userRepository, never()).store(any());
    }
}