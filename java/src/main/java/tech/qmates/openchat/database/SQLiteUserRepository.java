package tech.qmates.openchat.database;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import org.sqlite.SQLiteException;
import tech.qmates.openchat.domain.entity.RegisteredUser;
import tech.qmates.openchat.domain.entity.UserToRegister;
import tech.qmates.openchat.domain.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLiteUserRepository implements UserRepository {

    private final String sqliteFilepath;

    public SQLiteUserRepository(String sqliteFilepath) {
        this.sqliteFilepath = sqliteFilepath;
    }

    @Override
    public void store(UserToRegister user) {
        try (Connection connection = openConnection()) {
            PreparedStatement query = connection.prepareStatement(
                "INSERT INTO users(id, username, password, about) VALUES (?,?,?,?)"
            );
            query.setString(1, user.uuid().toString());
            query.setString(2, user.username());
            query.setString(3, sha256Of(user.password()));
            query.setString(4, user.about());
            query.executeUpdate();
        } catch (SQLiteException e) {
            if (e.getMessage().startsWith("[SQLITE_CONSTRAINT_PRIMARYKEY]"))
                throw new RuntimeException("Cannot store user, uuid value already used.", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isUsernameAlreadyUsed(String username) {
        try (Connection connection = openConnection()) {
            PreparedStatement query = connection.prepareStatement(
                "SELECT count(1) as count FROM users WHERE username = ?"
            );
            query.setString(1, username);
            ResultSet resultSet = query.executeQuery();
            return resultSet.getInt("count") > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RegisteredUser> getAll() {
        try (Connection connection = openConnection()) {
            Statement query = connection.createStatement();
            ResultSet resultSet = query.executeQuery("SELECT * FROM users");
            return mapResultSet((ResultSet row) ->
                    new RegisteredUser(
                        UUID.fromString(row.getString("id")),
                        row.getString("username"),
                        row.getString("about")
                    ),
                resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RegisteredUser getUserById(UUID uuid) {
        try (Connection connection = openConnection()) {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
            query.setString(1, uuid.toString());
            ResultSet resultSet = query.executeQuery();
            if (!resultSet.next())
                return null;

            return new RegisteredUser(
                UUID.fromString(resultSet.getString("id")),
                resultSet.getString("username"),
                resultSet.getString("about")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reset() {
        try (Connection connection = openConnection()) {
            Statement query = connection.createStatement();
            query.executeUpdate("DELETE FROM users");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String sha256Of(String password) {
        HashCode hashCode = Hashing.sha256().hashString(password, StandardCharsets.UTF_8);
        return hashCode.toString();
    }

    // ***** TODO move function below in some SQLiteRepository super class ******

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + sqliteFilepath);
    }

    private <T> List<T> mapResultSet(ResultSetMapper<T> mapFunction, ResultSet resultSet) throws SQLException {
        List<T> result = new ArrayList<>();
        while (resultSet.next()) {
            T mapped = mapFunction.apply(resultSet);
            result.add(mapped);
        }
        return result;
    }

    @FunctionalInterface
    interface ResultSetMapper<R> {
        R apply(ResultSet t) throws SQLException;
    }

}
