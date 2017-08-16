package com.drexel.cs283.assignment2;

import java.io.BufferedReader;
import java.net.SocketException;
import java.util.ArrayList;

public class ChatReadHandler implements Runnable, Callerback {

    private BufferedReader in;
    private String descriptor;
    private ArrayList<Callback> callbacks;
    private boolean chatEnded = false;

    public ChatReadHandler(BufferedReader in, String descriptor) {

        this.in = in;
        this.descriptor = descriptor;
        this.callbacks = new ArrayList<Callback>();

    }

    @Override
    public void run() {

        String socketInput = "";


        try {
            while (!(socketInput = in.readLine()).equals("#quit") && !chatEnded) {
                System.out.println("\r" + descriptor + ": " + socketInput);
                System.out.print("--> ");
            }

            System.out.print("\r");

        } catch (SocketException se) {
            System.out.println("\rSocket has been closed. — Suppressing Error In ReadHandler run()");
        } catch (NullPointerException npe) {
            System.out.println("\r" + descriptor + " has lost connection.");
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
