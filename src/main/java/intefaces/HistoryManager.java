package main.java.intefaces;

import main.java.tasks.Task;
import java.util.List;

public interface HistoryManager {

    void add(Task task); // функция этого метода перешла в класс inMemoryTaskManager (см. со 131 строки)

    List<Task> getHistory();
}
