package com.hxdcml.parse;

import com.hxdcml.card.magic.Body;
import com.hxdcml.card.magic.Card;
import com.hxdcml.card.magic.CardFactory;
import com.hxdcml.card.magic.Creature;
import com.hxdcml.card.magic.DataMap;
import com.hxdcml.card.magic.Planeswalker;
import com.hxdcml.lang.Constant;
import com.hxdcml.sql.SQLite;
import com.hxdcml.wrapper.MagicWrapper;
import com.hxdcml.wrapper.SQLProcedure;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser that supports magic The Gathering. The cards will be parsed from
 * magiccards.info and data necessary will be retrieved. Such Data includes:
 * 1) Image URL
 * 2) Name
 * 3) Detail
 * 4) Ability
 * 5) Text
 * 6) ID
 * 7) Ruling
 * 8) LEGALITY
 * 9) Split / Double / or Flip Cards
 * <p/>
 * User: Souleiman Ayoub
 * Date: 11/18/12
 * Time: 11:18 PM
 */
public final class MParser {
    private Matcher matcher;
    private String query;
    private String counterpart;

    private static final String REGEX_IMAGE = "<img src=\"(\\S+)\"";
    private static final String REGEX_NAME = "<a href=\".*\\.html\">(.*?)</a>";
    private static final String REGEX_DETAIL = "<p>(.*?)<.p>";
    private static final String REGEX_ABILITY = "<p class=\"ctext\">(.*?)<.p>";
    private static final String REGEX_TEXT = "<p>(<i>.*?<.i>)<.p>";
    private static final String REGEX_ID = "(\\d+)\">\\?<.a>";
    private static final String REGEX_RULING = "<li><b>(\\d+.\\d+.\\d+)" +
            "<.b>(.*?)<.li>";
    private static final String REGEX_LEGALITY = "<li class=.(\\w+).>.*in " +
            "(\\w+).*<.li>";
    private static final String REGEX_COUNTERPART = "The other part is:.*?" +
            "\\s<a.*?=\"(.*?)\">";
    private static final String REGEX_BOLD = "<b>|<.b>";
    private static final String REGEX_UNDERLINE = "<u>|<.u>";
    private static final String REGEX_ITALIC = "<i>|<.i>";
    private static final String REGEX_ALL_FONT = REGEX_BOLD + "|" + REGEX_ITALIC +
            "|" + REGEX_UNDERLINE;


    /**
     * Instantiates the object with the given query
     *
     * @param name of the card we want to query
     */
    protected MParser(String name) {
        query = name;
    }

    private Card invoke(int value) {
        String source;
        if (value == 0){
            source = Source.getSource("http://magiccards.info/query?q=!" +
                                query.replaceAll(" ", "+"));
        } else if (value == 1) {
            source = Source.getSource("http://magiccards.info" + counterpart);
        } else {
            source = Source.getSource("http://magiccards.info/random.html");
        }
        return invokeParser(source);
    }

    /**
     * Invokes the parser that has a possibility that it will be parsing the counterpart as
     * well. Parsing the counterpart will be the same as parsing the same Card as it would.
     * No difference in the structure of the counterpart and the original.
     *
     * @param source contains the source code
     * @return a Body object that has been parsed.
     */
    private Card invokeParser(String source) {
        if (source.contains("Your query did not match any cards."))
            return null; //As the condition meets true,
        // meaning it does not exist. Return null.

        source = clean(source);
        Card card = new Card();
        card.setImage(parse(source, REGEX_IMAGE));
        card.setName(parse(source, REGEX_NAME));
        String ability = parseAndReplace(source, REGEX_ABILITY,
                REGEX_ITALIC + "|" + REGEX_BOLD);
        ability = fixAbility(ability);
        card.setAbility(ability);
        card.setFlavor(parseAndReplace(source, REGEX_TEXT, REGEX_ALL_FONT + "|<br>"));
        card.setId(Integer.parseInt(parse(source, REGEX_ID)));

        DataMap map = new DataMap();
        String[] list = parseMultiple(source, REGEX_LEGALITY, null, 2, 1);
        for (String form : list) {
            String[] split = form.split(" ");
            map.put(split[0].toUpperCase(), split[1].toUpperCase());
        }
        card.setFormat(map);

        map = new DataMap();
        list = parseMultiple(source, REGEX_RULING, REGEX_ALL_FONT, 1, 2);
        if (list != null) {
            for (String ruling : list) {
                String[] split = ruling.split(" : ");
                map.put(split[0], split[1]);
            }
        }
        card.setRuling(map);

        String detail = parseAndReplace(source, REGEX_DETAIL, "<br>.*");
        card = extract(card, detail);

        counterpart = parse(source, REGEX_COUNTERPART);

        return card;
    }

