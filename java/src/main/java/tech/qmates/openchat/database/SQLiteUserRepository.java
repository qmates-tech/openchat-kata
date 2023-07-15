package tech.qmates.openchat.database;

import tech.qmates.openchat.domain.entity.RegisteredUser;
import tech.qmates.openchat.domain.entity.UserToRegister;
import tech.qmates.openchat.domain.repository.UserRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SQLiteUserRepository implements UserRepository {

    private final String sqliteFilepath;

    public SQLiteUserRepository(String sqliteFilepath) {
        this.sqliteFilepath = sqliteFilepath;
    }

    @Override
    public void store(UserToRegister user) {
        try {
            Connection connection = openConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS users");
            connection.close();
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
        return null;
    }

    @Override
    public void reset() {

    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + sqliteFilepath);
    }

}
