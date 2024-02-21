import TaskManagers.*;
import Tasks.*;
import UtilityClasses.Managers;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager =  Managers.getDefault();

        //Создаем 12 задач для проверки истории
        createTasks((InMemoryTaskManager) taskManager);
        // Выводим в консоль все задачи и историю
        printAllTasksAndHistory((InMemoryTaskManager) taskManager);
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
        manager.createEpicTask(new EpicTask("Эпическая задача - 2", "Описание эпической задачи",
                manager.getId(), TaskStatus.NEW)); // id = 5
        manager.createSubTask(new SubTask("Подзадача - 3", "Описание подзадачи",
                manager.getId(), TaskStatus.NEW, 5)); // id = 6
        manager.createTask(new Task("Обычная задача - 3", "Описание обычной задачи",
                manager.getId(), TaskStatus.NEW)); // id = 7
        manager.createEpicTask(new EpicTask("Эпическая задача - 3", "Описание эпической задачи",
                manager.getId(), TaskStatus.NEW)); // id = 8
        manager.createSubTask(new SubTask("Подзадача - 4", "Описание подзадачи",
                manager.getId(), TaskStatus.NEW, 8)); // id = 9
        manager.createSubTask(new SubTask("Подзадача - 5", "Описание подзадачи",
                manager.getId(), TaskStatus.NEW, 8)); // id = 10
        manager.createSubTask(new SubTask("Подзадача - 6", "Описание подзадачи",
                manager.getId(), TaskStatus.NEW, 8)); // id = 11
    }
    private static void printAllTasksAndHistory(InMemoryTaskManager manager) {
            Task task;

        for (int i = 0; i < manager.getId(); i++) {
            if(manager.getTaskMap().containsKey(i)) {
                task = manager.getTaskById(i);
                System.out.println(task);
            }

            if(manager.getEpicTaskMap().containsKey(i)) {
                task = manager.getEpicTaskById(i);
                System.out.println(task);
            }

            if(manager.getSubTaskMap().containsKey(i)) {
                task = manager.getSubTaskById(i);
                System.out.println(task);
            }
        }

        System.out.println("\nИстория:");
        for (Task historyTask : manager.getHistoryManager().getHistory()) {
            System.out.println(historyTask);
        }

        }
    }
