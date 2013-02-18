package com.hxdcml.protocol;

/**
 * User: Souleiman Ayoub
 * Date: 1/4/13
 * Time: 11:12 PM
 */
public class ProtocolData {
    public static final int MAGIC = 0;

    public static final int SEARCH = 0;
    public static final int UPDATE = 1;
    public static final int DELETE = 2;
    public static final int SIZE = 3;
    public static final int RANDOM = 4;

    private int command;
    private ProtocolMessage message;

    /**
     * @param command the type of command to deal with
     */
    public ProtocolData(int command) {
        this.command = command;
    }

    /**
     * @param message sets the Protocol Message. This contains the fine detail for the
     *                SEARCH/UPDATE/DELETE.
     */
    public void setMessage(ProtocolMessage message) {
        this.message = message;
    }

    /**
     * @return a ProtocolMessage that contains the details of the command
     */
    public ProtocolMessage getMessage() {
        return message;
    }

    /**
     * @return true, iff the command is equal to SEARCH, otherwise false.
     */
    public boolean isSearch() {
        return command == SEARCH;
    }

    /**
     * @return true, iff the command is equal to RANDOM, otherwise false.
     */
    public boolean isRandom() {
        return command == RANDOM;
    }

    /**
     * @return true, iff the command is equal to UPDATE, otherwise false.
     */
    public boolean isUpdate() {
        return command == UPDATE;
    }

    /**
     * @return true, iff the command is equal to DELETE, otherwise false.
     */
    public boolean isDelete() {
        return command == DELETE;
    }

    /**
     * @return true, iff the command is equal to SIZE, otherwise false.
     */
    public boolean isSize() {
        return command == SIZE;
    }
}
