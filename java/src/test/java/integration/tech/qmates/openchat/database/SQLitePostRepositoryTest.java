package integration.tech.qmates.openchat.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.CollectionUtils;
import tech.qmates.openchat.database.SQLitePostRepository;
import tech.qmates.openchat.database.SQLiteUserRepository;
import tech.qmates.openchat.domain.entity.Post;
import tech.qmates.openchat.domain.repository.PostRepository;
import tech.qmates.openchat.domain.repository.UserRepository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SQLitePostRepositoryTest extends SQLiteRepositoryTest {

    PostRepository repository = new SQLitePostRepository(getSqliteFilePath());

    @BeforeEach
    void setUp() {
        repository.reset();
    }

    @Test
    void storePostAndGetAllByUser() {
        UUID userId = UUID.randomUUID();
        ZonedDateTime postDatetime = ZonedDateTime.of(LocalDateTime.of(2023, Month.JULY, 20, 11, 49, 19), ZoneId.of("UTC"));
        Post toStore = new Post(UUID.randomUUID(), userId, "Text of the post.", postDatetime);

        repository.store(toStore);
        Set<Post> allByUser  = repository.getAllByUser(userId);

        assertThat(allByUser).hasSize(1);
        assertEquals(toStore, allByUser.iterator().next());
    }

    // cannot store already used post id
    // datetime store is timezone safe
}