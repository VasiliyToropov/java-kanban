import org.junit.jupiter.api.Test;
import taskmanagers.InMemoryTaskManager;
import tasks.Task;
import tasks.TaskStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TaskTest {
    //Проверяем присвоение Id при создании задачи
    @Test
    public void shouldAssignId() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        int id = taskManager.getId();
        LocalDateTime taskTime = LocalDateTime.now();
        long duration = 30;

        new Task("Подзадача", "Описание", id, TaskStatus.NEW, taskTime, duration);
        int expectedId = 0;

        assertEquals(expectedId, id);
    }
}