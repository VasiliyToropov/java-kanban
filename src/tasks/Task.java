package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private Integer id;
    private TaskStatus taskStatus;
    private Duration duration;
    private LocalDateTime startTime;


    public Task(String name, String description, Integer id, TaskStatus taskStatus, LocalDateTime startTime,
                long duration) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.id = id;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "Обычная задача{" +
                "Название='" + name + '\'' +
                ", Описание='" + description + '\'' +
                ", id=" + id +
                ", Статус=" + taskStatus +
                ", Время начала выполнения=" + startTime +
                ", Продолжительность=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) &&
                Objects.equals(id, task.id) && taskStatus == task.taskStatus && Objects.equals(duration, task.duration)
                && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, taskStatus, duration, startTime);
    }
}