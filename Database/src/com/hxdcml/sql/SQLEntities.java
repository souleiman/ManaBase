package com.hxdcml.sql;

/**
 * User: Souleiman Ayoub
 * Date: 12/27/12
 * Time: 1:16 PM
 */
public interface SQLEntities {
    /**
     * Based on the class that inherits this interface, it must provide the entities that
     * can be used in order to gather the data entities for it's instance field.
     *
     * @return a HashMap where the Key will be the entity type and the values will contain
     * the data associated with the key.
     */
    public SQLMap getEntities();
}
