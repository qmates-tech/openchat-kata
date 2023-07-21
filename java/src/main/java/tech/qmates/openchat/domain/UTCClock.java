package tech.qmates.openchat.domain;

import java.time.ZonedDateTime;

@FunctionalInterface
public interface UTCClock {
   ZonedDateTime now();
}
