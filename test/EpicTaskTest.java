import TaskManagers.*;
import Tasks.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTaskTest {
    //Проверяем присвоение Id при создании задачи
    @Test
    public void shouldAssignId() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        int id = taskManager.getId();

        new EpicTask("Подзадача", "Описание", id, TaskStatus.NEW);
        int expectedId = 0;

        assertEquals(expectedId, id, "id не присвоен");
    }

    //Проверяем, что объект Epic нельзя добавить в самого себя в виде подзадачи
    @Test
    public void shouldNotAddAsSubtask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        int id = taskManager.getId();

        taskManager.createEpicTask(new EpicTask("Подзадача", "Описание", id, TaskStatus.NEW));
        taskManager.createSubTask(new SubTask("Подзадача", "Описание", id, TaskStatus.NEW, id));

        int expectedSize = 0;
        int subtasksArraySize = taskManager.getEpicTaskById(id).getSubtasksId().size();

        assertEquals(expectedSize, subtasksArraySize,
                "Тест не пройден, задача Epic добавилась в самого себя в виде подзадачи");
    }

}
