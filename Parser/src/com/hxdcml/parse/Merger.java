package com.hxdcml.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * User: Souleiman Ayoub
 * Date: 1/21/13
 * Time: 3:15 PM
 */
public class Merger implements Runnable {
    private File file;
    private String source;

    public static String merge() throws InterruptedException {
        File[] files = new File("sets/").listFiles();
        assert files != null;
        Arrays.sort(files);
        Merger[] mergers = new Merger[files.length];
        Thread[] threads = new Thread[files.length];

        for (int i = 0; i < files.length; i++) {
            mergers[i] = new Merger(files[i]);
            threads[i] = new Thread(mergers[i]);
            threads[i].start();
            threads[i].join();
        }

        System.out.println("Merging...");
        String source = "";
        for (Merger merger : mergers) {
            source += merger.getString() + "\n";
        }

        return source;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        System.out.println("Starting "+ file.getName());
        source = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                source += line + "\n";
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Finished " + file.getName());
    }

    public Merger(File file) {
        this.file = file;
    }

    public String getString() {
        return source.trim();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Merger.merge());
    }
}
