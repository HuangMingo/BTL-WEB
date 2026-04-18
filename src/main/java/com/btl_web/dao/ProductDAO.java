package com.btl_web.dao;

import com.btl_web.model.Product;
import db.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductDAO {
    private static final String SQL_FIND_ALL = "SELECT id, name, group_name, segment, size, color, price "
            + "FROM shop_product ORDER BY id";
    private static final String SQL_FIND_BY_ID = "SELECT id, name, group_name, segment, size, color, price "
            + "FROM shop_product WHERE id = ?";
    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS shop_product ("
            + "id TEXT PRIMARY KEY,"
            + "name TEXT NOT NULL,"
            + "group_name TEXT NOT NULL,"
            + "segment TEXT NOT NULL,"
            + "size TEXT NOT NULL,"
            + "color TEXT NOT NULL,"
            + "price NUMERIC(14, 2) NOT NULL)";

    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                products.add(mapProduct(resultSet));
            }
        }
        return Collections.unmodifiableList(products);
    }

    public Product findById(String id) throws SQLException {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return mapProduct(resultSet);
            }
        }
    }

    public void ensureTableExists() throws SQLException {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_CREATE_TABLE)) {
            statement.executeUpdate();
        }
    }

    private Product mapProduct(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getString("id"),
                resultSet.getString("name"),
                resultSet.getString("group_name"),
                resultSet.getString("segment"),
                resultSet.getString("size"),
                resultSet.getString("color"),
                resultSet.getBigDecimal("price"));
    }

    private Connection getConnection() throws SQLException {
        try {
            return JDBCUtil.getConnection();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Không tìm thấy PostgreSQL JDBC driver.", e);
        }
    }
}