package tech.qmates.openchat.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

abstract class SQLiteRepository {

    private final String sqliteFilepath;

    protected SQLiteRepository(String sqliteFilepath) {
        this.sqliteFilepath = sqliteFilepath;
    }

    protected Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + sqliteFilepath);
    }

    protected <T> Set<T> mapResultSet(ResultSetMapper<T> mapFunction, ResultSet resultSet) throws SQLException {
        Set<T> result = new HashSet<>();
        while (resultSet.next()) {
            T mapped = mapFunction.apply(resultSet);
            result.add(mapped);
        }
        return result;
    }

    @FunctionalInterface
    protected interface ResultSetMapper<R> {
        R apply(ResultSet t) throws SQLException;
    }
}
