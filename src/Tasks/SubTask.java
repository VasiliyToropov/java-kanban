package Tasks;

public class SubTask extends Task {

    private Integer epicTaskID;

    public SubTask(String name, String description, Integer id, TaskStatus taskStatus, Integer epicTaskID) {
        super(name, description, id, taskStatus);
        this.epicTaskID = epicTaskID;
    }

    public Integer getEpicTaskID() {
        return epicTaskID;
    }

    public void setEpicTaskID(Integer epicTaskID) {
        this.epicTaskID = epicTaskID;
    }

    @Override
    public String toString() {
        return "Подзадача{" +
                "Название='" + this.getName() + '\'' +
                ", Описание='" + this.getDescription() + '\'' +
                ", id=" + this.getId() +
                ", Статус=" + this.getTaskStatus() +
                ", Epic-задача=" + epicTaskID + "}";
    }
}
