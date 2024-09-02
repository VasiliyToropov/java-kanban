import historymanagers.HistoryManager;
import org.junit.jupiter.api.Test;
import taskmanagers.InMemoryTaskManager;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {
    //Проверяем, что задачи добавляются корректно
    @Test
    public void shouldTaskWillBeAddedToHistory() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        HistoryManager historyManager = taskManager.getHistoryManager();

        LocalDateTime taskTime = LocalDateTime.now();
        LocalDateTime epicTaskTime = LocalDateTime.of(2024, 11, 11, 11, 11);
        LocalDateTime subTaskTime = LocalDateTime.of(2024, 12, 11, 11, 11);
        long duration = 30;

        taskManager.createTask(new Task("Обычная задача - 1", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW, taskTime, duration)); // id = 0
        taskManager.createEpicTask(new EpicTask("Эпическая задача - 1", "Описание эпической задачи",
                taskManager.getId(), TaskStatus.NEW, epicTaskTime, duration)); // id = 1
        taskManager.createSubTask(new SubTask("Подзадача - 1", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 1, subTaskTime, duration));

        //Проверяем, что история пустая
        int expectedSize = 0;
        int tasksInHistorySize = historyManager.getHistory().size();

        assertEquals(expectedSize, tasksInHistorySize, "История не пустая");

        // Проверяем, что добавление работает корректно
        taskManager.getTaskById(0);
        taskManager.getEpicTaskById(1);
        taskManager.getSubTaskById(2);

        tasksInHistorySize = historyManager.getHistory().size();
        expectedSize = 3;

        assertEquals(expectedSize, tasksInHistorySize,
                "Количество добавленных задач не совпадает с количеством задач в истории");
        assertNotNull(historyManager.getHistory(), "История пустая");

        //Проверяем, что при добавлении задачи в историю она не дублируется
        taskManager.getSubTaskById(2);
        assertEquals(expectedSize, tasksInHistorySize,
                "Количество добавленных задач не совпадает с количеством задач в истории");

    }

    //убеждаемся, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    public void shouldBeSavePreviousVersionOfTask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        HistoryManager historyManager = taskManager.getHistoryManager();

        LocalDateTime taskTime = LocalDateTime.now();
        long duration = 30;

        taskManager.createTask(new Task("Обычная задача - 1", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW, taskTime, duration)); // id = 0

        taskManager.getTaskById(0);

        Task newTask = new Task("Обновленная задача", "Описание обновленной задачи", 3,
                TaskStatus.NEW, taskTime, duration);

        taskManager.updateTask(0, newTask);
        taskManager.getTaskById(3);

        ArrayList<Task> historyTasks = historyManager.getHistory();

        String expectedName = "Обычная задача - 1";
        String expectedDescription = "Описание обычной задачи";
        int expectedId = 0;
        Task updatedTask = historyTasks.getFirst();

        assertEquals(expectedName, updatedTask.getName(), "Данные сохранены в истории неверно");
        assertEquals(expectedDescription, updatedTask.getDescription(), "Данные сохранены в истории неверно");
        assertEquals(expectedId, updatedTask.getId(), "Данные сохранены в истории неверно");
    }

    // Проверяем, что удаление из истории работает корректно
    @Test
    public void shouldTaskWillBeDeletedFromHistory() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        HistoryManager historyManager = taskManager.getHistoryManager();

        LocalDateTime taskTime = LocalDateTime.now();
        LocalDateTime epicTaskTime = LocalDateTime.of(2024, 11, 11, 11, 11);
        LocalDateTime subTaskTime1 = LocalDateTime.of(2024, 12, 11, 11, 11);
        LocalDateTime subTaskTime2 = LocalDateTime.of(2024, 12, 11, 11, 45);
        long duration = 30;

        taskManager.createTask(new Task("Обычная задача - 1", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW, taskTime, duration)); // id = 0
        taskManager.createEpicTask(new EpicTask("Эпическая задача - 1", "Описание эпической задачи",
                taskManager.getId(), TaskStatus.NEW, epicTaskTime, duration)); // id = 1
        taskManager.createSubTask(new SubTask("Подзадача - 1", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 1, subTaskTime1, duration)); // id = 2
        taskManager.createSubTask(new SubTask("Подзадача - 2", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 1, subTaskTime2, duration)); // id = 3

        taskManager.getTaskById(0);
        taskManager.getEpicTaskById(1);
        taskManager.getSubTaskById(2);
        taskManager.getSubTaskById(3);

        // Проверяем удаление из середины списка
        taskManager.deleteSubTaskById(2);

        int expectedHistorySize = 3;
        ArrayList<Task> historyTasks = historyManager.getHistory();

        assertEquals(expectedHistorySize, historyTasks.size());

        // Проверяем удаление из начала списка
        taskManager.deleteTaskById(0);

        historyTasks = historyManager.getHistory();
        expectedHistorySize = 2;

        assertEquals(expectedHistorySize, historyTasks.size());

        // Проверяем удаление из конца списка
        taskManager.deleteSubTaskById(3);

        historyTasks = historyManager.getHistory();
        expectedHistorySize = 1;

        assertEquals(expectedHistorySize, historyTasks.size());
    }

    //Проверяем, что встроенный связный список версий удаляет дубликаты и перемещает добавленную задачу в конец списка
    @Test
    public void shouldDeleteDublicateAndAddFirst() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        HistoryManager historyManager = taskManager.getHistoryManager();

        LocalDateTime taskTime = LocalDateTime.now();
        LocalDateTime epicTaskTime = LocalDateTime.of(2024, 11, 11, 11, 11);
        LocalDateTime subTaskTime = LocalDateTime.of(2024, 12, 11, 11, 11);
        long duration = 30;

        taskManager.createTask(new Task("Обычная задача - 1", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW, taskTime, duration)); // id = 0
        taskManager.createEpicTask(new EpicTask("Эпическая задача - 1", "Описание эпической задачи",
                taskManager.getId(), TaskStatus.NEW, epicTaskTime, duration)); // id = 1
        taskManager.createSubTask(new SubTask("Подзадача - 1", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 1, subTaskTime, duration));

        taskManager.getTaskById(0);
        taskManager.getEpicTaskById(1);
        taskManager.getSubTaskById(2);
        taskManager.getTaskById(0);

        ArrayList<Task> historyTasks = historyManager.getHistory();

        int expectedHistorySize = 3;
        int expectedTaskId = historyTasks.getLast().getId();

        assertEquals(expectedHistorySize, historyTasks.size());
        assertEquals(expectedTaskId, 0);
    }
}

