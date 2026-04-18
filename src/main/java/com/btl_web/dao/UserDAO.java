package com.btl_web.dao;

import com.btl_web.model.Address;
import com.btl_web.model.User;
import db.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * UserDAO - Pure database access layer for User entity No caching, only direct
 * database operations
 *
 * @author ADMINN
 */
public class UserDAO {
    private static final String SQL_INSERT_USER = "INSERT INTO \"user\" (username, fullname, password, base_address) VALUES (?, ?, ?, ?)";
    private static final String SQL_EXISTS_BY_USERNAME = "SELECT 1 FROM \"user\" WHERE username = ?";
    private static final String SQL_FIND_BY_USERNAME = "SELECT * FROM \"user\" WHERE username = ?";
    private static final String SQL_LOGIN = "SELECT * FROM public.\"user\" WHERE username = ? and password = ?";
    private String SQL_UPDATE_PROFILE = "UPDATE \"user\" SET fullname = ?, age = ?, gender = ?, email = ?, phone = ?, base_address = ? WHERE username = ?";
    //Kiem tra trung username
    public User selectByUserName(String userName) throws SQLException, ClassNotFoundException {
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM public.\"user\" WHERE username = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return mapUser(rs);
        }
        return null;
    }

    //Kiem tra trung username va password
    public User selectByUserNameAndPassword(String userName, String password) throws SQLException, ClassNotFoundException {
        Connection con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM \"user\" WHERE username = ? AND password = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return mapUser(rs);
        }
        return null;
    }

    public boolean register(String username, String fullName, String password) throws SQLException, ClassNotFoundException {
        if (selectByUserName(username) != null) {
            return false;
        }
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT_USER);
        statement.setString(1, username);
        statement.setString(2, fullName);
        statement.setString(3, password);
        statement.setString(4, "");
        return statement.executeUpdate() > 0;
    }

    public User login(String username, String password) throws SQLException, ClassNotFoundException {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement statement = con.prepareStatement(SQL_LOGIN);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            return null;
        }
        return mapUser(resultSet);
    }

    public User mapUser(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        AddressDAO addressDAO = new AddressDAO();
        String username = resultSet.getString("username");
        String fullName = resultSet.getString("fullname");
        String password = resultSet.getString("password");
        int age = resultSet.getInt("age");
        String gender = resultSet.getString("gender");
        String email = resultSet.getString("email");
        String phone = resultSet.getString("phone");
        String baseAddress = resultSet.getString("base_address");
        ArrayList<Address> addresses = addressDAO.getAllAddressByUsername(username);
        Address defaultAddress = addressDAO.getAddressById(resultSet.getInt("default_address"));
        User user = new User(username, fullName, password);
        user.setAge(age);
        user.setGender(gender);
        user.setEmail(email);
        user.setPhone(phone);
        user.setBaseAddress(baseAddress);
        user.setDefaultAddress(defaultAddress);
        return user;
    }

    //Cập nhật thông tin người dùng
    public User updateProfile(String username, String fullName, int age, String gender, String email, String phone, String baseAddress) throws SQLException, ClassNotFoundException {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement statement = con.prepareStatement(SQL_UPDATE_PROFILE);
        statement.setString(1, fullName);
        statement.setInt(2, age);
        statement.setString(3, gender);
        statement.setString(4, email);
        statement.setString(5, phone);
        statement.setString(6, baseAddress);
        statement.setString(7, username);
        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            return selectByUserName(username);
        }
        return null;
    }

    // Cập nhật địa chỉ mặc định
    public boolean setDefaultAddress(String username, int addressId) throws SQLException, ClassNotFoundException {
        Connection con = JDBCUtil.getConnection();
        String sql = "UPDATE \"user\" SET default_address = ? WHERE username = ?";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setInt(1, addressId);
        statement.setString(2, username);
        int rowsUpdated = statement.executeUpdate();
        return rowsUpdated > 0;
    }

    // Xóa địa chỉ mặc định (đặt thành NULL)
    public boolean clearDefaultAddress(String username) throws SQLException, ClassNotFoundException {
        Connection con = JDBCUtil.getConnection();
        String sql = "UPDATE \"user\" SET default_address = NULL WHERE username = ?";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setString(1, username);
        int rowsUpdated = statement.executeUpdate();
        return rowsUpdated > 0;
    }
}
