package main.java.service;

import main.java.intefaces.HistoryManager;
import main.java.intefaces.TaskManager;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    IdCounter idCounter = new IdCounter();
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void addTask(Task task) {
        if (task.getId() == 0) { // если ноль то значит задача новая
            task.setId(idCounter.getId());
            if (task.getStatus().equals(Status.NEW) || task.getStatus().equals(Status.DONE) || task.getStatus().equals(
                    Status.IN_PROGRESS)) {
                tasks.put(task.getId(), task);
                System.out.println("Задача успешно добавлена");
            } else {
                System.out.println("Проверьте корректность значения статуса задачи: " + task.getTitle());
            }
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic.getId() == 0) { // если ноль то значит задача новая
            epic.setId(idCounter.getId());
            if (epic.getStatus().equals(Status.NEW) || epic.getStatus().equals(Status.DONE) || epic.getStatus().equals(Status.IN_PROGRESS)) {
                epics.put(epic.getId(), epic);
                System.out.println("Епик успешно добавлен");
            } else {
                System.out.println("Проверьте корректность значения статуса епика: " + epic.getTitle());
            }
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (subtask.getId() == 0) { // если ноль то значит задача новая
            subtask.setId(idCounter.getId());
            if (epics.containsKey(subtask.getEpicId())) {
                // что указанный в ней эпик существует."
                if (subtask.getStatus().equals(Status.NEW) || subtask.getStatus().equals(Status.DONE) || subtask.getStatus().equals(Status.IN_PROGRESS)) {
                    subtasks.put(subtask.getId(), subtask); // в мапе лежат <ключ - это id подзадачи, сама подзадача>
                    epics.get(subtask.getEpicId()).getSubtasksList().add(subtask.getId()); // добавляет id подзадачи в список эпика
                    System.out.println("Подзадача успешно добавлена");
                    updateEpicStatus(epics.get(subtask.getEpicId()).getId()); // обновляет статус эпика
                } else {
                    System.out.println("Проверьте корректность значения статуса подзадачи: " + subtask.getTitle());
                }

            }
        }
    }

    // case 2: Получение списка всех задач.-------------------------------------
    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> list = new ArrayList<>();
        for (Integer id : tasks.keySet()) {
            list.add(tasks.get(id));
        }
        return list;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> list = new ArrayList<>();
        for (Integer id : epics.keySet()) {
            list.add(epics.get(id));
        }
        return list;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> list = new ArrayList<>();
        for (Integer id : subtasks.keySet()) {
            list.add(subtasks.get(id));
        }
        return list;
    }

    // case 3: Удаление всех задач.---------------------------------------
    @Override
    public void taskClean() {
        tasks.clear();
        System.out.println("Удаление задач выполнено");
    }

    @Override
    public void epicClean() {
        subtasks.clear();
        epics.clear();
        System.out.println("Удаление эпиков выполнено");
    }

    @Override
    public void subtaskClean() {
        int subtaskCleanCounter = 0;
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            subtaskCleanCounter++;
        }
        if (epics.size() == subtaskCleanCounter) {
            System.out.println("Удаление задач выполнено");
        }
        for (Epic epic : epics.values()) {
            updateEpicStatus(epic.getId()); // "Нужно обновить статусы эпиков"
            System.out.println("Обновление статуса эпиков выполнено");
        }
    }

    // ТЗ-4
    // case 4:get методы-------------------------------------------------------------
    public Task getTaskById(int idInput) {
        Task task = null;
        if (tasks.get(idInput) != null) { // проверка на null
            historyManager.add(tasks.get(idInput)); // ТЗ-4 добавляем в лист просмотренную задачу
            task = tasks.get(idInput);
        }
        return task;
    }

    public Epic getEpicById(int idInput) {
        Epic epic = null;
        if (epics.get(idInput) != null) { // проверка на null
            historyManager.add(epics.get(idInput)); // ТЗ-4 добавляем в лист просмотренную задачу
            epic = epics.get(idInput);
        }
        return epic;
    }

    public Subtask getSubtaskById(int idInput) {
        Subtask subtask = null;
        if (subtasks.get(idInput) != null) { // проверка на null
            historyManager.add(subtasks.get(idInput)); // ТЗ-4 добавляем в лист просмотренную задачу
            // просмотренную задачу
            subtask = subtasks.get(idInput);
        }
        return subtask;
    }



    // case 5: Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
