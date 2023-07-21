package tech.qmates.openchat.domain.usecase;

import tech.qmates.openchat.domain.UTCClock;
import tech.qmates.openchat.domain.entity.Post;

import java.time.ZonedDateTime;
import java.util.UUID;

public class SubmitPostUseCase {

    private final UTCClock clock;

    public SubmitPostUseCase(UTCClock clock) {
        this.clock = clock;
    }

    public Post run(UUID authorUserId, String postText) {
        return new Post(
            UUID.randomUUID(),
            authorUserId,
            postText,
            clock.now()
        );
    }
}
