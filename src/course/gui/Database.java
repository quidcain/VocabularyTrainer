package course.gui;

import java.sql.*;
import java.util.HashMap;

/**
 * Created by user on 03.11.2016.
 */
public class Database {
    private static final String DB_URL = "jdbc:h2:./mainDb";
    private static final String LOGIN = "sa";
    private static final String PASSWORD = "";
    private Connection connection;
    Database(){
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Can't get driver: " + e.getMessage());
            System.exit(1);
        }
    }
    public void createConnection(){
        try {
            connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public Connection getConnection() {
        return connection;
    }
    public HashMap<String, String> getAccounts(){
        HashMap<String, String> accounts = new HashMap<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM ACCOUNTS;");) {
            while (resultSet.next())
                accounts.put(resultSet.getString("nickname"), resultSet.getString("password"));
            return accounts;
        } catch (SQLException e) {
            System.out.println("Can't get connection: " + e.getMessage());
            return null;
        }
    }
}
