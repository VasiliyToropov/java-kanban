package TaskManagers;

import HistoryManagers.*;
import Tasks.*;
import UtilityClasses.Managers;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> taskMap = new HashMap<>();
    private final HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    private final HashMap<Integer, EpicTask> epicTaskMap = new HashMap<>();
    private Integer id = 0;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public Task getTaskById(Integer ID) {
        historyManager.addToHistory(taskMap.get(ID));
        return taskMap.get(ID);
    }

    @Override
    public SubTask getSubTaskById(Integer ID) {
        historyManager.addToHistory(subTaskMap.get(ID));
        return subTaskMap.get(ID);
    }

    @Override
    public EpicTask getEpicTaskById(Integer ID) {
        historyManager.addToHistory(epicTaskMap.get(ID));
        return epicTaskMap.get(ID);
    }

    @Override
    public void createTask(Task task) {
        taskMap.put(id, task);
        id++;
    }

    @Override
    public void createSubTask(SubTask subTask) {
        if (subTask.getId().equals(subTask.getEpicTaskID())) {
            return;
        }

        for (Integer i : epicTaskMap.keySet()) {
            EpicTask epicTask = epicTaskMap.get(i);

            if (epicTask.getId().equals(subTask.getEpicTaskID())) {
                epicTask.getSubtasksId().add(subTask.getId());
                subTaskMap.put(id, subTask);
                id++;
            }
        }
    }

    @Override
    public void createEpicTask(EpicTask epicTask) {
        epicTaskMap.put(id, epicTask);
        id++;
    }

    @Override

    public ArrayList<Task> printAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public ArrayList<EpicTask> printAllEpicTasks() {
        return new ArrayList<>(epicTaskMap.values());
    }

    @Override
    public ArrayList<SubTask> printAllSubtasks() {
        return new ArrayList<>(subTaskMap.values());
    }

    @Override
    public void deleteAllTasks() {
        for(Integer i: taskMap.keySet()) {
            historyManager.remove(i);
        }
        taskMap.clear();
    }

    @Override
    public void deleteSubTasks() {
        for(Integer i: subTaskMap.keySet()) {
            historyManager.remove(i);
        }
        subTaskMap.clear();
    }

    @Override
    public void deleteEpicTasks() {
        for(Integer i: epicTaskMap.keySet()) {
            historyManager.remove(i);
        }
        epicTaskMap.clear();
        deleteSubTasks();
    }

    @Override
    public void deleteTaskById(Integer id) {
        for (Integer i : taskMap.keySet()) {
            if (id.equals(i)) {
                taskMap.remove(i);
                historyManager.remove(i);
                return;
            }
        }
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        for (Integer i : subTaskMap.keySet()) {
            if (id.equals(i)) {
                SubTask subTask = subTaskMap.get(i);
                EpicTask epicTask = epicTaskMap.get(subTask.getEpicTaskID());
                epicTask.getSubtasksId().remove(i);
                subTaskMap.remove(i);
                historyManager.remove(i);
                return;
            }
        }
    }

    @Override
    public void deleteEpicTaskById(Integer id) {
        for (Integer i : epicTaskMap.keySet()) {
            if (id.equals(i)) {
                EpicTask epictask = epicTaskMap.get(i);
                for (Integer subtasksId : epictask.getSubtasksId()) {
                    subTaskMap.remove(subtasksId);
                    historyManager.remove(subtasksId);
                }
                epicTaskMap.remove(i);
                historyManager.remove(i);
                return;
            }
        }
    }

    @Override
    public void updateTask(Integer id, Task newTask) {
        for (Integer i : taskMap.keySet()) {
            if (i.equals(id)) {
                if (isIdFree(id, newTask)) {
                    taskMap.remove(i);
                    taskMap.put(newTask.getId(), newTask);
                }
                return;
            }
        }
    }

    @Override
    public void updateSubTask(Integer id, SubTask newTask) {
        for (Integer i : subTaskMap.keySet()) {
            if (i.equals(id)) {
                if (isIdFree(id, newTask)) {
                    SubTask oldTask = subTaskMap.get(i);
                    EpicTask epicTask = epicTaskMap.get(oldTask.getEpicTaskID());

                    epicTask.getSubtasksId().remove(oldTask.getId());
                    epicTask.getSubtasksId().add(newTask.getId());

                    subTaskMap.remove(i);
                    subTaskMap.put(newTask.getId(), newTask);

                    changeSubTaskStatus(newTask.getId(), newTask.getTaskStatus());
                }
                return;
            }
        }
    }

    @Override
    public void updateEpicTask(Integer id, EpicTask newTask) {
        for (Integer i : epicTaskMap.keySet()) {
            if (i.equals(id)) {
                if (isIdFree(id, newTask)) {
                    EpicTask oldTask = epicTaskMap.get(i);

                    for (Integer subTaskId : oldTask.getSubtasksId()) {
                        SubTask sub = subTaskMap.get(subTaskId);
                        sub.setEpicTaskID(newTask.getId());
                        newTask.getSubtasksId().add(subTaskId);
                    }

                    epicTaskMap.remove(i);
                    epicTaskMap.put(newTask.getId(), newTask);

                    changeEpicTaskStatus(newTask.getId(), newTask.getTaskStatus());
                }
                return;
            }
        }
    }

    @Override
    public void changeTaskStatus(Integer id, TaskStatus newStatus) {
        for (Integer i : taskMap.keySet()) {
            if (i.equals(id)) {
                taskMap.get(i).setTaskStatus(newStatus);
                return;
            }
        }
    }

    @Override
    public void changeSubTaskStatus(Integer id, TaskStatus newStatus) {
        for (Integer i : subTaskMap.keySet()) {
            if (i.equals(id)) {
                SubTask subTask = subTaskMap.get(i);
                switch (newStatus) {
                    case NEW: {
                        subTask.setTaskStatus(TaskStatus.NEW);
                        EpicTask epicTask = getEpicTaskById(subTask.getEpicTaskID());
                        int count = 0;
                        for (Integer subTaskId : epicTask.getSubtasksId()) {
                            if (getSubTaskById(subTaskId).getTaskStatus() == TaskStatus.NEW) {
                                count++;
                            }
                        }
                        if (count == epicTask.getSubtasksId().size()) {
                            epicTask.setTaskStatus(TaskStatus.NEW);
                        } else {
                            epicTask.setTaskStatus(TaskStatus.IN_PROGRESS);
                        }
                        return;
                    }
                    case IN_PROGRESS: {
                        subTask.setTaskStatus(TaskStatus.IN_PROGRESS);
                        getEpicTaskById(subTask.getEpicTaskID()).setTaskStatus(TaskStatus.IN_PROGRESS);
                        return;
                    }
                    case DONE: {
                        subTask.setTaskStatus(TaskStatus.DONE);
                        getEpicTaskById(subTask.getEpicTaskID()).setTaskStatus(TaskStatus.IN_PROGRESS);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void changeEpicTaskStatus(Integer id, TaskStatus newStatus) {
        for (Integer i : epicTaskMap.keySet()) {
            if (i.equals(id)) {
                EpicTask epicTask = epicTaskMap.get(i);

                switch (newStatus) {
                    case NEW: {
                        epicTask.setTaskStatus(TaskStatus.NEW);
                        for (Integer subTaskId : epicTask.getSubtasksId()) {
                            getSubTaskById(subTaskId).setTaskStatus(TaskStatus.NEW);
                        }
                        return;
                    }

                    case IN_PROGRESS: {
                        int count = 0;
                        for (Integer subTaskId : epicTask.getSubtasksId()) {
                            boolean doneAndInProgress = (getSubTaskById(subTaskId).getTaskStatus() == TaskStatus.DONE)
                                    || (getSubTaskById(subTaskId).getTaskStatus() == TaskStatus.IN_PROGRESS);
                            if (doneAndInProgress) {
                                count++;
                            }
                        }
                        if (count >= 1) {
                            epicTask.setTaskStatus(TaskStatus.IN_PROGRESS);
                        }
                        return;
                    }

                    case DONE: {
                        int count = 0;
                        for (Integer subTaskId : epicTask.getSubtasksId()) {
                            if (getSubTaskById(subTaskId).getTaskStatus() == TaskStatus.DONE) {
                                count++;
                            }
                        }
                        if (count == epicTask.getSubtasksId().size()) {
                            epicTask.setTaskStatus(TaskStatus.DONE);
                        }
                        return;
                    }
                }
            }
        }
    }

    @Override
    public ArrayList<Integer> getSubTaskList(Integer epicTaskId) {
        return epicTaskMap.get(epicTaskId).getSubtasksId();
    }

    //Метод для определения занятости выбранного id в других списках.
    public boolean isIdFree(Integer id, Task task) {
        if (id.equals(task.getId())) {
            return true;
        }

        if (taskMap.containsKey(task.getId())) {
            return false;
        }

        if (subTaskMap.containsKey(task.getId())) {
            return false;
        }

        return !epicTaskMap.containsKey(task.getId());
    }

    @Override
    public HashMap<Integer, Task> getTaskMap() {
        return taskMap;
    }

    @Override
    public HashMap<Integer, SubTask> getSubTaskMap() {
        return subTaskMap;
    }

    @Override
    public HashMap<Integer, EpicTask> getEpicTaskMap() {
        return epicTaskMap;
    }

    public Integer getId() {
        return id;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

}