    /**
     * Cleans up the ability String from unnecessary HTML tags that are left hanging. Level
     * Up usually has this, but due to it's nature, and in order to leave it readable in
     * "text-mode" the String will be modified to replace break-lines to | as a separator.
     *
     * @param ability the String that will possibly be modified
     * @return a new String if the String has been fixed
     */
    private String fixAbility(String ability) {
        ability = ability.replaceAll("[\\{|}]", "");
        ability = ability.replaceAll("<br><br>", " | ");
        ability = ability.replaceAll("LEVEL", "● LEVEL");
        return ability;
    }

    /**
     * Cleans up the source while keeping data necessary. This will help making parsing easier in order to retrieve
     * necessary data.
     *
     * @param source in which we want to clean up
     * @return String of a new Source that is cleaner than it originally was.
     */
    private String clean(String source) {
        String tableStart = "</table>\n" +
                "<hr />";
        //"<table border=\"0\" cellpadding=\"0\" " +
                //"cellspacing=\"0\" width=\"100%\" align=\"center\" " +
                //"style=\"margin: 0 0 0.5em 0;\">";
        String tableEnd = "<u><b>Printings:</b></u><br>";
        int indexBegins = source.indexOf(tableStart);
        int indexEnds = source.indexOf(tableEnd, indexBegins);
        String peeled = source.substring(indexBegins, indexEnds).trim();

        String cleanerSource = "";
        String[] split = peeled.split("\n");

        /**
         * Cleans up each line where necessary.
         */
        for (int i = 0; i < split.length; i++) {
            String line = split[i].replace("  ", "");
            if (i <= 3) //We don't need the first 3 lines
                continue;
            else if (check(line))
            /**
             * Anything that passes this checkpoint is not needed
             */
                continue;
            else if (!line.endsWith(">")) {
                Object[] o = append(line, split[i + 1], split[i + 2]);
                line = (String) o[0];
                i += (Integer) o[1];
            }
            cleanerSource += line + "\n";
        }
        return cleanerSource;
    }

    /**
     * If the given String starts with or possibly contains the following
     * Strings in the conditions, it will return true. This is to be used as
     * a means to parse out unneeded data.
     *
     * @param line The String that we will be checking upon.
     * @return true if there exist the following below, otherwise false.
     */
    private boolean check(String line) {
        return line.startsWith("<small>") || line.startsWith("</td>") ||
                line.startsWith("<td valign") ||
                line.contains("http://magiccards.info/images/en.gif") ||
                line.startsWith("width") || line.startsWith("</span>") ||
                line.equals("<ul>") || line.startsWith("</ul>") ||
                line.startsWith("<!-") || line.contains("<span ") ||
                line.equals("<p><i></i></p>") || line.startsWith("<p>Illus.");
    }

