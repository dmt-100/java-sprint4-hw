package main.java.intefaces;

import main.java.service.Status;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    // case 2: Получение списка всех задач.-------------------------------------
    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();

    // case 3: Удаление всех задач.---------------------------------------
    void taskClean();

    void epicClean();

    void subtaskClean();

    Task getTaskById(int taskId);

    Epic getEpicById(int taskId);

    Subtask getSubtaskById(int taskId);

    // case 5: Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
// "Это значит что в объекте Task заполнено поле id и мы можем его использовать для обновления объекта. поэтому во всех трёх методах должен на вход подаваться только объект задачи"
    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    // case 6: Удалить по идентификатору. ----------------------------------------
    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

    // case 7: Изменить статус --------------------------------------------------
    void changeStatusTask(int id, Status status);

    void changeStatusSubtask(int id, Status status);

    // case 8: Получение списка всех подзадач определённого эпика. -----------------------------
    ArrayList<Integer> getSubtaskList(int epicId);

    // метод обновления статуса епика
    void updateEpicStatus(int epicId);

    List<Task> historyList();

}
