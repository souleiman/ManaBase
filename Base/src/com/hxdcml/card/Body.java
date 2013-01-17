package com.hxdcml.card;

import com.hxdcml.sql.SQLEntities;

/**
 * User: Souleiman Ayoub
 * Date: 11/22/12
 * Time: 6:33 PM
 */
public interface Body extends SQLEntities{
    /**
     * Check if the card is a permanent
     * It would be permanent if the card is not an instant or sorcery.
     *
     * @return <i>true</i> - if it's permanent; otherwise, <i>false</i>.
     */
    public boolean isPermanent();

    /**
     * Check if the card is a land.
     * It would be a land if it contained "Land" in the Card Type
     *
     * @return <i>true</i> - if it's a land; otherwise, <i>false</i>.
     */
    public boolean isLand();

    /**
     * Check if the card is a creature.
     * It would be a creature if it contained "creature" in the Card Type
     *
     * @return <i>true</i> - if it's a creature; otherwise, <i>false</i>.
     */
    public boolean isCreature();

    /**
     * Check if the card is a artifact.
     * It would be a artifact if it contained "artifact" in the Card Type
     *
     * @return <i>true</i> - if it's a artifact; otherwise, <i>false</i>.
     */
    public boolean isArtifact();

    /**
     * Check if the card is a enchantment.
     * It would be a enchantment if it contained "enchantment" in the Card Type
     *
     * @return <i>true</i> - if it's a enchantment; otherwise, <i>false</i>.
     */
    public boolean isEnchantment();

    /**
     * Check if the card is a sorcery.
     * It would be a sorcery if it contained "Land" in the Card Type
     *
     * @return <i>true</i> - if it's a sorcery; otherwise, <i>false</i>.
     */
    public boolean isSorcery();

    /**
     * Check if the card is a instant.
     * It would be a instant if it contained "instant" in the Card Type
     *
     * @return <i>true</i> - if it's a instant; otherwise, <i>false</i>.
     */
    public boolean isInstant();

    /**
     * Check if the card is a planeswalker.
     * It would be a planeswalker if it contained "planeswalker" in the Card Type
     *
     * @return <i>true</i> - if it's a planeswalker; otherwise, <i>false</i>.
     */
    public boolean isPlaneswalker();

    /**
     * Check if the card is a planes.
     * It would be a plane if it contained "plane" in the Card Type
     *
     * @return <i>true</i> - if it's a plane; otherwise, <i>false</i>.
     */
    public boolean isPlane();

    /**
     * Check if the card is a scheme.
     * It would be a land if it contained "scheme" in the Card Type
     *
     * @return <i>true</i> - if it's a scheme; otherwise, <i>false</i>.
     */
    public boolean isScheme();

    /**
     * Some cards my not have a lore, so we want to check if it does.
     *
     * @return <i>true </i>- if it has lore; otherwise <i>false.</i>
     */
    public boolean hasAbility();

    /**
     * Like lore(), some cards, may not even have a flavor text, this will check if it does.
     *
     * @return <i>true </i>- if it has flavor text; otherwise <i>false.</i>
     */
    public boolean hasFlavor();

    /**
     * Get the name of the card
     *
     * @return name of the card in String
     */
    public String getName();

    /**
     * Set the name of the card
     *
     * @param name String which holds the name of the card.
     */
    public void setName(String name);

    /**
     * Get the ID of the card.
     *
     * @return an <i>int</i> value.
     */
    public int getId();

    /**
     * Set the value of the id.
     *
     * @param id <i>int</i> which holds the value of id
     */
    public void setId(int id);

    /**
     * Get the card type
     *
     * @return type of the card in String
     */
    public String getType();

    /**
     * Set the card type.
     *
     * @param type String which holds the type of the card.
     */
    public void setType(String type);

    /**
     * Get the card text.
     *
     * @return card text in String
     */
    public String getAbility();

    /**
     * Sets the lore.
     *
     * @param ability String which holds the lore of the card
     */
    public void setAbility(String ability);

    /**
     * Get Flavor Text.
     *
     * @return Flavor text in String
     */
    public String getFlavor();

    /**
     * Set the flavor text of the card.
     *
     * @param text String which holds the Flavor text of the card.
     */
    public void setFlavor(String text);

    /**
     * Get the cost of the card.
     *
     * @return mana cost in String
     */
    public String getMana();

    /**
     * Set the mana cost
     *
     * @param cost String which holds the mana cost value.
     */
    public void setMana(String cost);

    /**
     * Get the converted cost
     *
     * @return Converted Mana Cost in String
     */
    public int getCost();

    /**
     * Set the converted mana cost
     *
     * @param convertedCost String which holds the converted mana cost value.
     */
    public void setCost(int convertedCost);

    /**
     * Get the ruling list.
     *
     * @return list that contains the rules
     */
    public DataMap getRuling();

    /**
     * Sets the ruling
     *
     * @param ruling sets the new ruling
     */
    public void setRuling(DataMap ruling);

    /**
     * Check to see if the card has rules.
     *
     * @return true if there is at least 1 rule; otherwise false.
     */
    public boolean hasRules();

    /**
     * Get format of the card.
     *
     * @return <i>Format</i> which contains information about the legal process of the card,
     *         based on a specific format.
     */
    public DataMap getFormat();

    /**
     * Set the format
     *
     * @param format new format we want to set.
     */
    public void setFormat(DataMap format);

    /**
     * @return get the linked card associated with the current Card. If there isn't a
     *         linked Card, return null.
     */
    public String getLink();

    /**
     * Sets the associated Card with the other card.
     *
     * @param card Card that is linked with this card.
     */
    public void setLink(String card);

    /**
     * @return true if there is a link, otherwise false.
     */
    public boolean hasLink();

    /**
     * Set the image url of the Image
     *
     * @param url the image url of the image
     */
    public void setImage(String url);

    /**
     * Get the url of the image
     *
     * @return the String of the url location
     */
    public String getImage();
}
