package com.hxdcml.parse;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class is used as a means to extract web-page source
 * in order for special parsing operation can take place.
 * User: Souleiman Ayoub
 * Date: 11/18/12
 * Time: 7:28 PM
 */
public class Source {

    /**
     * By no means should this constructor be called.
     */
    private Source() {
        throw new AssertionError("This class should not be instantiated.");
    }

    /**
     * Collects the the source of the web-page and prepares to return it.
     *
     * @param url The page to retrieve the source
     * @return the source code
     */
    protected static String getSource(String url) {
        String source = "";
        try {
            URL link = new URL(url);
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(link.openStream(), "UTF-8"));
            String input;
            while ((input = reader.readLine()) != null) {
                if (input.trim().equals(""))
                    continue;
                source += (source.equals("") ? "" : "\n") + input;
            }
            reader.close();
        } catch (MalformedURLException e) {
            throw new RuntimeException("[Source:43] The URL provided does not exist or invalid.");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("[Source:45] The site farted, we can't even comprehend the " +
                    "encoding of the " +
                    "character.");
        } catch (IOException e) {
            throw new RuntimeException("[Source:47] Something interrupted the connection or failed to " +
                    "operate.");
        }
        return source;
    }
}
