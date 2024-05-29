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

        return String.format("Epic-задача{Название='%s', Описание='%s', id='%d', Статус='%s', ID подзадач='%s', " +
                        "Время начала выполнения='%s', Продолжительность='%s'}",
                this.getName(), this.getDescription(), this.getId(), this.getTaskStatus(), subtasksId,
                this.getStartTime(), this.getDuration());
    }
}