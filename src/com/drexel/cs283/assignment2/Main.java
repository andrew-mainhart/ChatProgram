package com.drexel.cs283.assignment2;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class Main {

    public static void main(String[] args) throws Exception {

        if (args.length == 3) {
            System.out.println("Connecting...");
            String hostname = args[1];
            int portNumber = Integer.parseInt(args[2]);

            //If the user specified an argument, it means they want to connect to someone.
            Socket clientSocket = new Socket(hostname, portNumber);
            ChatHandler chatHandler = new ChatHandler(clientSocket, args[0]);
            Thread chatThread = new Thread(chatHandler);
            chatThread.start();
            chatThread.join();

        } else {

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
                ChatHandler chatHandler = new ChatHandler(socket, args[0]);
                Thread chatThread = new Thread(chatHandler);
                chatThread.start();
            }
        }
    }
}
