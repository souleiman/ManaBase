package com.hxdcml.protocol;

import com.google.gson.Gson;
import com.hxdcml.wrapper.SQLProcedure;

import java.sql.SQLException;

/**
 * User: Souleiman Ayoub
 * Date: 1/2/13
 * Time: 6:31 PM
 */
public abstract class Protocol {
    protected SQLProcedure procedure;
    protected Gson gson;

    /**
     * This will help the server interact with the client protocol. Based on the input,
     * it will return a result.
     *
     *
     *
     * @param data A String that will contain the command that will be used to process
     *                the message and return a result.
     * @param message A String that will contain something to help us manipulate with the
     *                command.
     * @return a String from the Server that will be sent to the client.
     */
    public String process(ProtocolData data, ProtocolMessage message) throws SQLException {
        System.out.print("COMMAND REQUESTED: ");
        if (data.isSearch()) {
            System.out.println("SEARCH");
            return requestSearch(message);
        } else if (data.isUpdate()) {
            System.out.println("UPDATE");
            return requestUpdate(message);
        } else if (data.isDelete()) {
            System.out.println("DELETE");
            return requestDelete(message);
        } else if (data.isSize()) {
            System.out.println("SIZE");
            return requestSize();
        } else if (data.isRandom()) {
            System.out.println("RANDOM");
            return requestRandom();
        } else {
            System.out.println("UNKNOWN!");
            return "[UNKNOWN COMMAND: " + data + "]";
        }
    }

    /**
     * Requests a random search
     * @return a String that contains the random result.
     */
    protected abstract String requestRandom() throws SQLException;

    /**
     * Requests to search the Database based on the message
     *
     * @param message Contains details on what to search
     * @return a String that may contain the search result.
     */
    protected abstract String requestSearch(ProtocolMessage message) throws SQLException;

    /**
     * Requests an update on a specific detail based on the message given.
     *
     *
     * @param message contains detail on what to search
     * @return TODO: Figure it out.
     */
    protected abstract String requestUpdate(ProtocolMessage message) throws SQLException;

    /**
     * Requests a delete on a specific detail based on the message given.
     *
     *
     * @param message contains detail on what to delete
     * @return TODO: Figure it out.
     */
    protected abstract String requestDelete(ProtocolMessage message) throws SQLException;

    /**
     * Request the size of the database
     *
     * @return Size of the database
     */
    protected String requestSize() {
        int size = procedure.size();
        procedure.close();
        return gson.toJson(size);
    }

    /**
     * Closes the procedure stream.
     */
    private void close() {
        if(procedure != null)
            procedure.close();
    }

    /**
     * Processes the String input and formats it from Json to ProtocolData instance,
     * to furthermore help process the data.
     *
     * @param input Given by the client as a Json String
     * @return a Json String that displays the result
     */
    public static String process(String input) throws SQLException {
        ProtocolData pd = new Gson().fromJson(input, ProtocolData.class);
        Protocol protocol;
        if (pd.isMagic()) {
            protocol = new MagicProtocol();
        } else {
            protocol = new YugiohProtocol();
        }
        String process = protocol.process(pd, pd.getMessage());
        protocol.close();
        return process;
    }
}
