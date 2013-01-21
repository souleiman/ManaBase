package com.hxdcml.card;

import com.hxdcml.lang.Constant;
import com.hxdcml.sql.SQLEntities;
import com.hxdcml.sql.SQLMap;

/**
 * User: Souleiman Ayoub
 * Date: 11/22/12
 * Time: 6:54 PM
 */
public class Card implements SQLEntities {
    protected int id;
    protected String link;
    protected String name;
    protected String type;
    protected String ability;
    protected String mana;

    /**
     * Check if the card is a permanent
     * It would be permanent if the card is not an instant or sorcery.
     *
     * @return <i>true</i> - if it's permanent; otherwise, <i>false</i>.
     */
    public boolean isPermanent() {
        return !isSorcery() && !isInstant();
    }

    /**
     * Check if the card is a land.
     * It would be a land if it contained "Land" in the Card Type
     *
     * @return <i>true</i> - if it's a land; otherwise, <i>false</i>.
     */
    public boolean isLand() {
        return type.contains("Land") && !type.contains("Creature");
    }

    /**
     * Check if the card is a creature.
     * It would be a creature if it contained "creature" in the Card Type
     *
     * @return <i>true</i> - if it's a creature; otherwise, <i>false</i>.
     */
    public boolean isCreature() {
        return type.contains("Creature") || type.contains("Summon");
    }

    /**
     * Check if the card is a artifact.
     * It would be a artifact if it contained "artifact" in the Card Type
     *
     * @return <i>true</i> - if it's a artifact; otherwise, <i>false</i>.
     */
    public boolean isArtifact() {
        return type.startsWith("Artifact");
    }

    /**
     * Check if the card is a enchantment.
     * It would be a enchantment if it contained "enchantment" in the Card Type
     *
     * @return <i>true</i> - if it's a enchantment; otherwise, <i>false</i>.
     */
    public boolean isEnchantment() {
        return type.startsWith("Enchantment");
    }

    /**
     * Check if the card is a sorcery.
     * It would be a sorcery if it contained "Land" in the Card Type
     *
     * @return <i>true</i> - if it's a sorcery; otherwise, <i>false</i>.
     */
    public boolean isSorcery() {
        return type.equals("Sorcery");
    }

    /**
     * Check if the card is a instant.
     * It would be a instant if it contained "instant" in the Card Type
     *
     * @return <i>true</i> - if it's a instant; otherwise, <i>false</i>.
     */
    public boolean isInstant() {
        return type.startsWith("Instant");
    }

    /**
     * Check if the card is a planeswalker.
     * It would be a planeswalker if it contained "planeswalker" in the Card Type
     *
     * @return <i>true</i> - if it's a planeswalker; otherwise, <i>false</i>.
     */
    public boolean isPlaneswalker() {
        return type.startsWith("Planeswalker");
    }

    /**
     * Check if the card is a planes.
     * It would be a plane if it contained "plane" in the Card Type
     *
     * @return <i>true</i> - if it's a plane; otherwise, <i>false</i>.
     */
    public boolean isPlane() {
        return type.startsWith("Plane —");
    }

    /**
     * Check if the card is a scheme.
     * It would be a land if it contained "scheme" in the Card Type
     *
     * @return <i>true</i> - if it's a scheme; otherwise, <i>false</i>.
     */
    public boolean isScheme() {
        return type.equals("Scheme");
    }

    /**
     * Some cards my not have a lore, so we want to check if it does.
     *
     * @return <i>true </i>- if it has lore; otherwise <i>false.</i>
     */
    public boolean hasAbility() {
        return ability != null && !ability.isEmpty();
    }

    /**
     * Get the Multiverse ID of the card.
     *
     * @return an <i>int</i> value.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the value of the multiverse id.
     *
     * @param id <i>int</i> which holds the value of multiverse id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the name of the card
     *
     * @return name of the card in String
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the card
     *
     * @param name String which holds the name of the card.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the card type
     *
     * @return type of the card in String
     */
    public String getType() {
        return type;
    }

    /**
     * Set the card type.
     *
     * @param type String which holds the type of the card.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the card text.
     *
     * @return card text in String
     */
    public String getAbility() {
        return ability;
    }

    /**
     * Sets the lore.
     *
     * @param ability String which holds the lore of the card
     */
    public void setAbility(String ability) {
        this.ability = ability;
    }

    /**
     * Get the cost of the card.
     *
     * @return mana cost in String
     */
    public String getMana() {
        return mana;
    }

    /**
     * Set the mana mana
     *
     * @param mana String which holds the mana mana value.
     */
    public void setMana(String mana) {
        this.mana = mana;
    }

    /**
     * @return get the linked card associated with the current Card. If there isn't a
     *         linked Card, return null.
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the associated Card with the other card.
     *
     * @param card Card that is linked with this card.
     */
    public void setLink(String card) {
        this.link = card;
    }

    /**
     * @return true if there is a link, otherwise false.
     */
    public boolean hasLink() {
        return link != null;
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
        SQLMap map = new SQLMap();
        map.put(Constant.LINK, link);
        map.put(Constant.ID, id);
        map.put(Constant.NAME, name);
        map.put(Constant.TYPE, type);
        map.put(Constant.ABILITY, ability);
        map.put(Constant.MANA, mana);
        map.put(Constant.POWER, null);
        map.put(Constant.TOUGHNESS, null);
        map.put(Constant.LOYALTY, null);
        return map;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", link='" + link + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", ability='" + ability + '\'' +
                ", mana='" + mana + '\'' +
                '}';
    }
}
