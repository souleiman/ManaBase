package com.hxdcml.wrapper;

import com.hxdcml.card.Card;
import com.hxdcml.card.CardFactory;
import com.hxdcml.card.Creature;
import com.hxdcml.card.DataMap;
import com.hxdcml.card.Planeswalker;
import com.hxdcml.sql.QueryMap;
import com.hxdcml.sql.QueryNode;
import com.hxdcml.sql.SQLBinder;
import com.hxdcml.sql.SQLEntities;
import com.hxdcml.sql.SQLMap;
import com.hxdcml.sql.SQLite;
import com.hxdcml.sql.UpdateMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.hxdcml.lang.Constant.ABILITY;
import static com.hxdcml.lang.Constant.CLASSIC;
import static com.hxdcml.lang.Constant.COMMANDER;
import static com.hxdcml.lang.Constant.COST;
import static com.hxdcml.lang.Constant.DATE;
import static com.hxdcml.lang.Constant.EXTENDED;
import static com.hxdcml.lang.Constant.FLAVOR;
import static com.hxdcml.lang.Constant.ID;
import static com.hxdcml.lang.Constant.IMAGE;
import static com.hxdcml.lang.Constant.LEGACY;
import static com.hxdcml.lang.Constant.LINK;
import static com.hxdcml.lang.Constant.LOYALTY;
import static com.hxdcml.lang.Constant.MANA;
import static com.hxdcml.lang.Constant.MODERN;
import static com.hxdcml.lang.Constant.NAME;
import static com.hxdcml.lang.Constant.POWER;
import static com.hxdcml.lang.Constant.RULING;
import static com.hxdcml.lang.Constant.R_NAME;
import static com.hxdcml.lang.Constant.STANDARD;
import static com.hxdcml.lang.Constant.TOUGHNESS;
import static com.hxdcml.lang.Constant.TYPE;
import static com.hxdcml.lang.Constant.VINTAGE;

/**
 * User: Souleiman Ayoub
 * Date: 12/24/12
 * Time: 2:22 PM
 */
public class MagicWrapper extends SQLWrapper implements SQLProcedure {
    protected static SQLite lite;
    private static SQLWrapper format = new FormatWrapper();
    private static SQLWrapper ruling = new RulingWrapper();

    private static String lastInsertedValue;
    //Table Name
    public static final String TABLE_NAME = "MAGIC";

    public MagicWrapper(SQLite lite) throws SQLException {
        MagicWrapper.lite = lite;
        createTable();
        format.createTable();
        ruling.createTable();
    }

    /**
     * Creates a Table
     */
    @Override
    protected void createTable() throws SQLException {

        PreparedStatement statement = lite.getConnection().prepareStatement(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        ID + " INTEGER," + //Multiverse ID
                        NAME + " STRING NOT NULL UNIQUE COLLATE NOCASE," + // NAME
                        TYPE + " STRING NOT NULL COLLATE NOCASE," + // Card Type
                        ABILITY + " STRING COLLATE NOCASE," + //Ability
                        FLAVOR + " STRING COLLATE NOCASE," + //Flavor
                        MANA + " STRING COLLATE NOCASE," + //MANA
                        COST + " STRING NOT NULL," + //COST
                        LINK + " STRING," + //LINK
                        IMAGE + " STRING," + //IMAGE
                        POWER + " STRING," + //POWER
                        TOUGHNESS + " STRING," + //TOUGHNESS
                        LOYALTY + " STRING" + //LOYALTY
                        ")"
        );
        statement.executeUpdate();
        statement.close();
    }

