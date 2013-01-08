package com.hxdcml.server;

import com.google.gson.Gson;
import com.hxdcml.lang.Constant;
import com.hxdcml.protocol.ProtocolData;
import com.hxdcml.protocol.ProtocolMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

/**
 * User: Souleiman Ayoub
 * Date: 1/2/13
 * Time: 7:07 PM
 */
public class Client {
    public static ProtocolData make() {
        ProtocolData data = new ProtocolData(ProtocolData.MAGIC, ProtocolData.SEARCH);
        ProtocolMessage message = new ProtocolMessage();
        HashMap<String, Object[]> map = new HashMap<String, Object[]>();
        map.put(Constant.NAME, new Object[]{"Sleep", true});
        message.setObject(map);
        data.setMessage(message);
        return data;
    }
    public static void main(String[] args) {
        try {
            Socket client = new Socket("www.hxdcml.com", 9889);
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream())
            );
            Scanner scanner = new Scanner(System.in);
            String serverMessage;
            String clientMessage;
            ProtocolData data = make();
            out.println(new Gson().toJson(data, ProtocolData.class));
            String result = in.readLine();
            System.out.println(result);

            //Card[] cards = new Gson().fromJson(result, Card[].class);
            //System.out.println(Arrays.toString(cards));

            System.out.print("You: ");
            clientMessage = scanner.nextLine();

            if (!clientMessage.isEmpty() && client.isBound()) {
                out.println(clientMessage);
                serverMessage = in.readLine();
                System.out.println("Server Message: " + serverMessage);
            }
            out.close();
            in.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
