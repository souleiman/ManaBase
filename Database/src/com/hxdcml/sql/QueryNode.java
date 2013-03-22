package com.hxdcml.sql;

import java.util.Arrays;

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
     *              search or 3 for ALL of them.
     */
    public QueryNode(String value, int type) {
        this.value = value;
        this.type = type;
    }

    /**
     * @return the String that is representing this Node.
     */
    public String getValue() {
        return value;
    }

    /**
     * @return true, if and only if type is equal to EXACT, otherwise false.
     */
    public boolean isExact() {
        return type == EXACT;
    }

    /**
     * @return true, if and only if type is equal to CONTAINS, otherwise false.
     */
    public boolean isContains() {
        return type == CONTAINS;
    }

    /**
     * @return true, if and only if type is equal to EXHAUST, otherwise false.
     */
    public boolean isExhaust() {
        return type == EXHAUST;
    }

    /**
     * @return a String Representation of the current Object.
     */
    @Override
    public String toString() {
        return "QueryNode{" +
                "value='" + value + '\'' +
                ", type=" + type +
                '}';
    }

    public static void main(String[] args) {
        boolean[] a = new boolean[10];
        System.out.println(Arrays.toString(a));
    }
}
