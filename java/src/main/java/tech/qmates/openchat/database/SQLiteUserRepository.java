package tech.qmates.openchat.database;

import tech.qmates.openchat.domain.entity.RegisteredUser;
import tech.qmates.openchat.domain.entity.UserToRegister;
import tech.qmates.openchat.domain.repository.UserRepository;

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
            query.setString(3, user.password());
            query.setString(4, user.about());
            query.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isUsernameAlreadyUsed(String username) {
        return false;
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
    public void reset() {
        try (Connection connection = openConnection()) {
            Statement query = connection.createStatement();
            query.executeUpdate("DELETE FROM users");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
