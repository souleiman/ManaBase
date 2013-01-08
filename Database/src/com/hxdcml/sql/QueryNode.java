package com.hxdcml.sql;

/**
 * User: Souleiman Ayoub
 * Date: 12/30/12
 * Time: 3:28 PM
 */
public class QueryNode {
    private String value;
    private boolean exact;

    /**
     * These values inserted will function as search helpers, Where value is the value that
     * is being compared by, and based on the boolean value, whether to search for exact or
     * exhaust.
     *
     * @param value comparison value to search by.
     * @param exact true if to search by exact, otherwise false for exhaustive search.
     */
    public QueryNode(String value, boolean exact) {
        this.value = value;
        this.exact = exact;
    }

    public String getValue() {
        return value;
    }
    public boolean isExact(){
        return exact;
    }
    public boolean isExhaust(){
        return !exact;
    }

    @Override
    public String toString() {
        return "QueryNode{" +
                "value='" + value + '\'' +
                ", exact=" + exact +
                '}';
    }
}
