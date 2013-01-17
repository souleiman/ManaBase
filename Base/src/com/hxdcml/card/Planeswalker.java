package com.hxdcml.card;

import com.hxdcml.lang.Constant;
import com.hxdcml.sql.SQLMap;

/**
 * User: Souleiman Ayoub
 * Date: 12/23/12
 * Time: 1:25 PM
 */
public class Planeswalker extends Card {
    protected String loyalty;

    /**
     * Check if the card is a permanent
     * It would be permanent if the card is not an instant or sorcery.
     *
     * @return <i>true</i> - if it's permanent; otherwise, <i>false</i>.
     */
    @Override
    public boolean isPermanent() {
        return true;
    }

    /**
     * Check if the card is a planeswalker.
     * It would be a planeswalker if it contained "planeswalker" in the BasicCard Type
     *
     * @return <i>true</i> - if it's a planeswalker; otherwise, <i>false</i>.
     */
    @Override
    public boolean isPlaneswalker() {
        return true;
    }

    /**
     * Sets the loyalty of the Planeswalker
     *
     * @param loyalty of the planeswalker
     */
    public void setLoyalty(String loyalty) {
        this.loyalty = loyalty;
    }

    /**
     * @return The loyalty of the current planeswalker instance
     */
    public String getLoyalty() {
        return loyalty;
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
        SQLMap map = super.getEntities();
        map.put(Constant.LOYALTY, loyalty);
        return map;
    }

    @Override
    public String toString() {
        return "Planeswalker{" +
                "loyalty='" + loyalty + '\'' +
                '}' +
                super.toString();
    }
}