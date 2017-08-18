package com.drexel.cs283.assignment2;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class Handshake {

    private User user;
    private String publicKey;


    public Handshake(String handshakeString) throws Exception {
        String handshakeParts[] = handshakeString.split("~");

        if (handshakeParts.length != 3) {
            throw new Exception("Invalid number of handshake parts.");
        }

        if (handshakeParts[0].length() == 0 || handshakeParts[1].length() == 0 || handshakeParts[2].length() == 0) {
            throw new Exception("Malformed handshake");
        }

        Keys keys = new Keys();
        keys.setSharedKey(Long.parseLong(handshakeParts[1]));
        keys.setPublicKey(Long.parseLong(handshakeParts[2]));

        user = new User(handshakeParts[0], keys);
    }

    public static Handshake doHandshake(User currentUser, BufferedReader in, BufferedWriter out) {

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

    public User getUser() {
        return user;
    }
}
