package com.hxdcml.parse;

import com.hxdcml.card.BasicCard;
import com.hxdcml.lang.Constant;

/**
 * User: Souleiman Ayoub
 * Date: 11/18/12
 * Time: 11:10 PM
 */
public final class Parser {
    /**
     * This class should not be instantiated.
     */
    private Parser() {
        throw new AssertionError("By no means should this class be instantiated.");
    }

    /**
     * Based on this method, it should parse the site or wherever necessary to accomplish the retrieval of the data.
     * Whatever means necessary, this is not to be confused with parsing through the database. This is to be used if
     * the card does not exist within the data. In other words, Crawl where necessary.
     *
     * @param name of the card to look up
     * @param type The game type refer to the {@link Constant} to see the types available
     * @return The card object if the card exist, However, null can be returned, if the card does not exist or an
     *         issue arises.
     */
    public static BasicCard[] parse(String name, int type) {
        return (type == Constant.TYPE_MAGIC) ? MParser.parse(name) : YParser.parse(name);
    }
}
