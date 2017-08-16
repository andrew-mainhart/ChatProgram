package com.drexel.cs283.assignment2;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws Exception {

        int port = 4000;
        ServerSocket serverSocket = null;

        while (serverSocket == null) {
            try {
                serverSocket = new ServerSocket(port);
            } catch (BindException be) {
                //be.printStackTrace();
            }
            port++;
        }

        System.out.println("Accepting Connections on Port: " + serverSocket.getLocalPort());


        int numberOfConnections = 0;

        while (true) {
            Socket socket = serverSocket.accept();
            ChatHandler chatHandler = new ChatHandler(socket);
            Thread chatThread = new Thread(chatHandler);
            chatThread.start();
        }

    }
}
