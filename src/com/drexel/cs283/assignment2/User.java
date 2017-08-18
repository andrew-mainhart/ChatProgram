package com.drexel.cs283.assignment2;

public class User {

    private String username;
    private Keys keys;

    public User() {

    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, Keys keys) {
        this.username = username;
        this.keys = keys;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Keys getKeys() {
        return keys;
    }

    public void setKeys(Keys keys) {
        this.keys = keys;
    }

    @Override
    public String toString() {
        return getUsername() + "~" + getKeys();
    }
}
