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
        try(Connection connection = openConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO users(id, username, password, about) VALUES (?,?,?,?)"
            );

            statement.setString(1, user.uuid().toString());
            statement.setString(2, user.username());
            statement.setString(3, user.password());
            statement.setString(4, user.about());

            statement.executeUpdate();
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
        try(Connection connection = openConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            List<RegisteredUser> result = new ArrayList<>();
            while (rs.next()) {
                result.add(new RegisteredUser(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("username"),
                    rs.getString("about")
                ));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reset() {
        try(Connection connection = openConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM users");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + sqliteFilepath);
    }

}
