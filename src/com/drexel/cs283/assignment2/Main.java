package com.drexel.cs283.assignment2;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {

    public static void main(String[] args) throws Exception {

        if(args.length != 3 && args.length != 1) {
            System.out.println("Usage: java -jar chat.jar <Your Name> (Hostname to connect to) (Port to connect on)");
            System.out.println("Port 4000 is used by default, a port will be assigned if 4000 is not available.");
            System.out.println("Hostname and Port are optional - If not specified, you will act as a server.");
            return;
        }

        User currentUser = new User(args[0], MiniRSA.generateNewKeys());

        if (args.length == 3) {
            System.out.print("Connecting...");
            String hostname = args[1];
            int portNumber = Integer.parseInt(args[2]);

            //If the user specified an argument, it means they want to connect to someone.
            Socket clientSocket = new Socket(hostname, portNumber);
            System.out.print("\r");
            ChatHandler chatHandler = new ChatHandler(clientSocket, currentUser);
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
                ChatHandler chatHandler = new ChatHandler(socket, currentUser);
                Thread chatThread = new Thread(chatHandler);
                chatThread.start();
            }
        }
    }
}
