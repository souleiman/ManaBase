package com.hxdcml.sql;

/**
 * User: Souleiman Ayoub
 * Date: 12/25/12
 * Time: 2:25 PM
 */
public final class SQLBinder {
    /**
     * In order to sanitize the SQL Operation the values provided must meet the requirement,
     * such as Strings cannot contain a single quote, since those need to be escaped.
     *
     * @param value given values to be sanitized
     * @return a String Sanitized Object ready to be used for SQL Operation.
     */
    public static Object format(Object value) {
        if (value instanceof String) {
            value = "\'" + String.valueOf(value).replaceAll("\'", "\'\'") + "\'";
        }
        return value;
    }

    public static String containsFormat(String value) {
        String temp = "%" + value + "%";
        return (String) format(temp);
    }

    public static String exhaustFormat(String value) {
        String temp = "%";
        for (char c : value.toCharArray()) {
            temp += c + "%";
        }
        return (String) format(temp);
    }
}
