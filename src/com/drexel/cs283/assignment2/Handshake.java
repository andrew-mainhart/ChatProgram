package com.drexel.cs283.assignment2;

public class Handshake {

    private User user;
    private String publicKey;


    public Handshake(String handshakeString) throws Exception {
        String handshakeParts[] = handshakeString.split("~");

        if (handshakeParts.length != 3) {
            throw new Exception("Invalid number of handshake parts.");
        }

        if (handshakeParts[0].length() == 0 || handshakeParts[1].length() == 0 || handshakeParts[2].length() == 0){
            throw new Exception("Malformed handshake");
        }

        Keys keys = new Keys();
        keys.setSharedKey(Long.parseLong(handshakeParts[1]));
        keys.setPublicKey(Long.parseLong(handshakeParts[2]));

        user = new User(handshakeParts[0], keys);
    }

    public User getUser() {
        return user;
    }
}
