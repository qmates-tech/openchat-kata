package unit.tech.qmates.openchat.domain.usecase;

import org.junit.jupiter.api.Test;
import tech.qmates.openchat.domain.usecase.GetTimelineUseCase;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GetTimelineUseCaseTest {

    private final GetTimelineUseCase usecase = new GetTimelineUseCase();

    @Test
    void returnsEmptyPostListForExistingUser() {
        UUID existingUserId = UUID.randomUUID();
        List<Object> actual = usecase.run(existingUserId);
        assertThat(actual).isEmpty();
    }

}