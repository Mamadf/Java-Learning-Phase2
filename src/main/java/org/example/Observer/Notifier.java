package org.example.Observer;

import java.util.ArrayList;

public class Notifier implements Subject{
    private final ArrayList<Observer> users = new ArrayList<>();

    public Notifier() {
        User user = new User("Mamad");
        User user2 = new User("Ghasem");
        this.addUser(user);
        this.addUser(user2);
    }

    @Override
    public void addUser(Observer observer) {
        users.add(observer);
    }

    @Override
    public void removeUser(Observer observer) {
        users.remove(observer);

    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : users) {
            observer.notify(message);
        }
    }
}
