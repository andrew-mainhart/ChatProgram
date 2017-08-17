package com.drexel.cs283.assignment2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatWriteHandler implements Runnable, Callerback {

    private BufferedWriter out;
    private User currentUser;
    private User someOtherUser;

    private ArrayList<Callback> callbacks;
    private boolean chatEnded = false;

    public ChatWriteHandler(BufferedWriter out, User currentUser, User someOtherUser) {

        this.out = out;
        this.currentUser = currentUser;
        this.someOtherUser = someOtherUser;

        this.callbacks = new ArrayList<Callback>();
    }

    @Override
    public void run() {

        Scanner scanner = new Scanner(System.in);
        String userInput = "";

        System.out.print("--> ");

        //Encrypt with the other user's keys
        while (!chatEnded) {
            try {

                userInput = scanner.nextLine();

                String encryptedUserInput = MiniRSA.encryptString(userInput, someOtherUser.getKeys());
                out.write(encryptedUserInput + "\n");
                out.flush();

                if (userInput.equals("#quit")) {
                    this.end();
                    doCallbacks(null);
                }

            } catch (SocketException se) {
                //Ignore these,
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!chatEnded) {
                System.out.print("--> ");
            }
        }

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
