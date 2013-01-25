package com.hxdcml.sql;


import com.hxdcml.lang.Constant;

import java.sql.*;

/**
 * User: Souleiman Ayoub
 * Date: 12/24/12
 * Time: 1:39 PM
 */
public class SQLite {
    protected Connection connection;
    protected Statement statement;

    /**
     * Instantiates connection and statement to be used
     */
    public SQLite() {
        try {
            Class.forName("org.sqlite.JDBC");
            String filename = Constant.DATABASE_FILE_MAGIC;
            connection = DriverManager.getConnection("jdbc:sqlite:" + filename);
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate("PRAGMA FOREIGN_KEYS=ON");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Get the last row id that we recently inserted into.
     */
    public int getLastInsertRow() {
        try {
            ResultSet set = statement.getGeneratedKeys();
            int row = set.getInt("LAST_INSERT_ROWID()");
            return row == 0 ? -1 : row;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Executes the SQL query and returns the result
     *
     * @param sql the query that we want to find
     * @return the result
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        return statement.executeQuery(sql);
    }

    /**
     * Executes an update.
     *
     * @param sql the SQL command
     * @return value for row count, 0 for nothing, and -1 for something went wrong
     */
    public int executeUpdate(String sql) throws SQLException {
        return statement.executeUpdate(sql);
    }

    /**
     * @return the statement that can be used to further manipulate sql commands
     */
    public Statement getStatement() {
        return statement;
    }

    /**
     * @return the connection that can be used if needed.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Closes the SQLite Stream
     */
    public void close() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException ignored) {
        }
    }
}
