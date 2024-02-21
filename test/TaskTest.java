import TaskManagers.*;
import Tasks.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TaskTest {
    //Проверяем присвоение Id при создании задачи
    @Test
    public void shouldAssignId() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        int id = taskManager.getId();

        new Task("Подзадача", "Описание", id, TaskStatus.NEW);
        int expectedId = 0;

        assertEquals(expectedId, id);
    }
}