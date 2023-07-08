package tech.qmates.openchat.domain.usecase;

import org.junit.jupiter.api.Test;
import tech.qmates.openchat.domain.entity.User;
import tech.qmates.openchat.domain.repository.UserRepository;
import tech.qmates.openchat.domain.usecase.RegisterUserUsecase.UsernameAlreadyInUseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RegisterUserUsecaseTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final RegisterUserUsecase usecase = new RegisterUserUsecase(userRepository);

    @Test
    void storeUser() throws UsernameAlreadyInUseException {
        when(userRepository.isUsernameAlreadyUsed("pippo")).thenReturn(false);
        usecase.run("pippo");
        verify(userRepository, times(1)).store(new User("pippo"));
    }

    @Test
    void throwsExceptionIfUsernameAlreadyInUse() {
        when(userRepository.isUsernameAlreadyUsed("pippo")).thenReturn(true);

        UsernameAlreadyInUseException thrownException = assertThrows(
            UsernameAlreadyInUseException.class,
            () -> usecase.run("pippo")
        );

        assertEquals("Username [pippo] already in use!", thrownException.getMessage());
        verify(userRepository, never()).store(any());
    }
}