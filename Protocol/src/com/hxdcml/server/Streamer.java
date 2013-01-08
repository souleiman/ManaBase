package com.hxdcml.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * User: Souleiman Ayoub
 * Date: 1/3/13
 * Time: 12:00 AM
 */
public interface Streamer {
    /**
     * Prepares the output stream to allow writing to a connection
     *
     * @param stream an OutputStream will provide the output connection to allow messages
     *               to be sent.
     */
    public void setOutput(OutputStream stream);

    /**
     * Prepares the input stream to allow reading from a connection
     *
     * @param stream an InputStream that will provide the ability to read from
     *               client/server.
     */
    public void setInput(InputStream stream);

    public String requestMessage() throws IOException;

    public void sendMessage(String message, Object... arg);

    public void close() throws IOException;
}
