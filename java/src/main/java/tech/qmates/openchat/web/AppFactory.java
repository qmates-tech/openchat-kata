package tech.qmates.openchat.web;

import tech.qmates.openchat.database.SQLitePostRepository;
import tech.qmates.openchat.database.SQLiteUserRepository;
import tech.qmates.openchat.domain.UTCClock;
import tech.qmates.openchat.domain.repository.PostRepository;
import tech.qmates.openchat.domain.repository.UserRepository;

import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class AppFactory {

    private static UserRepository userRepositoryInstance;
    private static PostRepository postRepositoryInstance;

    public static UserRepository getUserRepository() {
        if (userRepositoryInstance == null)
            userRepositoryInstance = new SQLiteUserRepository(getSqliteFilePath());

        return userRepositoryInstance;
    }

    public static PostRepository getPostRepository() {
        if (postRepositoryInstance == null)
            postRepositoryInstance = new SQLitePostRepository(getSqliteFilePath());

        return postRepositoryInstance;
    }

    public static void resetRepositories() {
        if (userRepositoryInstance != null)
            userRepositoryInstance.reset();
    }

    public static UTCClock getRealClock() {
        return () -> ZonedDateTime.now(ZoneId.of("UTC"));
    }

    private static String getSqliteFilePath() {
        URL res = AppFactory.class.getClassLoader().getResource("production.db");
        if (res == null)
            throw new RuntimeException("Cannot find sqlite database file!");

        try {
            return res.toURI().getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot load sqlite database file!", e);
        }
    }
}
