package com.hxdcml.protocol;

/**
 * User: Souleiman Ayoub
 * Date: 1/4/13
 * Time: 11:14 PM
 */
public class ProtocolMessage {
    private String name;
    private Object object;

    /**
     * Used in special cases, returns the name of the card. Mostly used in DELETE/Update
     * @return the name of the Card
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the card we want to DELETE/Update
     * @param name a String...
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the object, usually a map.
     * @return returns an Object that may be a Map of some sort.
     */
    public Object getObject() {
        return object;
    }

    /**
     * Sets the object, which may be a Map or something else. Depends on it's use and Command.
     * @param object sets the Object.
     */
    public void setObject(Object object) {
        this.object = object;
    }
}
