import HistoryManagers.*;
import TaskManagers.*;
import Tasks.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    InMemoryTaskManager taskManager = new InMemoryTaskManager();
    Task task = new Task("Обычная задача - 1", "Описание обычной задачи",
            taskManager.getId(), TaskStatus.NEW);
    EpicTask epicTask = new EpicTask("Эпическая задача - 1", "Описание эпической задачи",
            taskManager.getId(), TaskStatus.NEW);
    SubTask subTask = new SubTask("Подзадача - 1", "Описание подзадачи",
            taskManager.getId(), TaskStatus.NEW, 1);

    @Test
    public void shouldTaskWillBeAddedToHistory() {

        historyManager.addToHistory(task);
        historyManager.addToHistory(epicTask);
        historyManager.addToHistory(subTask);

        int expectedSize = 3;
        int tasksInHistorySize = historyManager.getHistory().size();

        assertEquals(expectedSize, tasksInHistorySize, "Количество добавленных задач не совпадает с количеством задач в истории");
        assertNotNull(historyManager.getHistory(), "История пустая");
    }

    //убеждаемся, что задачи, добавляемые в HistoryManagers.HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    public void shouldBeSavePreviousVersionOfTask() {
        historyManager.addToHistory(task);

        Task newTask = new Task("Обновленная задача", "Описание обновленной задачи", 3, TaskStatus.NEW);

        taskManager.updateTask(0, newTask);
        historyManager.addToHistory(newTask);

        ArrayList<Task> historyTasks = historyManager.getHistory();

        String expectedName = "Обычная задача - 1";
        String expectedDescription = "Описание обычной задачи";
        int expectedId = 0;
        Task updatedTask = historyTasks.getFirst();

        assertEquals(expectedName, updatedTask.getName(), "Данные сохранены в истории неверно");
        assertEquals(expectedDescription, updatedTask.getDescription(), "Данные сохранены в истории неверно");
        assertEquals(expectedId, updatedTask.getId(), "Данные сохранены в истории неверно");
    }

}

