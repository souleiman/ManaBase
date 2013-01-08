package com.hxdcml.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

/**
 * This class will act as a Server, it is capable of accepting multiple Sockets,
 * allowing multiple nodes of processing. Each nodes will have an Observable,
 * which will be Observed by this class. If something happens,
 * the Observer will do something about it. In order to maintain memory,
 * when the Sockets closes, ends, or no longer in use. It will be removed,
 * and provides more space for new PoolNode spawns.
 *
 * User: Souleiman Ayoub
 * Date: 1/2/13
 * Time: 6:31 PM
 */
public class ServerPool extends ServerSocket implements Observer {
    private Hashtable<String, PoolNode> buffer;

    /**
     * Opens a ServerSocket based on the port provided.
     * @param port Listen on this given port.
     */
    public ServerPool(int port) throws IOException {
        super(port);
        buffer = new Hashtable<String, PoolNode>(100);
    }

    /**
     * This overridden method will call in it's super class (ServerSocket) and have it
     * invoke the accept(), as we are to expect a Socket, we will insert this socket into a
     * PoolNode, have this instance Observe that node, and add it into the buffer. Finally,
     * have the PoolNode start it's thread.
     *
     * @return Returns the Socket that was recently inserted into the PoolNode.
     */
    @Override
    public Socket accept() throws IOException {
        Socket socket = super.accept();
        PoolNode node = new PoolNode(socket);
        node.addObserver(this);
        buffer.put(node.hostname, node);
        node.start();
        System.out.println("Buffer Size: " + buffer.size());
        return socket;
    }

    /**
     * Prepares the ServerPool to start accepting connections from the client.
     *
     * @throws IOException
     */
    public void start() throws IOException {
        System.out.println("ServerPool successfully booted.");
        while (isBound())
            accept();
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o      the observable object.
     * @param reason an argument passed to the <code>notifyObservers</code> and it will
     *               indicate the reason for update.
     */
    @Override
    public void update(Observable o, Object reason) {
        PoolNode node = (PoolNode) o;
        System.out.println(exitMessage(node, reason));
        if (reason.equals("EXIT") || reason.equals("isClosed")) {
            System.out.println(node.hostname + " has been removed.");
            remove(node);
        } else if (reason.equals("HALT")) {
            removeAll();
            System.out.println("All nodes have been closed.");
            System.exit(0);
        }
    }

    /**
     * Prepares an exit message based on the given reason.
     *
     * @param node PoolNode that will be used to extract the hostname.
     * @param reason The reason why this node is exiting.
     * @return String that will have a message to be outputted.
     */
    private String exitMessage(PoolNode node, Object reason) {
        String message = node.hostname;
        if (reason.equals("EXIT") || reason.equals("isClosed")) {
            message += " has exited.";
        } else if (reason.equals("HALT")) {
            message += " has requested to put the application to halt.";
        } else {
            return "[UNKNOWN REASON!]";
        }
        return message;
    }

    /**
     * Stop the node, and removes the node from the buffer.
     * @param node PoolNode instance that will be stopped and removed.
     */
    public void remove(PoolNode node) {
        node.stop();
        buffer.remove(node.hostname);
    }

    /**
     * Removes and stops all node.
     */
    public void removeAll() {
        for (String hostname : buffer.keySet()) {
            PoolNode node = buffer.get(hostname);
            node.stop();
        }
        buffer.clear();
    }

    /**
     * Closes this socket.
     * <p/>
     * Any thread currently blocked in {@link #accept()} will throw
     * a {@link java.net.SocketException}.
     * <p/>
     * <p> If this socket has an associated channel then the channel is closed
     * as well.
     * <p/>
     * Note! As this method is overridden, we will first remove all PoolNode instance that
     * are currently connected.
     */
    @Override
    public void close() {
        try {
            removeAll();
            super.close();
        } catch (IOException e) {
            //Ignored
        }
    }

    public static void main(String[] args) throws IOException {
        ServerPool pool = new ServerPool(9889);
        pool.start();
        pool.close();
    }
}
