package com.hxdcml.parse;

import com.hxdcml.card.Card;
import com.hxdcml.card.Creature;
import com.hxdcml.card.Planeswalker;
import com.hxdcml.lang.Constant;
import com.hxdcml.sql.SQLite;
import com.hxdcml.wrapper.MagicWrapper;
import com.hxdcml.wrapper.SQLProcedure;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Souleiman Ayoub
 * Date: 1/21/13
 * Time: 8:57 AM
 */
public class Parser {
    /**
     * This will begin the process of reading a set which can be found at
     * gatherer.wizard.com, this will help us read in a large amount of cards and insert
     * them into the database.
     *
     * @throws InterruptedException thrown when something interrupts the Merger.
     * @throws SQLException         In case the database, is in use.
     */
    private void start() throws InterruptedException, SQLException {
        String source = prepareSource();
        String[] split = condense(source);
        Card[] cards = convertToCard(split);

        System.out.println("Inserting cards into database...");
        SQLProcedure procedure = new MagicWrapper(new SQLite(Constant.DATABASE_FILE_MAGIC));
        for (Card card : cards) {
            try {
                procedure.insert(card);
            } catch (SQLException e) {
                System.out.println("Copy found... Not adding... " + card.getName());
            }
        }
        procedure.close();
    }

    /**
     * Converts the list of cards based on the split result.
     *
     * @param split contains content of the card that will be parsed out and generated.
     * @return an Array of Card.
     */
    private Card[] convertToCard(String[] split) {
        System.out.println("Preparing cards...");
        Card[] cards = new Card[split.length];
        for (int i = 0; i < cards.length; i++)
            cards[i] = createCard(split[i]);
        return cards;
    }

    /**
     * Converts a line of String into a Card. Based on the given information within the
     * String, it will represent the Card object.
     *
     * @param data the String that contains data of the Card.
     * @return a Card object that was represented by the String.
     */
    private Card createCard(String data) {
        HashMap<String, String> map = new HashMap<>();
        String[] split = data.split("\n");
        for (String line : split) {
            String[] splice = line.split(":", 2);
            map.put(splice[0], (splice.length == 1 ? "0" : splice[1].trim()));
        }
        Card card;
        String type = map.get("Type");
        if (type.startsWith("Planeswalker")) {
            Planeswalker planeswalker = new Planeswalker();
            String loyalty = map.get("Loyalty");
            planeswalker.setLoyalty(loyalty);
            card = planeswalker;
        } else if (type.contains("Creature") || type.contains("Summon")) {
            Creature creature = new Creature();
            String pt = map.get("Pow/Tgh").replaceAll("\\(|\\)", "");
            String[] separate = pt.split("/");
            creature.setPower(separate[0]);
            creature.setToughness(separate[1]);
            card = creature;
        } else {
            card = new Card();
        }

        card.setId(Integer.parseInt(map.get("ID")));
        card.setName(map.get("Name"));
        card.setMana(map.get("Cost"));
        card.setType(map.get("Type"));
        String ability = map.get("Rules Text");
        card.setAbility(ability.equals("0") ? "" : ability);
        return card;
    }

    /**
     * Begins by separating each table row and new line. This will help us visualize each
     * data needed. This will be compressed, which will give us a result that contains
     * information of a card.
     *
     * @param source the Source the contains HTML data.
     * @return an Array of String, that each represent a Card Object.
     */
    private String[] condense(String source) {
        System.out.println("Condensing and Compressing Strings...");
        String[] split = source.split("</tr>\n</tr>");
        for (int i = 0; i < split.length; i++)
            split[i] = compress(split[i]);
        return split;
    }

    /**
     * Converts each line from HTML to an easier format that will later help us easily format.
     *
     * @param card the String that represents the card in HTML format.
     * @return a String that is easier to read then the HTML format.
     */
    private String compress(String card) {
        String[] split = card.split("</tr>");
        String temp = "";
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].replaceAll("\n", " ").trim();
            if (i == 0) {
                Matcher matcher =
                        Pattern.compile("multiverseid=(\\d+)\">(.*?)</a>").matcher(split[0]);
                matcher.find();
                temp += "ID: " + matcher.group(1) + "\n";
                temp += "Name: " + matcher.group(2) + "\n";
                continue;
            }
            temp += split[i] + "\n";
        }
        return temp.trim();
    }

    /**
     * Prepares the source to be parsed by merging the data we need.
     *
     * @return the String source which contains the HTML.
     * @throws InterruptedException Is to be thrown if something get interrupted in the
     *                              Merger.
     */
    private String prepareSource() throws InterruptedException {
        System.out.println("Preparing the source for easy read-access");
        System.out.println("Reading original source(s)...");
        String source = Merger.merge();
        int begin = source.indexOf("<!-- End Options Container -->");
        int end = source.indexOf("<div class=\"clear\"></div>", begin);
        System.out.println("Removing Unnecessary portions...");
        source = source.substring(begin, end);
        String[] split = source.split("\n");
        String newSource = "";
        for (String line : split) {
            if (!lineNotNeeded(line)) {
                line = line.trim();
                newSource += line + "\n";
            }
        }
        newSource = newSource.replaceAll("<br />", "");
        return newSource.trim();
    }

    /**
     * Based on the given line, if any of the unecessary String falls in the given line.
     * return true.
     *
     * @param line the String we want to check against.
     * @return true, if the line contains any of the Strings that's not needed, otherwise,
     *         false.
     */
    private boolean lineNotNeeded(String line) {
        line = line.trim();
        String[] dontNeed = {
                "<!--", "<div", "</div>", "<table", "<td>", "</td>", "<td", "<tr",
                "</table>", "<br"
        };
        boolean bool = false;
        for (String var : dontNeed)
            bool |= line.startsWith(var);
        return bool;
    }

    public static void main(String[] args) throws SQLException, InterruptedException {
        Parser p = new Parser();
        p.start();
    }
}
