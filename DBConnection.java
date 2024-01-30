import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DBConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "postgres";
    private static String PASSWORD;

    public static void setPassword(String password) {
        PASSWORD = password;
    }

    // Establishing database connection
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.setAutoCommit(false); // Disable auto-commit to manage transactions manually
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // Closing database connection
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
