package com.foocmend.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ChartDao {

    public ArrayList<String> list() {

        String url = "jdbc:mysql://localhost:3306/foocmend";
        String username = "foocmend";
        String password = "_aA123456";
        ArrayList<String> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()){

            String sql = "SELECT count(case when favoriteFoods like '%KOREA%' then 1 end) AS 한식," +
                    "count(case when favoriteFoods like '%AMERICA%' then 1 end) AS 양식," +
                    "count(case when favoriteFoods like '%japan%' then 1 end) AS 일식 FROM member where gender='male'";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                list.add(rs.getString("한식"));
                list.add(rs.getString("양식"));
                list.add(rs.getString("일식"));
            }

            rs.close();

        } catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
