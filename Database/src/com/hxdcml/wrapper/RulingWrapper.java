package com.hxdcml.wrapper;

import com.hxdcml.sql.SQLEntities;
import com.hxdcml.sql.SQLMap;
import com.hxdcml.sql.SQLite;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.hxdcml.lang.Constant.*;

/**
 * User: Souleiman Ayoub
 * Date: 12/25/12
 * Time: 8:19 PM
 */
public class RulingWrapper extends SQLWrapper {
    //Table Name
    public static final String TABLE_NAME = "RULING";

    protected RulingWrapper() {
    }

    /**
     * Creates a table
     */
    @Override
    protected void createTable() throws SQLException {
        String reference =
                R_NAME + ") REFERENCES " + MagicWrapper.TABLE_NAME + "(" + NAME + ") " +
                        "ON UPDATE CASCADE ON DELETE CASCADE";
        PreparedStatement statement = MagicWrapper.lite.getConnection().prepareStatement(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        R_NAME + " STRING,"+
                        DATE + " STRING," +
                        RULING + " STRING," +
                        "FOREIGN KEY ("+ reference +
                ")"
        );
        statement.executeUpdate();
        statement.close();
    }

    /**
     * Creates an INSERT SQL Operation based on the Wrapper
     * The values key should contain the following, The first value will contain the Key
     * needed to modify, second value will contain the index of the ArrayList to manipulate,
     * if an ArrayList exist.
     *
     * @param entities are Objects that contains the data that will be inserted into the
     *                 database.
     * @return a String that represents the INSERT SQL Operation
     */
    @Override
    protected synchronized PreparedStatement inject(SQLEntities entities) throws SQLException {
        String column = String.format(" (%s, %s, %s)", R_NAME, DATE, RULING);
        return MagicWrapper.lite.getConnection().prepareStatement(
                "INSERT INTO " + TABLE_NAME + column +
                        " VALUES (?, ?, ?)"
        );
    }

    /**
     * Injects the data into the database.
     *
     * @param entities the SQLEntities that contain information necessary to be inserted to
     *                 the database.
     */
    @Override
    public synchronized void insert(SQLEntities entities) throws SQLException {
        SQLMap map = entities.getEntities();
        PreparedStatement statement = inject(entities);
        String r_key = MagicWrapper.getLastInsertedRowValue();
        if (map.isEmpty()) {
            statement.setString(1, r_key);
            statement.setString(2, null);
            statement.setString(3, null);
            statement.executeUpdate();
            statement.close();
            return;
        }

        for (String key : map.keySet()) {
            Object o = map.get(key);
            if (o instanceof ArrayList) {
                ArrayList<String> list = (ArrayList<String>) o;
                for (String value : list) {
                    statement.setString(1, r_key);
                    statement.setString(2, key);
                    statement.setString(3, value);
                    statement.executeUpdate();
                }
            } else {
                statement.setString(1, r_key);
                statement.setString(2, key);
                statement.setString(3, (String) o);
                statement.executeUpdate();
            }
        }
        statement.close();
    }

    public static void main(String[] args) throws SQLException {
        SQLProcedure procedure = new MagicWrapper(new SQLite());
        boolean b = procedure.delete("Sleep");
        System.out.println(b);
        procedure.close();
    }
}
