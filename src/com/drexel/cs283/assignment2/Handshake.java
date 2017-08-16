package com.drexel.cs283.assignment2;

public class Handshake {

    private String username;
    private String publicKey;

    public Handshake(String username, String publicKey){

        this.username = username;
        this.publicKey = publicKey;

    }

    public Handshake(String handshakeString) throws Exception {
        String handshakeParts[] = handshakeString.split("~");

        if (handshakeParts.length != 2) {
            throw new Exception("Invalid number of handshake parts.");
        }

        if (handshakeParts[0].length() == 0 || handshakeParts[1].length() == 0){
            throw new Exception("Malformed handshake");
        }

        this.username = handshakeParts[0];
        this.publicKey = handshakeParts[1];
    }

    public String getUsername(){
        return username;
    }

}
