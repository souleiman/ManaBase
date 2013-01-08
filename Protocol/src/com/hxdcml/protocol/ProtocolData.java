package com.hxdcml.protocol;

/**
 * User: Souleiman Ayoub
 * Date: 1/4/13
 * Time: 11:12 PM
 */
public class ProtocolData {
    public static final int MAGIC = 0;
    public static final int YUGIOH = 1;

    public static final int SEARCH = 0;
    public static final int UPDATE = 1;
    public static final int DELETE = 2;
    public static final int SIZE = 3;

    private int type;
    private int command;
    private ProtocolMessage message;

    /**
     * @param type an integer value that holds the type of Card we want to deal with
     * @param command the type of command to deal with
     */
    public ProtocolData(int type, int command) {
        this.type = type;
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

    public boolean isMagic() {
        return type == MAGIC;
    }
    public boolean isYugioh() {
        return type == YUGIOH;
    }

    public boolean isSearch() {
        return command == SEARCH;
    }
    public boolean isUpdate() {
        return command == UPDATE;
    }
    public boolean isDelete() {
        return command == DELETE;
    }
    public boolean isSize() {
        return command == SIZE;
    }

    @Override
    public String toString() {
        return "ProtocolData{" +
                "type=" + type +
                ", command=" + command +
                ", message=" + message +
                '}';
    }
}
