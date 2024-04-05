package tasks;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private Integer id;
    private TaskStatus taskStatus;

    public Task(String name, String description, Integer id, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.id = id;
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

    @Override
    public String toString() {
        return "Обычная задача{" +
                "Название='" + name + '\'' +
                ", Описание='" + description + '\'' +
                ", id=" + id +
                ", Статус=" + taskStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && Objects.equals(id, task.id) && taskStatus == task.taskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, taskStatus);
    }
}