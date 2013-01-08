package com.hxdcml.wrapper;

import com.hxdcml.lang.Constant;
import com.hxdcml.sql.SQLEntities;
import com.hxdcml.sql.SQLMap;
import com.hxdcml.sql.SQLite;
import com.hxdcml.sql.UpdateMap;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.hxdcml.lang.Constant.*;

/**
 * User: Souleiman Ayoub
 * Date: 12/25/12
 * Time: 8:00 PM
 */
public class FormatWrapper extends SQLWrapper {
    //Table Name
    public static final String TABLE_NAME = "FORMAT";

    protected FormatWrapper() {
    }

    /**
     * Create a table
     */
    @Override
    protected void createTable() throws SQLException {
        String reference =
                R_NAME + ") REFERENCES " + MagicWrapper.TABLE_NAME + "(" + NAME + ") " +
                        "ON UPDATE CASCADE ON DELETE CASCADE";
        PreparedStatement statement = MagicWrapper.lite.getConnection().prepareStatement(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        R_NAME + " STRING," +
                        STANDARD + " STRING NOT NULL," +
                        EXTENDED + " STRING NOT NULL," +
                        MODERN + " STRING NOT NULL," +
                        LEGACY + " STRING NOT NULL," +
                        VINTAGE + " STRING NOT NULL," +
                        CLASSIC + " STRING NOT NULL," +
                        COMMANDER + " STRING NOT NULL," +
                        "FOREIGN KEY (" + reference +
                        ")"
        );
        statement.executeUpdate();
        statement.close();
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
        String column = String.format(" (%s, %s, %s, %s, %s, %s, %s, %s)",
                R_NAME, VINTAGE, LEGACY, EXTENDED, STANDARD, CLASSIC, COMMANDER, MODERN);
        return MagicWrapper.lite.getConnection().prepareStatement(
                "INSERT INTO " + TABLE_NAME + column +
                        " VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        );
    }

    private void check(SQLMap map) {
        String[] format_list = {VINTAGE, LEGACY, EXTENDED, STANDARD, CLASSIC, COMMANDER,
                MODERN};
        for (String format : format_list) {
            Object o = map.get(format);
            if (o == null) {
                map.put(format, "N/A");
            }
        }
    }

    /**
     * Injects the data into the database.
     *
     * @param entities the SQLEntities that contain information necessary to be inserted to
     *                 the database.
     */
    @Override
    public void insert(SQLEntities entities) throws SQLException {
        SQLMap map = entities.getEntities();
        check(map);

        PreparedStatement statement = inject(entities);
        statement.setString(1, MagicWrapper.getLastInsertedRowValue());
        statement.setString(2, (String) map.get(VINTAGE));
        statement.setString(3, (String) map.get(LEGACY));
        statement.setString(4, (String) map.get(EXTENDED));
        statement.setString(5, (String) map.get(STANDARD));
        statement.setString(6, (String) map.get(CLASSIC));
        statement.setString(7, (String) map.get(COMMANDER));
        statement.setString(8, (String) map.get(MODERN));
        statement.executeUpdate();
        statement.close();
    }

    public static void main(String[] args) throws SQLException {
        MagicWrapper wrapper = new MagicWrapper(new SQLite(TYPE_MAGIC));
        UpdateMap map = new UpdateMap();
        map.put(Constant.ID, "1337");
        map.put(Constant.NAME, "Fuck Ya.");
        wrapper.update("Sleepy", map);
        wrapper.close();
    }
}
