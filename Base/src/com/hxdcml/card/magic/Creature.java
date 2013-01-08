package com.hxdcml.card.magic;

import com.hxdcml.lang.Constant;
import com.hxdcml.sql.SQLMap;

/**
 * User: Souleiman Ayoub
 * Date: 12/23/12
 * Time: 6:58 PM
 */
public class Creature extends Card {
    protected String power;
    protected String toughness;

    /**
     * Check if the card is a creature.
     * It would be a creature if it contained "creature" in the BasicCard Type
     *
     * @return <i>true</i> - if it's a creature; otherwise, <i>false</i>.
     */
    @Override
    public boolean isCreature() {
        return true;
    }
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

    public String getPower() {
        return power;
    }
    public void setPower(String power) {
        this.power = power;
    }

    public String getToughness() {
        return toughness;
    }
    public void setToughness(String toughness) {
        this.toughness = toughness;
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
        map.put(Constant.POWER, power);
        map.put(Constant.TOUGHNESS, toughness);
        return map;
    }

    @Override
    public String toString() {
        return "Creature{" +
                "power='" + power + '\'' +
                ", toughness='" + toughness + '\'' +
                '}'
                + super.toString();
    }
}
