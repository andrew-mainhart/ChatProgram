package com.drexel.cs283.assignment2;

import java.io.BufferedReader;
import java.net.SocketException;
import java.util.ArrayList;

public class ChatReadHandler implements Runnable, Callerback {

    private BufferedReader in;
    private User currentUser;
    private User someOtherUser;

    private ArrayList<Callback> callbacks;
    private boolean chatEnded = false;

    public ChatReadHandler(BufferedReader in, User currentUser, User someOtherUser) {

        this.in = in;
        this.currentUser = currentUser;
        this.someOtherUser = someOtherUser;

        this.callbacks = new ArrayList<Callback>();

    }

    @Override
    public void run() {

        String socketInput = "";


        try {
            //Decrypt with current user's keys
            while (!(socketInput = MiniRSA.decryptString(in.readLine(), currentUser.getKeys())).equals("#quit") && !chatEnded) {
                System.out.println("\r" + someOtherUser.getUsername() + ": " + socketInput);
                System.out.print("--> ");
            }

            System.out.print("\r");

        } catch (SocketException se) {
            System.out.println("\rSocket has been closed. — Suppressing Error In ReadHandler run()");
        } catch (NullPointerException npe) {
            System.out.println("\r" + someOtherUser.getUsername() + " has lost connection.");
        } catch (Exception e) {

            e.printStackTrace();
        }

        doCallbacks(socketInput);
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

    public void end() {
        chatEnded = true;
    }

}
