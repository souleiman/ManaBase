package com.hxdcml.sql;

/**
 * User: Souleiman Ayoub
 * Date: 12/30/12
 * Time: 3:28 PM
 */
public class QueryNode {
    public static final int EXACT = 0;
    public static final int CONTAINS = 1;
    public static final int EXHAUST = 2;

    private String value;
    private int type;

    /**
     * These values inserted will function as search helpers, Where value is the value that
     * is being compared by, and based on the boolean value, whether to search for exact or
     * exhaust.
     *
     * @param value comparison value to search by.
     * @param type  0 if to search by exact, or 1 for contain, otherwise 2 for exhaustive
     *              search.
     */
    public QueryNode(String value, int type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public boolean isExact() {
        return type == EXACT;
    }

    public boolean isContains() {
        return type == CONTAINS;
    }

    public boolean isExhaust() {
        return type == EXHAUST;
    }

    @Override
    public String toString() {
        return "QueryNode{" +
                "value='" + value + '\'' +
                ", type=" + type +
                '}';
    }
}
