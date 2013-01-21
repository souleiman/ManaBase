package com.hxdcml.protocol;

import com.google.gson.GsonBuilder;
import com.hxdcml.card.Card;
import com.hxdcml.sql.QueryMap;
import com.hxdcml.sql.SQLite;
import com.hxdcml.sql.UpdateMap;
import com.hxdcml.wrapper.MagicWrapper;

import java.sql.SQLException;
import java.util.AbstractMap;

/**
 * User: Souleiman Ayoub
 * Date: 1/2/13
 * Time: 7:28 PM
 */
public class MagicProtocol extends Protocol {

    protected MagicProtocol() throws SQLException {
        procedure = new MagicWrapper(new SQLite());
        gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    /**
     * Requests to search the Database based on the message
     *
     * @param message Contains details on what to search
     * @return a String that may contain the search result.
     */
    @Override
    protected String requestSearch(ProtocolMessage message) throws SQLException {
        AbstractMap query = (AbstractMap) message.getObject();
        QueryMap map = new QueryMap(query);
        Card[] cards = procedure.query(map);

        if (cards.length != 0)
            return GsonWrangler.arrayToJson(gson, cards);
        return null;
    }

    /**
     * Requests a random search
     *
     * @return a String that contains the random result.
     */
    @Override
    protected String requestRandom() {
        return null;
    }

    /**
     * Forces an update in the Database, deletes the data from the database and requests an
     * update.
     *
     * @param message contains what to delete and what to update.
     * @return the updated result.
     */
    @Override
    protected String requestForcedUpdate(ProtocolMessage message) throws SQLException {
        return null;
    }

    /**
     * Requests an update on a specific detail based on the message given.
     *
     * @param message contains detail on what to search
     * @return a boolean true if successfully updated, otherwise false. In a String for Json.
     */
    @Override
    protected String requestUpdate(ProtocolMessage message) throws SQLException {
        AbstractMap<String, String> map = (AbstractMap<String, String>) message.getObject();
        UpdateMap update = new UpdateMap(map);
        boolean result = procedure.update(message.getName(), update);
        return gson.toJson(result);
    }

    /**
     * Requests a delete on a specific detail based on the message given.
     *
     * @param message contains detail on what to delete
     * @return a boolean true if successfully updated, otherwise false. In a String for Json.
     */
    @Override
    protected String requestDelete(ProtocolMessage message) throws SQLException {
        String name = message.getName();
        boolean delete = procedure.delete(name);
        return gson.toJson(delete);
    }

    /**
     * Simply inserts the card in the database.
     * @param cards that will be inserted into the database
     */
    private void insert(Card... cards) throws SQLException {
        for (Card card : cards)
            procedure.insert(card);
    }
}