// "Это значит что в объекте Task заполнено поле id и мы можем его использовать для обновления объекта. поэтому во всех трёх методах должен на вход подаваться только объект задачи"
    @Override
    public void updateTask(Task task) {
        final int id = task.getId();
        for (Task t : tasks.values()) {
            if (t.getId() == id) { // "перед обновлением, надо проверить, что объект с заданным id есть в соответствующей мапе."
                tasks.put(t.getId(), task); // обновляем
                System.out.println("Обновление задачи прошло успешно");
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        final int id = epic.getId();
        for (Epic e : epics.values()) {
            if (e.getId() == id) {
                epics.put(e.getId(), epic); // обновляем
                System.out.println("Обновление прошло успешно");
            }
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        final int id = subtask.getId();
        final int epicId = subtask.getEpicId();
        if (epics.containsKey(epicId) && subtasks.containsKey(id)) { // "Нужно проверить что сабтаска и указанный в
            // ней эпик,
            // существуют."
            subtasks.put(id, subtask); // обновляем
            System.out.println("Обновление прошло успешно");
            updateEpicStatus(epicId); // "А после обновления сабтаски обновить статус эпика."
        }
    }

    // case 6: Удалить по идентификатору. ----------------------------------------
    @Override
    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            System.out.println("Задача удалена");
        }
    }

    @Override
    public void removeEpicById(int id) {
        ArrayList<Integer> al;
        al = epics.get(id).getSubtasksList(); // сохранаяем id-и подзадач в лист
        for (Subtask subtask : subtasks.values()) { // перебор подзадач
            for (int i = 0; i < al.size(); i++) { // перебор листа
                if (subtask.getId() == al.get(i)) { // если найден id подзадачи с значением из списка
                    subtasks.remove(subtask.getEpicId()); // то удаляем
                }
            }
        }
        if (epics.containsKey(id)) {
            epics.remove(id);
            System.out.println("Эпик удалён");
        }

    }

    @Override
    public void removeSubtaskById(int id) {
        // удаление подзадачи из списка который находится в епике
        if (epics.containsKey(subtasks.get(id).getEpicId())) {
            epics.get(subtasks.get(id).getEpicId()).removeSubtask(id); // " при удалении сабтаски нужно методом removeSubtask
            System.out.println("Удаление прошло успешно");
            // класса эпика удалить её из его списка"
            updateEpicStatus(subtasks.get(id).getEpicId()); // "и обновить статус эпика"
        }
        if (subtasks.containsKey(id)) {
            subtasks.remove(id);
            System.out.println("Подзадача удалена");
        }
    }

    // case 7: Изменить статус --------------------------------------------------
    @Override
    public void changeStatusTask(int id, Status status) {
        if (tasks.containsKey(id)) {
            tasks.get(id).setStatus(status);
            System.out.println("Статус изменён");
        } else {
            System.out.println("Задача с таким идентификатором не найдена");
        }
    }

    @Override
    public void changeStatusSubtask(int id, Status status) { // это главый метод менеджера так как изменяет статус
        // подзадачи для чего собственно и нужна эта программа=)
        if (subtasks.containsKey(id)) {
            subtasks.get(id).setStatus(status);
            System.out.println("Статус изменён");
            updateEpicStatus(subtasks.get(id).getEpicId());
        } else {
            System.out.println("Подзадача с таким идентификатором не найдена");
        }
    }

    // case 8: Получение списка всех подзадач определённого эпика. -----------------------------
    @Override
    public ArrayList<Integer> getSubtaskList(int epicId) {
        for (Integer id : epics.keySet()) {
            if (epics.get(id).getId() == epicId) {
                return epics.get(id).getSubtasksList();
            }
        }
        return null;
    }

    // метод обновления статуса епика
    @Override
    public void updateEpicStatus(int epicId) {
        if (epics.containsKey(epicId)) {
            boolean inProgress = true;
            // если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
            // *если у эпика нет подзадач
            if (epics.get(epicId).getSubtasksList().size() == 0) {
                epics.get(epicId).setStatus(Status.NEW);
                inProgress = false;
            }
            // *или все они имеют статус NEW
            if (epics.get(epicId).getSubtasksList().size() != 0) { // проверить на ноль
                int counterNew = 0;
                for (int i = 0; i < epics.get(epicId).getSubtasksList().size(); i++) { // перебор листа с id подзадач
                    if (subtasks.get(epics.get(epicId).getSubtasksList().get(i)).getStatus().equals(Status.NEW)) {
                        counterNew++;
                    }
                }
                if (epics.get(epicId).getSubtasksList().size() == counterNew) {
                    epics.get(epicId).setStatus(Status.NEW);
                    inProgress = false;
                }
            }

            // если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
            if (epics.get(epicId).getSubtasksList().size() != 0) { // проверить на ноль
                int counterDone = 0;
                for (int i = 0; i < epics.get(epicId).getSubtasksList().size(); i++) { // перебор листа с id подзадач
                    if (subtasks.get(epics.get(epicId).getSubtasksList().get(i)).getStatus().equals(Status.DONE)) {
                        counterDone++;
                    }
                }
                if (epics.get(epicId).getSubtasksList().size() == counterDone) {
                    epics.get(epicId).setStatus(Status.DONE);
                    inProgress = false;
                }

                // во всех остальных случаях статус должен быть IN_PROGRESS
                if (inProgress) {
                    epics.get(epicId).setStatus(Status.IN_PROGRESS);
                }
                epics.put(epics.get(epicId).getId(), epics.get(epicId));
                System.out.println("Обновление списка эпика прошло успешно");
            }
        } else {
            System.out.println("Епик с таким id не найден");
        }
    }

    // ТЗ-4
    // case 9:
    public List<Task> historyList() {
        List<Task> list = new LinkedList<>();
        for (Task task : historyManager.getHistory()) {
            list.add(task);
        }
        return list;
    }
}

