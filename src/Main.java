import taskmanagers.*;
import tasks.*;
import utility.Managers;

public class Main {
    static TaskManager taskManager =  Managers.getDefault();

    public static void main(String[] args) {
        //Создаем 6 задач для проверки истории
        createTasks((InMemoryTaskManager) taskManager);
        // Выводим в консоль все задачи и историю
        checkHistory((InMemoryTaskManager) taskManager);
    }

    private static void createTasks(InMemoryTaskManager manager) {
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
        manager.createEpicTask(new EpicTask("Эпическая задача - 2", "Описание эпической задачи",
                manager.getId(), TaskStatus.NEW)); // id = 6
    }

    private static void checkHistory(InMemoryTaskManager manager) {
        //Создаем записи в разном порядке в истории и выводим  4 0 2 1

        manager.getSubTaskById(4);
        printHistory(manager);
        manager.getTaskById(0);
        printHistory(manager);
        manager.getEpicTaskById(2);
        printHistory(manager);
        manager.getTaskById(1);
        printHistory(manager);

        //Смотрим, есть ли повторы
        manager.getSubTaskById(4);
        printHistory(manager);
        manager.getTaskById(0);
        printHistory(manager);
        manager.getEpicTaskById(2);
        printHistory(manager);

        // Удаляем задачу, которая есть в истории, и проверяем, что при печати она не будет выводиться.
        manager.deleteTaskById(1);
        printHistory(manager);

        //Удалием эпик с тремя подзадачами и убеждаемся, что из истории удалился как сам эпик, так и все его подзадачи.
        //Сначала добавляем подзадачи в историю
        manager.getSubTaskById(3);
        manager.getSubTaskById(5);
        printHistory(manager);

        //Теперь удаляем epic и проверяем историю
        manager.deleteEpicTaskById(2);
        printHistory(manager);
    }

    private static void printHistory(InMemoryTaskManager manager) {
        System.out.println("\nИстория:");
        for (Task historyTask : manager.getHistoryManager().getHistory()) {
            System.out.println(historyTask);
        }
    }
}
