package com.hxdcml.wrapper;

import com.hxdcml.card.Card;
import com.hxdcml.sql.QueryMap;
import com.hxdcml.sql.SQLEntities;
import com.hxdcml.sql.UpdateMap;

import java.sql.SQLException;

/**
 * User: Souleiman Ayoub
 * Date: 1/2/13
 * Time: 5:59 PM
 */
public interface SQLProcedure {

    /**
     * Injects the data into the database.
     *
     * @param entities the SQLEntities that contain information necessary to be inserted to
     *                 the database.
     */
    public void insert(SQLEntities entities) throws SQLException;

    /**
     * Deletes a row based on the primary/reference name
     *
     * @param name the row that will be deleted that is associated with this value.
     * @return true, if successfully deleted, otherwise false.
     */
    public boolean delete(String name) throws SQLException;

    /**
     * Updates the SQL operation where it is equal to the name and replaced based on
     * the SQLMap values.
     *
     * @param name the row that will be modified that is associated with this value.
     * @param map  values to be changed
     * @return true, if successfully updated, otherwise false.
     */
    public boolean update(String name, UpdateMap map) throws SQLException;

    /**
     * Looks for the data in the database based on the results in the SQLMap
     * @param map query entities that we will use to search in the database
     * @return a ResultSet
     */
    public Card[] query(QueryMap map) throws SQLException;

    /**
     * @return an integer value.
     */
    public int size();

    /**
     * Closes the SQLite class from locking the database
     */
    public void close();
}
