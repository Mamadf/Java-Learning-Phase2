package org.example.Observer;

public class User implements Observer {
    private String name;

    public User(String name) {
        this.name = name;
    }

    @Override
    public void notify(String message) {
        System.out.println("New message for "+ name + ": " + message);
    }
}
