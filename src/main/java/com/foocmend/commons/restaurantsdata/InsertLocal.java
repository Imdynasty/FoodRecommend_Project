package com.foocmend.commons.restaurantsdata;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InsertLocal {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/foocmend";
        String username = "foocmend";
        String password = "_aA123456";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT DISTINCT substr(address,1,2) as address  FROM restaurants";
            ResultSet rs = stmt.executeQuery(sql);
            List<String> types = new ArrayList<>();
            while(rs.next()) {
                String type = rs.getString("address");

                types.add(type);
            }
            rs.close();
            System.out.println(types);
            for (int i = 0; i < types.size(); i++) {
                String sql2 = String.format("INSERT INTO local VALUES ('%d', '%s')", i, types.get(i));
                stmt.executeUpdate(sql2);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
