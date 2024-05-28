package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EpicTask extends Task {
    private final ArrayList<Integer> subtasksId;

    public EpicTask(String name, String description, int id, TaskStatus taskStatus, LocalDateTime startTime,
                    long duration) {
        super(name, description, id, taskStatus, startTime, duration);
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
                ", ID подзадач :" + subtasksId +
                ", Время начала выполнения=" + this.getStartTime() +
                ", Продолжительность=" + this.getDuration() +
                '}';
    }
}