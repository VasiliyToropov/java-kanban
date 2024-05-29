package tasks;

import java.time.LocalDateTime;

public class SubTask extends Task {

    private Integer epicTaskId;

    public SubTask(String name, String description, Integer id, TaskStatus taskStatus, Integer epicTaskID,
                   LocalDateTime startTime, long duration) {
        super(name, description, id, taskStatus, startTime, duration);
        this.epicTaskId = epicTaskID;
    }

    public Integer getEpicTaskId() {
        return epicTaskId;
    }

    public void setEpicTaskId(Integer epicTaskId) {
        this.epicTaskId = epicTaskId;
    }

    @Override
    public String toString() {

        return String.format("Подзадача{Название='%s', Описание='%s', id='%d', Статус='%s', Epic-задача='%d', " +
                        "Время начала выполнения='%s', Продолжительность='%s'}",
                this.getName(), this.getDescription(), this.getId(), this.getTaskStatus(), epicTaskId,
                this.getStartTime(), this.getDuration());
    }
}
