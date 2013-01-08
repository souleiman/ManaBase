package com.hxdcml.lang;

/**
 * The constant interface. What more can I say...?
 *
 * User: Souleiman Ayoub
 * Date: 11/18/12
 * Time: 8:28 PM
 */
public interface Constant {
    public static final int TYPE_MAGIC = 1;
    public static final int TYPE_YUGIOH = 2;


    public static final String DATABASE_FILE_MAGIC = "manabase.db";
    public static final String DATABASE_FILE_YUGIOH = "yugibase.db";

    /**
     * Column Entities for all SQLWrapper
     */
    public static final String R_NAME = "R_NAME";

    /**
     * Entities for MagicWrapper
     */
    public static final String ID = "ID"; //Multiverse ID
    public static final String NAME = "NAME";
    public static final String TYPE = "TYPE";
    public static final String ABILITY = "ABILITY";
    public static final String FLAVOR = "FLAVOR";
    public static final String MANA = "MANA";
    public static final String COST = "COST";
    public static final String LINK = "LINK";
    public static final String IMAGE = "IMAGE";
    public static final String POWER = "POWER";
    public static final String TOUGHNESS = "TOUGHNESS";
    public static final String LOYALTY = "LOYALTY";

    /**
     * Entities for Ruling Wrapper
     */
    public static final String DATE = "ANNOUNCED_DATE";
    public static final String RULING = "RULE";

    /**
     * Entities for Format Wrapper
     */
    public static final String VINTAGE = "VINTAGE";
    public static final String LEGACY = "LEGACY";
    public static final String EXTENDED = "EXTENDED";
    public static final String STANDARD = "STANDARD";
    public static final String CLASSIC = "CLASSIC";
    public static final String COMMANDER = "COMMANDER";
    public static final String MODERN = "MODERN";
}
