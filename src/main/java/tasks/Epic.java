package main.java.tasks;

import main.java.service.Status;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtasksList;

    public Epic(String title, String description, Status status, ArrayList<Integer> subtasksList) {
        super(title, description, status);
        this.subtasksList = subtasksList;
    }

    public ArrayList<Integer> getSubtasksList() {
        return subtasksList;
    }

    public void cleanSubtaskIds() {
        subtasksList.clear();
    }
    public void removeSubtask(int id) {
        subtasksList.remove(subtasksList.indexOf(id));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksList, epic.subtasksList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksList);
    }

    @Override
    public String toString() {
        return "Epic{" + "id=" + getId() + ", subtasksList=" + subtasksList + ", title='" + getTitle()
                + '\'' + ", description='" + getDescription() + '\'' + ", status='" + getStatus() + '\'' + '}';
    }
}
