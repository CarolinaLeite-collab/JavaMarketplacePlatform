package MITELOVERS.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserSearchRepository {

    public ResultSet searchByName(Connection conn, String username) throws Exception {
        String sql = "SELECT * FROM users WHERE name = '" + username + "'";
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }
}