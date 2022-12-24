package main.java.service;

public class IdCounter {
    private int id = 0;

    public  int getId() {
        return ++id;
    }

}
