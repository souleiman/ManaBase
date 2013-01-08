package com.hxdcml.sql;

import java.util.HashMap;

/**
 * User: Souleiman Ayoub
 * Date: 12/27/12
 * Time: 4:04 PM
 */
public class SQLMap extends HashMap<String, Object> {
    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old
     * value is replaced.
     *
     * The value inputted will go through a SQLBinder.format(Object),
     * in order to make sure that the data inserted is safe.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with <tt>key</tt>.)
     */
    @Override
    public Object put(String key, Object value) {
        return super.put(key, value);
    }
}
