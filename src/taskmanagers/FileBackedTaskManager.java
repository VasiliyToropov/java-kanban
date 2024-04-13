package taskmanagers;

import exceptions.ManagerSaveException;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.*;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final File file;
    private final File historyFile;

    public FileBackedTaskManager() {
        // в filePath пишем файл из папки filesfortest
        String filePath = "src/filesfortest/fileForTask.csv";
        file = new File(filePath);

        //Создаем отдельный файл для истории
        String fileHistoryPath = "src/filesfortest/fileForHistory.csv";
        historyFile = new File(fileHistoryPath);
    }

    // Создаем запись в файл
    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            BufferedWriter historyWriter = new BufferedWriter(new FileWriter(historyFile))) {

            // Записываем данные в файл со списком задач
            writer.write("id,type,name,status,description, additionalInfo\n");

            for (Task task : getTaskMap().values()) {
                writer.write(toWrite(task));
            }

            for (EpicTask task : getEpicTaskMap().values()) {
                writer.write(toWrite(task));
            }

            for (SubTask task : getSubTaskMap().values()) {
                writer.write(toWrite(task));
            }

            // Записываем данные в файл для истории
            historyWriter.write("id,type,name,status,description\n");

            ArrayList<Task> historyTasks = getHistoryManager().getHistory();

            for (Task task : historyTasks) {
                historyWriter.write(toWrite(task));
            }

        } catch (IOException e) {
            try {
                throw new ManagerSaveException("Ошибка ввода-вывода");
            } catch (ManagerSaveException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    String toWrite(Task task) {
        String className = task.getClass().getName().substring(6);
        String taskDesc;

        int id = task.getId();
        String name = task.getName();
        TaskStatus status = task.getTaskStatus();
        String description = task.getDescription();


        if (className.equals("Task")) {
            taskDesc = id + "," + className + "," + name + "," + status + "," + description + "\n";

            return taskDesc;
        }

        if (className.equals("SubTask")) {
            SubTask subTask = (SubTask) task;
            int epicTaskId = subTask.getEpicTaskId();
            taskDesc = id + "," + className + "," + name + "," + status + "," + description + "," + epicTaskId + "\n";

            return taskDesc;
        }

        if (className.equals("EpicTask")) {
            EpicTask epicTask = (EpicTask) task;
            ArrayList<Integer> subTasksId = epicTask.getSubtasksId();
            taskDesc = id + "," + className + "," + name + "," + status + "," + description + ",";

            for (int subTaskId : subTasksId) {
                taskDesc = taskDesc.concat(subTaskId + " ");
            }

            taskDesc = taskDesc.concat("\n");

            return taskDesc;
        }

        return null;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();

            //Пропускаем строчку с названиями столбцов
            String taskString = reader.readLine();

            while (!taskString.isEmpty()) {
                taskString = reader.readLine();

                if (taskString == null) {
                    break;
                }

                Task addedTask = fromString(taskString);

                String addedTaskType = addedTask.getClass().getName().substring(6);

                if (addedTaskType.equals("Task")) {
                    fileBackedTaskManager.createTask(addedTask);
                }

                if (addedTaskType.equals("SubTask")) {
                    fileBackedTaskManager.createSubTask((SubTask) addedTask);
                }

                if (addedTaskType.equals("EpicTask")) {
                    fileBackedTaskManager.createEpicTask((EpicTask) addedTask);
                }

            }

            return fileBackedTaskManager;

        } catch (IOException exc) {
            System.out.println("Произошла ошибка ввода - вывода" + exc);
        }

        return null;
    }

    static Task fromString(String value) {
        String[] taskElements = value.split(",");

        int id = Integer.parseInt(taskElements[0]);
        String taskType = taskElements[1];
        String name = taskElements[2];
        TaskStatus status = TaskStatus.valueOf(taskElements[3]);
        String description = taskElements[4];

        if (taskType.equals("Task")) {
            return new Task(name, description, id, status);
        }

        if (taskType.equals("SubTask")) {
            int epicTaskId = Integer.parseInt(taskElements[5]);
            return new SubTask(name, description, id, status, epicTaskId);
        }

        if (taskType.equals("EpicTask")) {
            return new EpicTask(name, description, id, status);
        }

        return null;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void createEpicTask(EpicTask epicTask) {
        super.createEpicTask(epicTask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        save();
    }

    @Override
    public void deleteEpicTasks() {
        super.deleteEpicTasks();
        save();
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public void deleteEpicTaskById(Integer id) {
        super.deleteEpicTaskById(id);
        save();
    }

    @Override
    public void updateTask(Integer id, Task newTask) {
        super.updateTask(id, newTask);
        save();
    }

    @Override
    public void updateSubTask(Integer id, SubTask newTask) {
        super.updateSubTask(id, newTask);
        save();
    }

    @Override
    public void updateEpicTask(Integer id, EpicTask newTask) {
        super.updateEpicTask(id, newTask);
        save();
    }

    @Override
    public void changeTaskStatus(Integer id, TaskStatus newStatus) {
        super.changeTaskStatus(id, newStatus);
        save();
    }

    @Override
    public void changeSubTaskStatus(Integer id, TaskStatus newStatus) {
        super.changeSubTaskStatus(id, newStatus);
        save();
    }

    @Override
    public void changeEpicTaskStatus(Integer id, TaskStatus newStatus) {
        super.changeEpicTaskStatus(id, newStatus);
        save();
    }

    public Task getTaskById(Integer id) {
        Task task = getTaskMap().get(id);
        getHistoryManager().addToHistory(task);
        save();
        return task;
    }

    public SubTask getSubTaskById(Integer id) {
        SubTask task = getSubTaskMap().get(id);
        getHistoryManager().addToHistory(task);
        save();
        return task;
    }

    public EpicTask getEpicTaskById(Integer id) {
        EpicTask task = getEpicTaskMap().get(id);
        getHistoryManager().addToHistory(task);
        save();
        return task;
    }
}
