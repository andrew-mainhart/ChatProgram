package com.drexel.cs283.assignment2;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatWriteHandler implements Runnable, Callerback {

    private BufferedWriter out;
    private String descriptor;
    private ArrayList<Callback> callbacks;
    private boolean chatEnded = false;

    public ChatWriteHandler(BufferedWriter out) {

        this.out = out;
        this.callbacks = new ArrayList<Callback>();
    }

    @Override
    public void run() {

        Scanner scanner = new Scanner(System.in);
        String userInput = "";

        System.out.print("--> ");

        while (!(userInput = scanner.nextLine()).equals("#quit") && !chatEnded) {
            try {
                out.write(userInput + "\n");
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.print("--> ");
        }

        scanner.close();
        doCallbacks(userInput);
    }

    public void end() {
        chatEnded = true;
    }

    @Override
    public void registerCallback(Callback callback) {
        this.callbacks.add(callback);
    }

    private void doCallbacks(String data) {
        for (Callback callback : callbacks) {
            callback.exitChat(data);
        }
    }
}
