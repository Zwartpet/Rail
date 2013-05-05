package server.handler.communication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Casper
 */
public class MySQLDB {

    private Connection conn;
    private String user;
    private String pass;
    private String url;
    private static MySQLDB database;

    private MySQLDB() {
        user = "prtserver";
        pass = "mdHpcf5UBEePXjNu";
        url = "jdbc:mysql://192.168.178.10:3306/RailDB";
    }

    public static MySQLDB getInstance() {
        if (database == null) {
            database = new MySQLDB();
        }
        return database;
    }

    public void connect() {
        try {
            conn = DriverManager.getConnection(url, user, pass);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ResultSet query(String query) {
        Statement st;
        ResultSet res = null;
        try {
            st = conn.createStatement();
            res = st.executeQuery(query);
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return res;
    }

    public void disconnect() {
        try {
            conn.close();
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }
}

