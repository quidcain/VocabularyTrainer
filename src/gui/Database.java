package gui;

import java.sql.*;
import java.util.HashMap;

/**
 * Created by user on 03.11.2016.
 */
class Database {
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
    private void addNewUser(String nickname, String password) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO accounts (nickname, password) VALUES (?,?);")) {
            statement.setString(1, nickname);
            statement.setString(2, password);
            statement.executeUpdate();
        } catch (SQLException sqlEx) {
            System.out.println(sqlEx.getMessage());
        }
    }
    private void createUserTable(String nickname){
        try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + nickname + " (eng VARCHAR(255), rus VARCHAR(255));")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void createNewUser(String nickname, String password) {
        addNewUser(nickname, password);
        createUserTable(nickname);
    }
    public HashMap<String, String> getVocabulatiry(String nickname){
        HashMap<String, String> entireSessionVocabularity = new HashMap<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + nickname + " ;")) {
            while (resultSet.next())
                entireSessionVocabularity.put(resultSet.getString("eng"), resultSet.getString("rus"));
            return entireSessionVocabularity;
        } catch (SQLException e) {
            //it can't happen
            System.out.println("Can't get connection: " + e.getMessage());
            return null;
        }
    }
    public void deleteAccount(String account) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM accounts where nickname=?")) {
            statement.setString(1, account);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void addWord(String nickname, String eng, String rus) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO " + nickname + " (eng, rus) VALUES (?,?);")) {
            statement.setString(1, eng);
            statement.setString(2, rus);
            statement.executeUpdate();
        } catch (SQLException sqlEx) {
            System.out.println(sqlEx.getMessage());
        }
    }
    //public void deleteWord() {}
}
