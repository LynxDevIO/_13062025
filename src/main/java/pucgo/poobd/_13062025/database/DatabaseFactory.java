package pucgo.poobd._13062025.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseFactory {

    private static final String URL = "jdbc:sqlite:atividade.db";
    private static Connection connection;

    private DatabaseFactory() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver SQLite não encontrado.", e);
            }

            Properties props = new Properties();
            props.setProperty("ssl", "false");

            connection = DriverManager.getConnection(URL, props);
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}