import taskmanagers.FileBackedTaskManager;
import taskmanagers.TaskManager;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;
import utility.Managers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

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
        // Создаем даты для задач

        LocalDateTime startForTask1 = LocalDateTime.of(2024, 5, 5, 12, 0);
        long durationForTask1 = 30;

        LocalDateTime startForTask2 = LocalDateTime.of(2024, 5, 5, 12, 15);
        long durationForTask2 = 30;

        LocalDateTime startForEpic = LocalDateTime.of(2024, 5, 3, 14, 0);
        long durationForEpic = 60;

        LocalDateTime startForSubTask1 = LocalDateTime.of(2024, 5, 1, 15, 0);
        long durationForSubTask1 = 10;

        LocalDateTime startForSubTask2 = LocalDateTime.of(2024, 5, 1, 16, 30);
        long durationForSubTask2 = 20;

        LocalDateTime startForSubTask3 = LocalDateTime.of(2024, 5, 1, 16, 50);
        long durationForSubTask3 = 60;

        manager.createTask(new Task("Обычная задача - 1", "Описание обычной задачи",
                manager.getId(), TaskStatus.NEW, startForTask1, durationForTask1)); // id = 0

        manager.createTask(new Task("Обычная задача - 2", "Описание обычной задачи",
                manager.getId(), TaskStatus.NEW, startForTask2, durationForTask2)); // id = 1

        manager.createEpicTask(new EpicTask("Эпическая задача - 1", "Описание эпической задачи",
                manager.getId(), TaskStatus.NEW, startForEpic, durationForEpic)); // id = 2

        manager.createSubTask(new SubTask("Подзадача - 1", "Описание подзадачи",
                manager.getId(), TaskStatus.NEW, 1, startForSubTask1, durationForSubTask1)); // id = 3

        manager.createSubTask(new SubTask("Подзадача - 2", "Описание подзадачи",
                manager.getId(), TaskStatus.NEW, 1, startForSubTask2, durationForSubTask2)); // id = 4

        manager.createSubTask(new SubTask("Подзадача - 3", "Описание подзадачи",
                manager.getId(), TaskStatus.NEW, 1, startForSubTask3, durationForSubTask3)); // id = 5

    }

    private static void doSomeOperations(FileBackedTaskManager manager) {

        for (Task task : manager.getAllTasks()) {
            System.out.print(task.getName() + " ");
            System.out.print("Время начала: " + task.getStartTime() + " ");
            System.out.print("Продолжительность: " + task.getDuration() + " ");
            System.out.println("Дата окончания: " + manager.getEndTime(task));
        }

        for (Task task : manager.getAllEpicTasks()) {
            System.out.print(task.getName() + " ");
            System.out.print("Время начала: " + task.getStartTime() + " ");
            System.out.print("Продолжительность: " + task.getDuration() + " ");
            System.out.println("Дата окончания: " + manager.getEndTime(task));
        }

        for (Task task : manager.getAllSubtasks()) {
            System.out.print(task.getName() + " ");
            System.out.print("Время начала: " + task.getStartTime() + " ");
            System.out.print("Продолжительность: " + task.getDuration() + " ");
            System.out.println("Дата окончания: " + manager.getEndTime(task));
        }

        //Печатаем еще раз эпик, чтобы убедиться, что время поменялось
        for (Task task : manager.getAllEpicTasks()) {
            System.out.print(task.getName() + " ");
            System.out.print("Время начала: " + task.getStartTime() + " ");
            System.out.print("Продолжительность: " + task.getDuration() + " ");
            System.out.println("Дата окончания: " + manager.getEndTime(task));
        }

        System.out.println();
    }

    private static void checkReadFromFile() {
        System.out.println("Проверяем чтение из файла...\n");

        File file = new File("src/filesfortest/test.csv");
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);

        for (Task task : fileBackedTaskManager.getAllTasks()) {
            System.out.println(task);
        }

        for (Task task : fileBackedTaskManager.getAllEpicTasks()) {
            System.out.println(task);
        }

        for (Task task : fileBackedTaskManager.getAllSubtasks()) {
            System.out.println(task);
        }
    }
}
