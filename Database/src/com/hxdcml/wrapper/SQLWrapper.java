package com.hxdcml.wrapper;

import com.hxdcml.card.Card;
import com.hxdcml.lang.Constant;
import com.hxdcml.sql.QueryMap;
import com.hxdcml.sql.QueryNode;
import com.hxdcml.sql.SQLEntities;
import com.hxdcml.sql.SQLite;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: Souleiman Ayoub
 * Date: 12/24/12
 * Time: 2:29 PM
 */
public abstract class SQLWrapper {

    /**
     * Creates a table
     */
    protected abstract void createTable() throws SQLException;

    /**
     * Creates an INSERT SQL Operation based on the Wrapper
     *
     * @param entities are Objects that contains the data that will be inserted into the
     *                 database.
     * @return a String that represents the INSERT SQL Operation
     */
    protected abstract PreparedStatement inject(SQLEntities entities) throws SQLException;

    /**
     * Injects the data into the database.
     *
     * @param entities the SQLEntities that contain information necessary to be inserted to
     *                 the database.
     */
    protected abstract void insert(SQLEntities entities) throws SQLException;

    public static void main(String[] args) throws SQLException {
        SQLProcedure procedure = new MagicWrapper(new SQLite());
        QueryMap map = new QueryMap();
        map.put(Constant.NAME, new QueryNode("()", false));
        Card[] card = procedure.query(map);
        for (int i = 0; i < card.length; i++) {
            String name = card[i].getName().replaceAll("(.*? \\() | .*?\\)", "");
            System.out.println(name);
        }
    }
}
