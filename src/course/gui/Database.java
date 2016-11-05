package course.gui;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 03.11.2016.
 */
public class Database {
    private static final String DB_URL = "jdbc:h2:./mainDb";
    private static final String LOGIN = "sa";
    private static final String PASSWORD = "";
    private Connection connection;
    Database() throws ClassNotFoundException {
        Class.forName("org.h2.Driver");
    }
    public void createConnection() throws SQLException{
        connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
    }
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        return connection;
    }
    public HashMap<String, String> getAccounts(){
        HashMap<String, String> accounts = new HashMap<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM ACCOUNTS;")) {
            while (resultSet.next())
                accounts.put(resultSet.getString("nickname"), resultSet.getString("password"));
            return accounts;
        } catch (SQLException e) {
            //it can't happen
            System.out.println("Can't get connection: " + e.getMessage());
            return null;
        }
    }
    public void createUserTable(String nickname){
        try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + nickname + " (eng VARCHAR(255), rus VARCHAR(255));")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private int getUserWordsNumber(String nickname) {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + nickname + " ACCOUNTS;")) {
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            //it can't happen
            System.out.println("Can't get connection: " + e.getMessage());
            return 0;
        }
    }
    public ArrayList<WordsPair> getVocabulatiry(String nickname){
        ArrayList<WordsPair> entireSessionVocabularity = new ArrayList<>(getUserWordsNumber(nickname));
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + nickname + " ;")) {
            while (resultSet.next()) {
                WordsPair wordsPair = new WordsPair(resultSet.getString("eng"), resultSet.getString("rus"));
                entireSessionVocabularity.add(wordsPair);
            }
            return entireSessionVocabularity;
        } catch (SQLException e) {
            //it can't happen
            System.out.println("Can't get connection: " + e.getMessage());
            return null;
        }
    }

}
