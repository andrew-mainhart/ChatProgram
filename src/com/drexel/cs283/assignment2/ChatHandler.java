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

            Handshake handshake = doHandshake(in, out);
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

    public Handshake doHandshake(BufferedReader in, BufferedWriter out) {


        try {
            out.write(currentUser + "\n");
            out.flush();
        } catch (Exception e) {
            System.out.println("Failed to write handshake.");
            return null;
        }

        try {
            return new Handshake(in.readLine());
        } catch (Exception e) {
            System.out.println("Failed to read handshake.");
            return null;
        }

    }


    @Override
    public void exitChat(String data) {

        readHandler.end();
        writeHandler.end();


        if (!socket.isClosed()) {
            try {
                socket.close();
                System.out.println("Closed Chat on Port: " + socket.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!(data == null || data.equals("#quit") || data.equals(""))) {
            System.out.println("End data:" + data);
        }
    }
}