    /**
     * Put everything in 1 line, makes it easier to read
     * <pre>
     *     <p>This,
     *     then,
     *     this</p>
     *
     *     Should look like
     *
     *     <p>This, then, this</p>
     * </pre>
     * <p/>
     * Some lines are broken and split to 2 ~ 3 parts. In order
     * to accurately obtain the necessary data
     * needed. We need to put these lines together as one line.
     * <p/>
     * First we need to check if the current line is in need of being appended,
     *
     * @param line      The current line that will have next and possible
     *                  afterNext appended after another.
     * @param next      The next line after current which will be appended to
     *                  current line.
     * @param afterNext The last line that will be used and this line will
     *                  only be used if and only if the line after current is
     *                  does not end with the html </p>
     * @return An array of Object, which holds a String and an Integer. The
     *         String will be the appended line. The integer will be the
     *         offset to push the counter of the loop, this is to prevent the
     *         loop from entering next or possible afterNext.
     */
    private Object[] append(String line, String next,
                            String afterNext) {

        String nextLine = next.replace("  ", "");
        String last = afterNext.replace("  ", "");
        int i = 0;
        if (nextLine.endsWith("</p>"))
            line = line + nextLine.replace("  ", "");
        else if (!nextLine.endsWith("</p>")) {
            line = line + nextLine + last;
            i++;
        }
        i++;
        return new Object[]{line, i};
    }

    /**
     * Extracts information from the provided detail, since the details can vary depending
     * on the type of card we are dealing with, thus will check each type of card to verify
     * the condition and will appropriately extract the data necessary, such as Type, Power,
     * Toughness, Loyalty, Mana, and Cost, not all but type is required to be extracted.
     *
     * @param card   that will be modified.
     * @param detail the String that contains the data to be extracted
     * @return the modified and injected data that we extracted
     */
    private Card extract(Card card, String detail) {
        String type = "";
        String mana = null;
        int cost = 0;
        if (detail.contains("Land") && !detail.contains("Creature")) {
            type = detail;
        } else if (detail.contains("Creature")) {
            Creature creature = CardFactory.create(card, Creature.class);
            String[] split = detail.split(", ");
            type = parse(split[0], "(.*—.*)\\s.*/");
            String power = parse(split[0], type + "\\s(.*?)/");
            String toughness = parse(split[0], "/(.*?)$");

            creature.setPower(power);
            creature.setToughness(toughness);
            card = creature;

            //Some cards may not have a Mana Cost or Cost Coversion
            //ie. Mayor of Avabruck's werewolve
            if (split.length == 2) {
                String[] splice = split[1].split(" ");
                mana = splice[0];
                /**
                 * Check to see for cards such as Memnite that have no mana cost,
                 * if the card has a mana cost, it will remove the unneeded characters and
                 * leave the integers alone. Otherwise, just leave the c_cost equal to zero.
                 */
                if (splice.length == 2)
                    cost = Integer.parseInt(splice[1].replaceAll("[^0-9]", ""));
            }
        } else if (detail.contains("Artifact")) {
            type = parse(detail, "^(.*?),");
            mana = parse(detail, type + ",\\s(.*?[^ ])");
            cost = Integer.parseInt(mana);
        } else if (detail.startsWith("Sorcery") || detail.startsWith("Instant")
                || detail.startsWith("Enchantment")) {
            type = parse(detail, "^(.*?),");
            mana = parse(detail, type + ",\\s(.*?)\\s\\(");
            cost = Integer.parseInt(parse(detail, mana + "\\s\\((.*?)\\)"));
        } else if (detail.startsWith("Planeswalker —")) {
            Planeswalker walker = CardFactory.create(card, Planeswalker.class);
            type = parse(detail, "(Planeswalker — .*?)\\s");
            String loyalty = parse(detail, "\\(Loyalty: (.*?)\\),");
            walker.setLoyalty(loyalty);
            mana = parse(detail, ",\\s(.*?)\\s\\(");
            String ccost = parse(detail, mana + "\\s\\((.*?)\\)");
            if (ccost != null)
                cost = Integer.parseInt(ccost);
            card = walker;
        }
        card.setType(type);
        card.setMana(mana);
        card.setCost(cost);
        return card;
    }

    /**
     * If the counterpart string is not null, this means there is another
     * card on the otherside.
     *
     * @return true if it's not null, otherwise false.
     */
    private boolean hasCounterpart() {
        return counterpart != null;
    }

