package com.hxdcml.server;

import com.hxdcml.protocol.Protocol;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

/**
 * Prepares a Socket with a Threaded connection.
 * User: Souleiman Ayoub
 * Date: 1/2/13
 * Time: 9:12 PM
 */
public class PoolNode extends Observable implements Runnable {
    private Thread thread;
    private Socket socket;
    protected Streamer connection;
    protected String hostname;

    public PoolNode(Socket socket) {
        this.socket = socket;
        thread = new Thread(this);
        hostname = socket.getInetAddress().getHostName();
        System.out.println(hostname + " has connected.");
    }

    /**
     * Starts the Thread.
     */
    public void start() {
        thread.start();
    }

    /**
     * Interrupts the thread.
     */
    public void stop() {
        try {
            thread.interrupt();
            if (!socket.isClosed())
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This will be used to help spawn a Thread to allow multiple Clients to connect to the
     * server. The streaming connection will provide the functionality of transferring
     * information between the client and server. The data retrieved from the client will be
     * processed by the Protocol and based on the input value, we should expect a String
     * that will do what it was requested.
     * <p/>
     * If something goes wrong, the Observer will be notified, and interrupt the thread and
     * removes the node. If the user request to HALT or EXIT, the Observer will also be
     * notified to prepare for a clean up.
     */
    @Override
    public void run() {
        try {
            connection = new ConnectionStream();

            String input;
            String output;
            while ((input = connection.requestMessage()) != null) {
                System.out.println(hostname + " message: " + input);
                String attempt = check(input);
                if (attempt != null) {
                    connection.sendMessage(input);
                    notify(attempt);
                } else {
                    output = Protocol.process(input);
                    connection.sendMessage(output);
                }
            }
            connection.close();
            socket.close();
        } catch (SocketException e) {
            if (e.getMessage().equals("Connection reset")) {
                System.out.println("Connection between the client has been severed.");
                notify(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            assert connection != null;
            connection.sendMessage(null, e);
            e.printStackTrace();
        }
    }

    /**
     * Condenses the notifier for the Observable
     *
     * @param changes The changes made or reason.
     */
    private void notify(Object changes) {
        setChanged();
        notifyObservers(changes);
    }

    /**
     * Checks to make sure that the input is safe to be processed.
     *
     * @param input the String given by the client.
     * @return a String which means that the String is not safe to be processed,
     *         otherwise false.
     */
    private String check(String input) {
        if (socket.isClosed() || input == null)
            return "isClosed";
        else if (input.equals("HALT") || input.equals("EXIT"))
            return input;
        return null;
    }

    /**
     * This inner class will inherit the Streamer interface. This class solely functions as
     * a two-way stream. Meaning that it can read and write.
     */
    private class ConnectionStream implements Streamer {
        private PrintWriter output;
        private BufferedReader input;

        /**
         * Prepares the stream by requesting streams from the Socket.
         */
        public ConnectionStream() throws IOException {
            setOutput(socket.getOutputStream());
            setInput(socket.getInputStream());
        }

        /**
         * Prepares the output stream to allow writing to a connection
         *
         * @param stream an OutputStream will provide the output connection to allow messages
         *               to be sent.
         */
        public void setOutput(OutputStream stream) {
            output = new PrintWriter(stream, true);
        }

        /**
         * Prepares the input stream to allow reading from a connection
         *
         * @param stream an InputStream that will provide the ability to read from
         *               client.
         */
        public void setInput(InputStream stream) {
            input = new BufferedReader(new InputStreamReader(stream));
        }

        /**
         * Requests a message from the Client
         *
         * @return a String that contains the message from the Client.
         */
        public String requestMessage() throws IOException {
            return input.readLine();
        }

        /**
         * Sends a message to the Client
         *
         * @param message A String that will most likely be sent as a message.
         * @param arg     An Object such as Exception for our case, these will provide different
         *                kind of messages, such as Errors for example.
         */
        public void sendMessage(String message, Object... arg) {
            if (arg.length == 0)
                output.println(message);
            else if (arg[0] instanceof Exception) {
                Exception e = (Exception) arg[0];
                output.println("[ERROR]" + e.getClass().getName() + " has been thrown.");
            }
        }

        /**
         * Closes off the output and input stream. It's generally a good idea to close the
         * stream as soon as it's no longer in use.
         */
        public void close() throws IOException {
            output.close();
            input.close();
        }
    }
}