    /**
     * Creates an INSERT SQL Operation based on the Wrapper
     *
     * @param entities are Objects that contains the data that will be inserted into the
     *                 database.
     */
    @Override
    protected synchronized PreparedStatement inject(SQLEntities entities) throws SQLException {
        String column =
                String.format(" (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
                        ID, NAME, TYPE, ABILITY, FLAVOR, MANA, COST, LINK, IMAGE,
                        POWER, TOUGHNESS, LOYALTY);
        return lite.getConnection().prepareStatement(
                "INSERT INTO " + TABLE_NAME + column +
                        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
    }

    /**
     * Injects the data into the database.
     *
     * @param entities the SQLEntities that contain information necessary to be inserted to
     *                 the database.
     */
    @Override
    public synchronized void insert(SQLEntities entities) throws SQLException {
        PreparedStatement statement = inject(entities);

        SQLMap map = entities.getEntities();
        String name = (String) map.get(NAME);
        statement.setInt(1, (Integer) map.get(ID));
        statement.setString(2, name);
        statement.setString(3, (String) map.get(TYPE));
        statement.setString(4, (String) map.get(ABILITY));
        statement.setString(5, (String) map.get(FLAVOR));
        statement.setString(6, (String) map.get(MANA));
        statement.setString(7, String.valueOf(map.get(COST)));
        statement.setString(8, (String) map.get(LINK));
        statement.setString(9, (String) map.get(IMAGE));
        statement.setString(10, (String) map.get(POWER));
        statement.setString(11, (String) map.get(TOUGHNESS));
        statement.setString(12, (String) map.get(LOYALTY));
        statement.executeUpdate();
        statement.close();

        lastInsertedValue = name;

        Card card = (Card) entities;
        format.insert(card.getFormat());
        ruling.insert(card.getRuling());
    }

    /**
     * @return the last inserted row.
     */
    protected synchronized static String getLastInsertedRowValue() {
        return lastInsertedValue;
    }

    /**
     * Deletes a row based on the primary/reference key
     *
     * @param name the row that will be deleted that is associated with this value.
     */
    @Override
    public synchronized boolean delete(String name) throws SQLException {
        String delete = String.format("DELETE FROM %s WHERE %s LIKE %s",
                TABLE_NAME, NAME, SQLBinder.format(name));
        return lite.executeUpdate(delete) == 1;
    }

    /**
     * Updates the SQL operation where it is equal to the key/r_key and replaced based on
     * the SQLMap values.
     *
     * @param name the row that will be modified that is associated with this value.
     * @param map  values to be changed
     */
    @Override
    public synchronized boolean update(String name, UpdateMap map) throws SQLException {
        String set = map.makeToString();
        String update = String.format("UPDATE %s SET %s WHERE %s LIKE %s",
                TABLE_NAME, set, NAME, SQLBinder.format(name));
        return lite.executeUpdate(update) == 1;
    }

    /**
     * Looks for the data in the database based on the results in the SQLMap
     *
     * @param map query entities that we will use to search in the database
     * @return a ResultSet
     */
    @Override
    public Card[] query(QueryMap map) throws SQLException {
        String result = String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, " +
                "%s, %s, %s, %s, %s, %s, %s, %s", ID, NAME, TYPE, ABILITY, FLAVOR, MANA,
                COST, LINK, IMAGE, POWER, TOUGHNESS, LOYALTY, STANDARD, EXTENDED, MODERN,
                LEGACY, VINTAGE, CLASSIC, COMMANDER, DATE, RULING);
        String query = String.format("SELECT %s FROM %s, %s, %s ", result, TABLE_NAME,
                FormatWrapper.TABLE_NAME, RulingWrapper.TABLE_NAME);
        String condition = String.format("WHERE %s.%s LIKE %s.%s AND %s.%s LIKE %s.%s",
                TABLE_NAME, NAME, FormatWrapper.TABLE_NAME, R_NAME, TABLE_NAME, NAME,
                RulingWrapper.TABLE_NAME, R_NAME);

        String end = "";
        for (String value : map.keySet()) {
            end += " AND ";
            QueryNode node = map.get(value);
            String search = node.getValue();

            end += value;
            if (value.equals(ID)) {
                end += " = " + search;
            } else {
                end += " LIKE ";
                if (node.isExact())
                    end += SQLBinder.format(search);
                else {
                    search = SQLBinder.exhaustFormat(search);
                    end += search;
                }
            }
        }
        String sql = query + condition + end;
        ResultSet set = lite.executeQuery(sql);
        return makeList(set);
    }

    /**
     * Parses each value in the ResultSet and should give us an Array of Card.
     *
     * @param set that contains all the data and cursor values
     * @return an Array of Cards
     * @throws SQLException when something goes wrong.
     */
    private Card[] makeList(ResultSet set) throws SQLException {
        ArrayList<Card> list = new ArrayList<Card>();
        Card card = null;
        while (set.next()) {
            String name = set.getString(NAME);
            String date = set.getString(DATE);
            String ruling = set.getString(RULING);

            if (card == null || !name.equals(card.getName())) { //New Card preparation
                card = new Card();
                card.setImage(set.getString(IMAGE));
                card.setId(set.getInt(ID));
                card.setName(set.getString(NAME));
                card.setType(set.getString(TYPE));
                card.setAbility(set.getString(ABILITY));
                card.setFlavor(set.getString(FLAVOR));
                card.setMana(set.getString(MANA));
                String value = set.getString(COST);
                card.setCost(Integer.parseInt(value));
                card.setLink(set.getString(LINK));
                String power = set.getString(POWER);
                String loyalty = set.getString(LOYALTY);
                if (power != null) {
                    Creature creature = CardFactory.create(card, Creature.class);
                    creature.setPower(power);
                    creature.setToughness(set.getString(TOUGHNESS));
                    card = creature;
                } else if (loyalty != null || card.isPlaneswalker()) {
                    Planeswalker walker = CardFactory.create(card, Planeswalker.class);
                    walker.setLoyalty(loyalty == null ? "" : loyalty);
                    card = walker;
                }
                DataMap format = card.getFormat();
                format.put(STANDARD, set.getString(STANDARD));
                format.put(EXTENDED, set.getString(EXTENDED));
                format.put(MODERN, set.getString(MODERN));
                format.put(LEGACY, set.getString(LEGACY));
                format.put(VINTAGE, set.getString(VINTAGE));
                format.put(CLASSIC, set.getString(CLASSIC));
                format.put(COMMANDER, set.getString(COMMANDER));

                DataMap rule = card.getRuling();
                rule.put(date, ruling);

                list.add(card);
            } else if (name.equals(card.getName())) { //Same card but different ruling
                DataMap dm = card.getRuling();
                dm.put(date, ruling);
            }
        }

        int size = list.size();
        return list.toArray(new Card[size]);
    }

    /**
     * @return an integer value.
     */
    @Override
    public int size() {
        int size = -1;
        try {
            PreparedStatement statement = lite.getConnection().prepareStatement(
                    "SELECT COUNT(*) FROM " + TABLE_NAME
            );
            ResultSet set = statement.executeQuery();
            if (set.next())
                size = set.getInt("COUNT(*)");
            set.close();
            statement.close();
        } catch (SQLException e) {
            return size;
        }
        return size;
    }

    /**
     * Closes the SQLite class from locking the database
     */
    @Override
    public void close() {
        lite.close();
    }

    public static void main(String[] args) throws SQLException {
        SQLProcedure procedure = new MagicWrapper(new SQLite());
        QueryMap map = new QueryMap();
        map.put(NAME, new QueryNode("Garruk", false));
        map.put(COMMANDER, new QueryNode("LEGAL", true));
        Card[] cards = (Card[]) procedure.query(map);
        System.out.println(Arrays.toString(cards));
        for (Card card : cards)
            System.out.println(card.getName());
        procedure.close();
    }
}