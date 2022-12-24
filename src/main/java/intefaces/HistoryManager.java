package main.java.intefaces;

import main.java.tasks.Task;
import java.util.List;

public interface HistoryManager {

    void add(Task task); // функция этого метода перешла в класс inMemoryTaskManager (см. со 131 строки)
    // Managers.getDefaultHistory().getHistory().add(tasks.get(idInput)); // ТЗ-4 добавляем в лист просмотренную

    List<Task> getHistory();

    void checkList();

}
