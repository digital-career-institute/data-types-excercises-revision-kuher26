import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter database password:");
        String password = scanner.nextLine();
        DBConnection.setPassword(password);
        Connection connection = DBConnection.getConnection();

        if (connection != null) {
            try {
                Statement stmt = connection.createStatement();

                // Create table if not exists
                stmt.executeUpdate(sqlCreateTable);

                // Insert data into table
                insertDataIntoTable(stmt);

                // Select data from table
                System.out.println("Invoices with status = 'NEW':");
                selectDataWithNewStatus(stmt);

                // Add internal_id column to the table
                stmt.executeUpdate("ALTER TABLE Invoices ADD internal_id VARCHAR(255);");

                // Update internal_id for each row
                updateInternalId(stmt);

                // Close resources
                stmt.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    static final String sqlCreateTable = "CREATE TABLE IF NOT EXISTS Invoices (\n" +
                                         "id SERIAL PRIMARY KEY,\n" +
                                         "buyer VARCHAR(255),\n" +
                                         "seller VARCHAR(255),\n" +
                                         "value INT,\n" +
                                         "account_number INT,\n" +
                                         "status VARCHAR(20)\n" +
                                         ");";

    public static void insertDataIntoTable(Statement stmt) {
        String sqlInsert = "INSERT INTO Invoices (buyer, seller, value, account_number, status) VALUES\n" +
                           "('Harry Potter', 'Ollivanders', 1000, 2458, 'PAID'),\n" +
                           "('Hermione Granger', 'Weasleys Wizard Wheezes', 500, 1234, 'NEW'),\n" +
                           "('Draco Malfoy', 'Flourish and Blotts', 1600, 5432, 'WAITING'),\n" +
                           "('Luna Lovegood', 'Honeydukes', 9655, 5555, 'PAID');";

        try {
            // Execute SQL statement to insert data
            stmt.executeUpdate(sqlInsert);
            System.out.println("Records inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void selectDataWithNewStatus(Statement stmt) {
        String sqlSelect = "SELECT * FROM Invoices WHERE status = 'NEW';";

        try {
            // Execute SQL statement to select data
            java.sql.ResultSet rs = stmt.executeQuery(sqlSelect);

            // Process the result set
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                                   ", Buyer: " + rs.getString("buyer") +
                                   ", Seller: " + rs.getString("seller") +
                                   ", Value: " + rs.getInt("value") +
                                   ", Account Number: " + rs.getInt("account_number") +
                                   ", Status: " + rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateInternalId(Statement stmt) {
        String sqlUpdate = "UPDATE Invoices SET internal_id =\n" +
                           "CASE \n" +
                           "    WHEN buyer = 'Harry Potter' THEN 'c3e67fd5-8af8-429d-b36c-cb5b29f746bb'\n" +
                           "    WHEN buyer = 'Hermione Granger' THEN 'ce430b99-db23-42ff-9a26-06bec646bf94'\n" +
                           "    WHEN buyer = 'Draco Malfoy' THEN '8dd46fe0-0364-43f5-85d6-ee2c592ac1f6'\n" +
                           "    WHEN buyer = 'Luna Lovegood' THEN '1d46bbbe-b01c-4b26-99d2-d38f7a8128b8'\n" +
                           "END;";

        try {
            // Execute SQL statement to update data
            stmt.executeUpdate(sqlUpdate);
            System.out.println("Internal IDs updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
