package com.hxdcml.card.magic;

import com.hxdcml.sql.SQLEntities;
import com.hxdcml.sql.SQLMap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Souleiman Ayoub
 * Date: 12/27/12
 * Time: 1:32 PM
 */
public class DataMap implements SQLEntities {
    protected HashMap<String, Object> map;

    public DataMap() {
        map = new SQLMap();
    }

    /**
     * Puts a value based on the key value.
     * Due to keys may containing multiple values, we must first check if the key already
     * exists within the map. If this is true, we will check what kind of object the key
     * has, in this case, an ArrayList, if the instance is an ArrayList,
     * we can conclude that this key has multiple values, and all we need to do is add the
     * new value into the ArrayList. Otherwise, we create a new ArrayList and insert the
     * value that already exists in the Map and add the new value and replace the value with
     * the ArrayList to be associated by the key value. Finally, If the key does not exist
     * in the map, just insert the key.
     *
     * @param key   that will be used to reference the value
     * @param value the data that will be stored
     */
    public void put(String key, String value) {
        if (map.containsKey(key)) {
            Object list = map.get(key);
            if (list instanceof ArrayList) {
                ArrayList<String> e = (ArrayList<String>) list;
                e.add(value);
            } else {
                ArrayList<String> e = new ArrayList<String>();
                e.add(String.valueOf(list));
                e.add(value);
                map.put(key, e);
            }
        } else {
            map.put(key, value);
        }
    }

    /**
     * Based on the class that inherits this interface, it must provide the entities that
     * can be used in order to gather the data entities for it's instance field.
     *
     * @return a HashMap where the Key will be the entity type and the values will contain
     *         the data associated with the key.
     */
    @Override
    public SQLMap getEntities() {
        return (SQLMap) map;
    }

    @Override
    public String toString() {
        return "DataMap{" +
                "map=" + map +
                '}';
    }
}
