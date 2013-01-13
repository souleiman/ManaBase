package com.hxdcml.protocol;

import com.hxdcml.lang.Constant;
import com.hxdcml.sql.SQLite;
import com.hxdcml.wrapper.SQLProcedure;
import com.hxdcml.wrapper.YugiohWrapper;

import java.sql.SQLException;

/**
 * User: Souleiman Ayoub
 * Date: 1/2/13
 * Time: 7:45 PM
 */
public class YugiohProtocol extends Protocol {
    private SQLProcedure procedure;

    protected YugiohProtocol() throws SQLException {
        procedure = new YugiohWrapper(new SQLite(Constant.TYPE_YUGIOH));
    }

    /**
     * Requests to search the Database based on the message
     *
     *
     * @param message Contains details on what to search
     * @return a String that may contain the search result.
     */
    @Override
    protected String requestSearch(ProtocolMessage message) {
        return null;
    }

    /**
     * Requests an update on a specific detail based on the message given.
     *
     *
     * @param message contains detail on what to search
     * @return TODO: Figure it out.
     */
    @Override
    protected String requestUpdate(ProtocolMessage message) {
        return null;
    }

    /**
     * Requests a delete on a specific detail based on the message given.
     *
     *
     * @param message contains detail on what to delete
     * @return TODO: Figure it out.
     */
    @Override
    protected String requestDelete(ProtocolMessage message) {
        return null;
    }

    /**
     * Requests a random search
     *
     * @return a String that contains the random result.
     */
    @Override
    protected String requestRandom() throws SQLException {
        return null;
    }
}