    /**
     * Parses the data based on the regular expression. If the data is found return the
     * String appropriate to the result, otherwise return null.
     *
     * @param source The source in which we will use to look for the data associated
     *               with the regular expression.
     * @param regex  The regular expression that will be used in order to attain the
     *               data needed.
     * @return A String if the regexp matcher found something, otherwise return null.
     */
    private String parse(String source, String regex) {
        matcher = Pattern.compile(regex).matcher(source);
        return matcher.find() ? matcher.group(1) : null;
    }

    /**
     * Unlike the normal parse, this parse will do the same exact thing as the parser
     * above however, based on the data found. It will replace the given replace
     * regular expression and replace it with an empty String.
     *
     * @param source  The source in which we will use to look for the data associated
     *                with the regular expression.
     * @param regex   The regular expression that will be used in order to attain the
     *                data needed.
     * @param replace If this String has the given replace regex,
     *                it will replace it with an empty String.
     * @return A String if the regexp matcher found something, otherwise return null.
     */
    private String parseAndReplace(String source, String regex,
                                   String replace) {
        matcher = Pattern.compile(regex).matcher(source);
        String text = matcher.find() ? matcher.group(1) : null;
        if (text != null) {
            if (text.equals(""))
                return null;
            return text.replaceAll(replace, "");
        }
        return null;
    }

    /**
     * Parses multiple data associated with the regexp. This will be used in the case
     * of Ruling and Legality since they are more likely to have multiple results.
     *
     * @param source  The source in which we will use to look for the data associated
     *                with the regular expression.
     * @param regex   The regular expression that will be used in order to attain the
     *                data needed.
     * @param replace If this String has the given replace regex,
     *                it will replace it with an empty String.
     * @param first   Which String to be displayed first.
     * @param second  Which String to be displayed second.
     * @return An array of String which holds information necessary.
     */
    private String[] parseMultiple(String source, String regex, String replace,
                                   int first, int second) {
        ArrayList<String> list = new ArrayList<String>();
        matcher = Pattern.compile(regex).matcher(source);
        while (matcher.find()) {
            String itemA = matcher.group(first);
            String itemB = matcher.group(second);
            if (replace != null) {
                itemA = itemA.replaceAll(replace, "");
                itemB = itemB.replaceAll(replace, "");
            }
            list.add(itemA + " " + itemB);
        }
        int size = list.size();
        return size == 0 ? null : list.toArray(new String[size]);
    }

    /**
     * The main objective of this method is to look up the magic the Gathering card and return a magic Card in
     * respect to the name of the card if possible. If it cannot, it will return a null.
     * <p/>
     * Due to the nature of the possible different cards that are available within the Cards itself. They will be
     * broken down to different methods to accommodate the possibilities.
     *
     * Note, if name is null, then a random search will be invoked.
     *
     * @param name of the magic card that will be looked up.
     * @return the card if it exist. Otherwise, return null.
     * @precondition Check if the page found a result, if not return null.
     */
    protected static Card[] parse(String name) {
        ArrayList<Card> bodies = new ArrayList<Card>();
        MParser parser = new MParser(name);
        Card card = parser.invoke(name != null ? 0 : 2);
        if (card == null)
            return null;
        bodies.add(card);
        if (parser.hasCounterpart()) {
            Card counterpart = parser.invoke(1);
            card.setLink(counterpart.getName());
            counterpart.setLink(card.getName());
            bodies.add(counterpart);
        }
        int size = bodies.size();
        return bodies.toArray(new Card[size]);
    }

    public static void main(String[] args) throws SQLException {
        Scanner keyboard = new Scanner(System.in);
        while (true) {
            System.out.print("Card Name: ");
            String input = keyboard.nextLine();
            if (input.equalsIgnoreCase("."))
                break;

            if (input.isEmpty())
                input = null;

            Body[] body = parse(input);
            if (body == null) {
                System.out.println("Failed...");
                continue;
            }
            SQLProcedure wrapper = new MagicWrapper(new SQLite(Constant.TYPE_MAGIC));
            Body card = body[0];
            wrapper.insert(card);
            if (card.hasLink()) {
                wrapper.insert(body[1]);
            }
            wrapper.close();
        }
    }
}
