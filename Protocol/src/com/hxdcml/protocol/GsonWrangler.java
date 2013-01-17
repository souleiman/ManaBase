package com.hxdcml.protocol;

import com.google.gson.Gson;
import com.hxdcml.card.Card;

/**
 * User: Souleiman Ayoub
 * Date: 1/13/13
 * Time: 3:54 PM
 */
public class GsonWrangler {
    /**
     * Due to the frustration in attempting to parse out Json from Polymorphic OOP. We will
     * slightly modify the String formatting to support easier parsing.
     *
     * @param son the gson setting and parser.
     * @param cards that we will use to parse.
     * @return a String in a Slightly similar JSON format.
     */
    public static String arrayToJson(Gson son, Card[] cards) {
        String json = "";
        for (int i = 0; i < cards.length; i++) {
            Card card = cards[i];
            String toJson = son.toJson(card);
            json += toJson;
            if (i == cards.length - 1) {
                break;
            }
            json += "~|~";
        }
        return json;
    }
}
