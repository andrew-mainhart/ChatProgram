package com.drexel.cs283.assignment2;

import java.io.*;
import java.net.Socket;

public class ChatHandler implements Runnable, Callback {

    private Socket socket;
    private User currentUser;
    private User someOtherUser;

    private ChatReadHandler readHandler;
    private ChatWriteHandler writeHandler;


    public ChatHandler(Socket socket, User currentUser) {
        this.socket = socket;
        this.currentUser = currentUser;
    }

    @Override
    public void run() {

        try {

            /*
            String socketDescriptor = "" + socket.getPort();
            System.out.println("Accepted Chat on Port: " + socketDescriptor);
            */

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            Handshake handshake = Handshake.doHandshake(currentUser, in, out);
            someOtherUser = handshake.getUser();


            System.out.println("Connected to: " + someOtherUser.getUsername());

            readHandler = new ChatReadHandler(in, currentUser, someOtherUser);
            readHandler.registerCallback(this);
            Thread readThread = new Thread(readHandler);
            readThread.start();

            writeHandler = new ChatWriteHandler(out, currentUser, someOtherUser);
            writeHandler.registerCallback(this);
            Thread writeThread = new Thread(writeHandler);
            writeThread.start();

            readThread.join();
            writeThread.join();

            System.out.println("Chat closed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exitChat(String data) {

        readHandler.end();
        writeHandler.end();


        if (!socket.isClosed()) {
            try {
                socket.close();
                System.out.println("\rClosed Chat with: " + someOtherUser.getUsername());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!(data == null || data.equals("#quit") || data.equals(""))) {
            System.out.println("End data:" + data);
        }
    }
}
