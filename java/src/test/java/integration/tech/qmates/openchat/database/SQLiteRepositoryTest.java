package integration.tech.qmates.openchat.database;

import java.net.URISyntaxException;
import java.net.URL;

abstract class SQLiteRepositoryTest {

    protected String getSqliteFilePath() {
        URL res = getClass().getClassLoader().getResource("integration.test.db");
        if (res == null)
            throw new RuntimeException("Cannot find integration test database file!");

        try {
            return res.toURI().getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot load integration test database file!", e);
        }
    }
}
