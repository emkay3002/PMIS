package com.example.backup;

import java.sql.*;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/pmis";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public ResultSet getInmateById(int inmateId) throws SQLException {
        Connection conn = getConnection();
        String query = "SELECT * FROM Inmate WHERE InmateID = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, inmateId);
        return pstmt.executeQuery();
    }

    public ResultSet getAllInmates() throws SQLException {
        Connection conn = getConnection();
        String query = "SELECT * FROM Inmate";
        PreparedStatement pstmt = conn.prepareStatement(query);
        return pstmt.executeQuery();
    }
}
