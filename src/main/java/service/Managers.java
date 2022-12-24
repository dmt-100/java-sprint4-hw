package main.java.service;

import main.java.intefaces.HistoryManager;
import main.java.intefaces.TaskManager;

public class Managers {

    static HistoryManager getDefaultHistory() { // ТЗ-4 Не совсем понял зачем нужен именно статический метод, но
        // вроде разобрался
        return new InMemoryHistoryManager();
    }

    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
