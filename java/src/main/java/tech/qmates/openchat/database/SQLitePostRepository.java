package tech.qmates.openchat.database;

import org.sqlite.SQLiteException;
import tech.qmates.openchat.domain.entity.Post;
import tech.qmates.openchat.domain.repository.PostRepository;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

public class SQLitePostRepository extends SQLiteRepository implements PostRepository {

    public SQLitePostRepository(String sqliteFilepath) {
        super(sqliteFilepath);
    }

    public void store(Post post) {
        try (Connection connection = openConnection()) {
            PreparedStatement query = connection.prepareStatement(
                "INSERT INTO posts(id, userId, text, dateTime) VALUES (?,?,?,?)"
            );
            query.setString(1, post.id().toString());
            query.setString(2, post.userId().toString());
            query.setString(3, post.text());
            query.setString(4, serializeDateTime(post.dateTime()));
            query.executeUpdate();
        } catch (SQLiteException e) {
            if (e.getMessage().startsWith("[SQLITE_CONSTRAINT_PRIMARYKEY]"))
                throw new RuntimeException("Cannot store post, id value already used.", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Set<Post> getAllByUser(UUID userId) {
        try (Connection connection = openConnection()) {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM posts WHERE userId = ?");
            query.setString(1, userId.toString());

            ResultSet resultSet = query.executeQuery();
            return mapResultSet((ResultSet row) -> new Post(
                UUID.fromString(row.getString("id")),
                UUID.fromString(row.getString("userId")),
                row.getString("text"),
                deserializeDateTime(row.getString("dateTime"))
            ), resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void reset() {
        try (Connection connection = openConnection()) {
            Statement query = connection.createStatement();
            query.executeUpdate("DELETE FROM posts");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String serializeDateTime(ZonedDateTime zonedDateTime) {
        ZonedDateTime utcDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        return utcDateTime.toString();
    }

    private ZonedDateTime deserializeDateTime(String timestampString) {
        return ZonedDateTime.parse(timestampString);
    }

}
