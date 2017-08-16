package com.drexel.cs283.assignment2;

import java.io.*;
import java.net.Socket;

public class ChatHandler implements Runnable, Callback {

    private Socket socket;

    private ChatReadHandler readHandler;
    private ChatWriteHandler writeHandler;


    public ChatHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            String socketDescriptor = "" + socket.getPort();
            System.out.println("Accepted Chat on Port: " + socketDescriptor);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            Handshake handshake = doHandshake(in, out);

            readHandler = new ChatReadHandler(in, handshake.getUsername());
            readHandler.registerCallback(this);
            Thread readThread = new Thread(readHandler);
            readThread.start();

            writeHandler = new ChatWriteHandler(out, socketDescriptor);
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
            out.write("andrew~1034");
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
