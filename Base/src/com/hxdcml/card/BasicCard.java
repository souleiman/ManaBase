package com.hxdcml.card;

import com.hxdcml.sql.SQLEntities;

/**
 * User: Souleiman Ayoub
 * Date: 11/19/12
 * Time: 4:55 PM
 */
public interface BasicCard extends SQLEntities {
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
}
