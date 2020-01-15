package pijanski.grzegorz.networth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {

    private static final DbConnector INSTANCE = new DbConnector();

    private DbConnector() { }

    public static DbConnector getInstance() {
        return INSTANCE;
    }

    public Connection connect(final String url) throws SQLException {
        return DriverManager.getConnection(url);
    }
}
