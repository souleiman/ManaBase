package com.hxdcml.wrapper;

import com.hxdcml.card.BasicCard;
import com.hxdcml.sql.QueryMap;
import com.hxdcml.sql.SQLEntities;
import com.hxdcml.sql.SQLite;
import com.hxdcml.sql.UpdateMap;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: Souleiman Ayoub
 * Date: 1/2/13
 * Time: 8:42 PM
 */
public class YugiohWrapper extends SQLWrapper implements SQLProcedure {
    protected static SQLite lite;

    public YugiohWrapper(SQLite lite) throws SQLException {
        YugiohWrapper.lite = lite;
        createTable();
    }

    /**
     * Deletes a row based on the primary/reference name
     *
     * @param name the row that will be deleted that is associated with this value.
     * @return true, if successfully deleted, otherwise false.
     */
    @Override
    public boolean delete(String name) throws SQLException {
        return false;
    }

    /**
     * Updates the SQL operation where it is equal to the name/r_key and replaced based on
     * the SQLMap values.
     *
     * @param name the row that will be modified that is associated with this value.
     * @param map  values to be changed
     * @return true, if successfully updated, otherwise false.
     */
    @Override
    public boolean update(String name, UpdateMap map) throws SQLException {
        return false;
    }

    /**
     * Looks for the data in the database based on the results in the SQLMap
     *
     * @param map query entities that we will use to search in the database
     * @return a ResultSet
     */
    @Override
    public BasicCard[] query(QueryMap map) throws SQLException {
        return new BasicCard[0];
    }

    /**
     * @return an integer value.
     */
    @Override
    public int size() {
        return 0;
    }

    /**
     * Closes the SQLite class from locking the database
     */
    @Override
    public void close() {
    }

    /**
     * Creates a table
     */
    @Override
    protected void createTable() throws SQLException {
    }

    /**
     * Creates an INSERT SQL Operation based on the Wrapper
     *
     * @param entities are Objects that contains the data that will be inserted into the
     *                 database.
     * @return a String that represents the INSERT SQL Operation
     */
    @Override
    protected PreparedStatement inject(SQLEntities entities) throws SQLException {
        return null;
    }

    /**
     * Injects the data into the database.
     *
     * @param entities the SQLEntities that contain information necessary to be inserted to
     *                 the database.
     */
    @Override
    public void insert(SQLEntities entities) throws SQLException {
    }
}
