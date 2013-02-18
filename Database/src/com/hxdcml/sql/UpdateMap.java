package com.hxdcml.sql;

import java.util.AbstractMap;
import java.util.Set;

/**
 * User: Souleiman Ayoub
 * Date: 1/1/13
 * Time: 6:52 PM
 */
public class UpdateMap {
    private SQLMap map;

    /**
     * Instantiates the SQLMap
     */
    public UpdateMap() {
        map = new SQLMap();
    }

    /**
     * Inserts all the data inside of the AbstractMap into our Map.
     *
     * @param update the given information that we want to add into our Map.
     */
    public UpdateMap(AbstractMap<String, String> update) {
        this();
        map.putAll(update);
    }

    /**
     * The given key value will indicate a constant, namely the Table Column,
     * that will be updated based on the given value.
     *
     * @param key the column name.
     * @param value the value to change to.
     */
    public void put(String key, String value) {
        map.put(key, value);
    }

    /**
     * Returns the Set of Key Values.
     *
     * @return a Set that contains Strings of Key Values.
     */
    public Set<String> keySet() {
        return map.keySet();
    }

    /**
     * A simple getter method that will access a data in the map.
     *
     * @param key the String key that we want to access in the Map.
     * @return the String that represents the key value.
     */
    public String get(String key) {
        return (String) map.get(key);
    }

    /**
     * Converts SQLMap to support SET operation for SQL
     * @return a String that supports the SET operation.
     */
    public String makeToString() {
        String set = "";
        for (String key : keySet()) {
            String value = (String) SQLBinder.format(map.get(key));
            set += key + " = "+ value + ", ";
        }
        return set.replaceAll(", $", "");
    }
}
