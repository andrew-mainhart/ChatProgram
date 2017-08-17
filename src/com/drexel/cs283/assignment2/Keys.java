package com.drexel.cs283.assignment2;

public class Keys {

    private long sharedKey;
    private long publicKey;
    private long privateKey;

    public Keys(){

    }

    public Keys(long sharedKey, long publicKey, long privateKey){
        this.sharedKey = sharedKey;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public Keys(long sharedKey, long publicKey){
        this.sharedKey = sharedKey;
        this.publicKey = publicKey;
    }

    public long getSharedKey() {
        return sharedKey;
    }

    public void setSharedKey(long sharedKey) {
        this.sharedKey = sharedKey;
    }

    public long getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(long publicKey) {
        this.publicKey = publicKey;
    }

    public long getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(long privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String toString() {
        return "" + getSharedKey() + "~" + getPublicKey();
    }
}
