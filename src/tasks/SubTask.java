package tasks;

public class SubTask extends Task {

    private Integer epicTaskId;

    public SubTask(String name, String description, Integer id, TaskStatus taskStatus, Integer epicTaskID) {
        super(name, description, id, taskStatus);
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
        return "Подзадача{" +
                "Название='" + this.getName() + '\'' +
                ", Описание='" + this.getDescription() + '\'' +
                ", id=" + this.getId() +
                ", Статус=" + this.getTaskStatus() +
                ", Epic-задача=" + epicTaskId + "}";
    }
}
