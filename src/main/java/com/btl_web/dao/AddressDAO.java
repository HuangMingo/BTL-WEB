/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.btl_web.dao;

import com.btl_web.model.Address;
import db.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author ADMINN
 */
public class AddressDAO {

    private static final String SQL_INSERT_ADDRESS = "INSERT INTO address (username, recipient_name, recipient_phone, shipping_address) "
            + "VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE_ADDRESS = "UPDATE address SET recipient_name = ?, recipient_phone = ?, shipping_address = ? WHERE id = ? AND username = ?";
    // Thêm địa chỉ mới và trả về ID của địa chỉ vừa thêm
    public int addAddress(String username, String recipientName, String recipientPhone, String shippingAddress)
            throws SQLException, ClassNotFoundException {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(SQL_INSERT_ADDRESS);
        ps.setString(1, username);
        ps.setString(2, recipientName);
        ps.setString(3, recipientPhone);
        ps.setString(4, shippingAddress);
        int rowsInserted = ps.executeUpdate();
        if (rowsInserted > 0) {
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public int updateAddress(int addressId, String username, String recipientName, String recipientPhone, String shippingAddress)
            throws SQLException, ClassNotFoundException {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(SQL_UPDATE_ADDRESS);
        ps.setString(1, recipientName);
        ps.setString(2, recipientPhone);
        ps.setString(3, shippingAddress);
        ps.setInt(4, addressId);
        ps.setString(5, username);
        return ps.executeUpdate();
    }

    public ArrayList<Address> getAllAddressByUsername(String username) throws SQLException, ClassNotFoundException {
        ArrayList<Address> a = new ArrayList<>();
        String sql = "SELECT id, username, recipient_name, recipient_phone, shipping_address "
                + "FROM address WHERE username = ? ORDER BY id";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    a.add(mapAddress(rs));
                }
            }
        }
        return a;
    }

    public Address getAddressById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT id, username, recipient_name, recipient_phone, shipping_address "
                + "FROM address WHERE id = ?";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapAddress(rs);
                }
            }
        }
        return null;
    }

    private Address mapAddress(ResultSet rs) throws SQLException {
        Address address = new Address();
        address.setId(rs.getInt("id"));
        address.setUsername(rs.getString("username"));
        address.setRecipientName(rs.getString("recipient_name"));
        address.setRecipientPhone(rs.getString("recipient_phone"));
        address.setShippingAddress(rs.getString("shipping_address"));
        return address;
    }
}
