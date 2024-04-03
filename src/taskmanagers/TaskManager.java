package taskmanagers;

import Tasks.*;
import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {
    Task getTaskById(Integer id);

    SubTask getSubTaskById(Integer id);

    EpicTask getEpicTaskById(Integer id);

    void createTask(Task task);

    void createSubTask(SubTask subTask);

    void createEpicTask(EpicTask epicTask);

    ArrayList<Task> printAllTasks();

    ArrayList<EpicTask> printAllEpicTasks();

    ArrayList<SubTask> printAllSubtasks();

    void deleteAllTasks();

    void deleteSubTasks();

    void deleteEpicTasks();

    void deleteTaskById(Integer id);

    void deleteSubTaskById(Integer id);

    void deleteEpicTaskById(Integer id);

    void updateTask(Integer id, Task newTask);

    void updateSubTask(Integer id, SubTask newTask);

    void updateEpicTask(Integer id, EpicTask newTask);

    void changeTaskStatus(Integer id, TaskStatus newStatus);

    void changeSubTaskStatus(Integer id, TaskStatus newStatus);

    void changeEpicTaskStatus(Integer id, TaskStatus newStatus);

    ArrayList<Integer> getSubTaskList(Integer epicTaskId);

    HashMap<Integer, Task> getTaskMap();

    HashMap<Integer, SubTask> getSubTaskMap();

    HashMap<Integer, EpicTask> getEpicTaskMap();
}
