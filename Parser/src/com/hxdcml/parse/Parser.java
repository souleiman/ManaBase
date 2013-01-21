package com.hxdcml.parse;

import com.hxdcml.card.Card;
import com.hxdcml.card.Creature;
import com.hxdcml.card.Planeswalker;
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
    private void start() throws SQLException, InterruptedException {
        String source = prepareSource();
        System.out.println(source);
        String[] split = condense(source);
        Card[] cards = convertToCard(split);

        System.out.println("Inserting cards into database...");
        SQLProcedure procedure = new MagicWrapper(new SQLite());
        for (Card card : cards)
            procedure.insert(card);
        procedure.close();
    }

    private Card[] convertToCard(String[] split) {
        System.out.println("Preparing cards...");
        Card[] cards = new Card[split.length];
        for (int i = 0; i < cards.length; i++)
            cards[i] = createCard(split[i]);
        return cards;
    }

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

    private String[] condense(String source) {
        System.out.println("Condensing and Compressing Strings...");
        String[] split = source.split("</tr>\n</tr>");
        for (int i = 0; i < split.length; i++)
            split[i] = compress(split[i]);
        return split;
    }

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
        Parser parser = new Parser();
        parser.start();
    }
}
