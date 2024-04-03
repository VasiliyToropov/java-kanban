package tasks;

import java.util.ArrayList;

public class EpicTask extends Task {
    private final ArrayList<Integer> subtasksId;

    public EpicTask(String name, String description, int id, TaskStatus taskStatus) {
        super(name, description, id, taskStatus);
        subtasksId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    @Override
    public String toString() {
        return "Epic-задача{" +
                "Название='" + this.getName() + '\'' +
                ", Описание='" + this.getDescription() + '\'' +
                ", id=" + this.getId() +
                ", Статус=" + this.getTaskStatus() +
                ", ID подзадач :" + subtasksId + '}';
    }
}