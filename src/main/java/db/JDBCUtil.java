package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class JDBCUtil {


    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        // Viết trực tiếp thông tin vào đây cho gọn
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/btl_web";
        String user = "postgres";
        String password = "minh1200"; // Nhớ thay password thật của bạn vào đây

        return DriverManager.getConnection(url, user, password);
    }
}