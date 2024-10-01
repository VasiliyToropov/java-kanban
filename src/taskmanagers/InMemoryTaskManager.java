package taskmanagers;

import historymanagers.HistoryManager;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;
import utility.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> taskMap = new HashMap<>();
    private final HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    private final HashMap<Integer, EpicTask> epicTaskMap = new HashMap<>();
    private Integer id = 0;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public Task getTaskById(Integer id) {
        historyManager.addToHistory(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public SubTask getSubTaskById(Integer id) {
        historyManager.addToHistory(subTaskMap.get(id));
        return subTaskMap.get(id);
    }

    @Override
    public EpicTask getEpicTaskById(Integer id) {
        historyManager.addToHistory(epicTaskMap.get(id));
        return epicTaskMap.get(id);
    }

    @Override
    public void createTask(Task task) {
        LocalDateTime startTime = task.getStartTime();
        Duration duration = task.getDuration();

        if (isOverlap(startTime, duration)) {
            return;
        }

        taskMap.put(id, task);
        id++;
    }


    @Override
    public void createSubTask(SubTask subTask) {
        LocalDateTime startTime = subTask.getStartTime();
        Duration duration = subTask.getDuration();

        if (subTask.getId().equals(subTask.getEpicTaskId())) {
            return;
        }

        if (isOverlap(startTime, duration)) {
            return;
        }

        for (Integer i : epicTaskMap.keySet()) {
            EpicTask epicTask = epicTaskMap.get(i);

            if (epicTask.getId().equals(subTask.getEpicTaskId())) {
                epicTask.getSubtasksId().add(subTask.getId());
                subTaskMap.put(id, subTask);
                id++;
            }
        }
    }

    @Override
    public void createEpicTask(EpicTask epicTask) {
        LocalDateTime startTime = epicTask.getStartTime();
        Duration duration = epicTask.getDuration();

        if (isOverlap(startTime, duration)) {
            return;
        }

        epicTaskMap.put(id, epicTask);
        id++;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public ArrayList<EpicTask> getAllEpicTasks() {
        return new ArrayList<>(epicTaskMap.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubtasks() {
        return new ArrayList<>(subTaskMap.values());
    }

    @Override
    public void deleteAllTasks() {
        for (Integer i : taskMap.keySet()) {
            historyManager.remove(i);
        }
        taskMap.clear();
    }

    @Override
    public void deleteSubTasks() {
        for (Integer i : subTaskMap.keySet()) {
            historyManager.remove(i);
        }
        subTaskMap.clear();
    }

    @Override
    public void deleteEpicTasks() {
        for (Integer i : epicTaskMap.keySet()) {
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
                EpicTask epicTask = epicTaskMap.get(subTask.getEpicTaskId());
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
                    EpicTask epicTask = epicTaskMap.get(oldTask.getEpicTaskId());

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
                        sub.setEpicTaskId(newTask.getId());
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
        taskMap.keySet().stream()
                .filter(i -> i.equals(id))
                .map(taskMap::get)
                .peek(task -> task.setTaskStatus(newStatus))
                .collect(Collectors.toSet());

    }

    @Override
    public void changeSubTaskStatus(Integer id, TaskStatus newStatus) {
        for (Integer i : subTaskMap.keySet()) {
            if (i.equals(id)) {
                SubTask subTask = subTaskMap.get(i);
                switch (newStatus) {
                    case NEW: {
                        subTask.setTaskStatus(TaskStatus.NEW);
                        EpicTask epicTask = getEpicTaskById(subTask.getEpicTaskId());
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
                        getEpicTaskById(subTask.getEpicTaskId()).setTaskStatus(TaskStatus.IN_PROGRESS);
                        return;
                    }
                    case DONE: {
                        subTask.setTaskStatus(TaskStatus.DONE);
                        EpicTask epicTask = getEpicTaskById(subTask.getEpicTaskId());
                        int count = 0;
                        for (Integer subTaskId : epicTask.getSubtasksId()) {
                            if (getSubTaskById(subTaskId).getTaskStatus() == TaskStatus.DONE) {
                                count++;
                            }
                        }
                        if (count == epicTask.getSubtasksId().size()) {
                            epicTask.setTaskStatus(TaskStatus.DONE);
                        } else {
                            epicTask.setTaskStatus(TaskStatus.IN_PROGRESS);
                        }
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

    public LocalDateTime getEndTime(Task task) {

        LocalDateTime endTime = task.getStartTime();

        //Если тип задачи EpicTask
        if (task instanceof EpicTask epicTask) {

            // Если у эпика нет подзадач то возвращаем endTime по умолчанию
            if (epicTask.getSubtasksId().isEmpty()) {
                endTime = endTime.plusMinutes(epicTask.getDuration().toMinutes());
                return endTime;
            }

            //Находим первый subTask, чтобы найти минимальное время начала выполнения
            SubTask subTaskFirst = subTaskMap.get(epicTask.getSubtasksId().getFirst());
            LocalDateTime minStartTime = subTaskFirst.getStartTime();

            //Задаем время начала от первого SubTask
            epicTask.setStartTime(minStartTime);

            Duration totalDuration = Duration.ZERO;

            for (Integer subTaskId : epicTask.getSubtasksId()) {
                SubTask subTask = subTaskMap.get(subTaskId);
                LocalDateTime subTaskStartTime = subTask.getStartTime();

                //Если время начала подзадачи раньше чему эпика, то меняем время начала задачи у Эпика
                if (subTaskStartTime.isBefore(minStartTime)) {
                    epicTask.setStartTime(subTaskStartTime);
                    minStartTime = subTaskStartTime;
                }

                totalDuration = totalDuration.plusMinutes(subTask.getDuration().toMinutes());
            }

            epicTask.setDuration(totalDuration);
        }

        endTime = endTime.plusMinutes(task.getDuration().toMinutes());
        return endTime;
    }

    public Set<Task> getPrioritizedTasks() {
        Comparator<Task> comparator = Comparator.comparing(Task::getStartTime);

        Set<Task> prioritizedTasks = new TreeSet<>(comparator);

        prioritizedTasks.addAll(getAllEpicTasks());
        prioritizedTasks.addAll(getAllSubtasks());
        prioritizedTasks.addAll(getAllTasks());

        return prioritizedTasks;
    }

    //Метод для опредления наложения времени выполнения задач
    public boolean isOverlap(LocalDateTime startTime, Duration duration) {
        Set<Task> prioritizedTasks = getPrioritizedTasks();
        LocalDateTime endTime = startTime.plusMinutes(duration.toMinutes());

        for (Task task : prioritizedTasks) {
            LocalDateTime taskStartTime = task.getStartTime();
            LocalDateTime taskEndTime = taskStartTime.plusMinutes(task.getDuration().toMinutes());

            boolean timeCondition1 = startTime.isBefore(taskEndTime) & startTime.isAfter(taskStartTime);
            boolean timeCondition2 = taskEndTime.isBefore(endTime) & taskEndTime.isAfter(startTime);

            if (timeCondition1 || timeCondition2) {
                return true;
            }
        }

        return false;
    }

}


