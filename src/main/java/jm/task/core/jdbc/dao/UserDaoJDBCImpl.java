package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.beans.BeanProperty;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.connection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS mydbtest" +
                    "(id mediumint not null auto_increment," +
                    " name VARCHAR(50), " +
                    "lastname VARCHAR(50), " +
                    "age tinyint, " +
                    "PRIMARY KEY (id))");
            System.out.println("Таблица создана");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void dropUsersTable() {
        try (Connection connection = Util.connection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate( "Drop table if exists mydbtest");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String SQL = "INSERT INTO mydbtest(name, lastName, age) VALUES (?, ?, ?)";
        try (Connection connection = Util.connection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        String SQL = "DELETE FROM mydbtest where (?)";
        Connection connection = Util.connection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException a) {
                e.printStackTrace();
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String SQL = "SELECT id, name, lastName, age from mydbtest";
        try (Connection connection = Util.connection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                User user = new User();
                user.setId((long) resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setAge((byte) resultSet.getInt("age"));
                user.setLastName(resultSet.getString("lastName"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  users;
    }
    public void cleanUsersTable() {
        try (Connection connection = Util.connection()){
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM mydbtest WHERE id > 0");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
