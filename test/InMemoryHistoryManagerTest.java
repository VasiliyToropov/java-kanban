import HistoryManagers.*;
import TaskManagers.*;
import Tasks.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {
    //Проверяем, что задачи добавляются корректно
    @Test
    public void shouldTaskWillBeAddedToHistory() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        HistoryManager historyManager = taskManager.getHistoryManager();

        taskManager.createTask(new Task("Обычная задача - 1", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW)); // id = 0
        taskManager.createEpicTask(new EpicTask("Эпическая задача - 1", "Описание эпической задачи",
                taskManager.getId(), TaskStatus.NEW)); // id = 1
        taskManager.createSubTask(new SubTask("Подзадача - 1", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 1));

        int expectedSize = 3;

        taskManager.getTaskById(0);
        taskManager.getEpicTaskById(1);
        taskManager.getSubTaskById(2);

        int tasksInHistorySize = historyManager.getHistory().size();

        assertEquals(expectedSize, tasksInHistorySize, "Количество добавленных задач не совпадает с количеством задач в истории");
        assertNotNull(historyManager.getHistory(), "История пустая");
    }

    //убеждаемся, что задачи, добавляемые в HistoryManagers.HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    public void shouldBeSavePreviousVersionOfTask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        HistoryManager historyManager = taskManager.getHistoryManager();

        taskManager.createTask(new Task("Обычная задача - 1", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW)); // id = 0

        taskManager.getTaskById(0);

        Task newTask = new Task("Обновленная задача", "Описание обновленной задачи", 3, TaskStatus.NEW);

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

        taskManager.createTask(new Task("Обычная задача - 1", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW)); // id = 0
        taskManager.createEpicTask(new EpicTask("Эпическая задача - 1", "Описание эпической задачи",
                taskManager.getId(), TaskStatus.NEW)); // id = 1
        taskManager.createSubTask(new SubTask("Подзадача - 1", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 1)); // id = 2

        taskManager.getTaskById(0);
        taskManager.getEpicTaskById(1);
        taskManager.getSubTaskById(2);

        taskManager.deleteEpicTaskById(1);

        int expectedHistorySize = 1;
        ArrayList<Task> historyTasks = historyManager.getHistory();

        assertEquals(expectedHistorySize,historyTasks.size());
    }
    //Проверяем, что встроенный связный список версий удаляет дубликаты и перемещает добавленную задачу в конец списка
    @Test
    public void shouldDeleteDublicateAndAddFirst() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        HistoryManager historyManager = taskManager.getHistoryManager();

        taskManager.createTask(new Task("Обычная задача - 1", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW)); // id = 0
        taskManager.createEpicTask(new EpicTask("Эпическая задача - 1", "Описание эпической задачи",
                taskManager.getId(), TaskStatus.NEW)); // id = 1
        taskManager.createSubTask(new SubTask("Подзадача - 1", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 1)); // id = 2

        taskManager.getTaskById(0);
        taskManager.getEpicTaskById(1);
        taskManager.getSubTaskById(2);
        taskManager.getTaskById(0);

        ArrayList<Task> historyTasks = historyManager.getHistory();

        int expectedHistorySize = 3;
        int expectedTaskId = historyTasks.getLast().getId();

        assertEquals(expectedHistorySize,historyTasks.size());
        assertEquals(expectedTaskId, 0);
    }
}

