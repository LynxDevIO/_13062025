package pucgo.poobd._13062025.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseFactory {

    private static final String URL = "jdbc:sqlite:atividade_db";

    private DatabaseFactory() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL não encontrado.", e);
        }

        Properties props = new Properties();
        props.setProperty("ssl", "false");

        return DriverManager.getConnection(URL, props);
    }
}