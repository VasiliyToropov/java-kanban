import taskmanagers.FileBackedTaskManager;
import taskmanagers.TaskManager;
import tasks.*;
import utility.Managers;

import java.io.File;
import java.io.IOException;

public class Main {
    static TaskManager taskManager;

    static {
        try {
            taskManager = Managers.getDefault();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        //Создаем 6 задач для записи в файл
        createTasks((FileBackedTaskManager) taskManager);
        // Проводим разные операции с задачами
        doSomeOperations((FileBackedTaskManager) taskManager);
        // Проверяем чтение и создание менеджера из файла
        checkReadFromFile();
    }

    private static void createTasks(FileBackedTaskManager manager) {
        manager.createTask(new Task("Обычная задача - 1", "Описание обычной задачи",
                manager.getId(), TaskStatus.NEW)); // id = 0
        manager.createTask(new Task("Обычная задача - 2", "Описание обычной задачи",
                manager.getId(), TaskStatus.NEW)); // id = 1
        manager.createEpicTask(new EpicTask("Эпическая задача - 1", "Описание эпической задачи",
                manager.getId(), TaskStatus.NEW)); // id = 2
        manager.createSubTask(new SubTask("Подзадача - 1", "Описание подзадачи",
                manager.getId(), TaskStatus.NEW, 2)); // id = 3
        manager.createSubTask(new SubTask("Подзадача - 2", "Описание подзадачи",
                manager.getId(), TaskStatus.NEW, 2)); // id = 4
        manager.createSubTask(new SubTask("Подзадача - 3", "Описание подзадачи",
                manager.getId(), TaskStatus.NEW, 2)); // id = 5

    }

    private static void doSomeOperations(FileBackedTaskManager manager) {
        manager.deleteTaskById(1);
        manager.deleteSubTaskById(5);
    }

    private static void checkReadFromFile() {
        File file = new File("src/filesfortest/test.csv");
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);

        for (Task task : fileBackedTaskManager.printAllTasks()) {
            System.out.println(task);
        }

        for (Task task : fileBackedTaskManager.printAllEpicTasks()) {
            System.out.println(task);
        }

        for (Task task : fileBackedTaskManager.printAllSubtasks()) {
            System.out.println(task);
        }

        // Проверяем работу истории
        fileBackedTaskManager.getTaskById(0);
        fileBackedTaskManager.getTaskById(3);
        fileBackedTaskManager.getEpicTaskById(4);
    }
}
