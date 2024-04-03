import taskmanagers.*;
import tasks.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

        taskManager.createEpicTask(new EpicTask("Эпик-задача", "Описание", id, TaskStatus.NEW));
        taskManager.createSubTask(new SubTask("Подзадача", "Описание", id, TaskStatus.NEW, id));

        int expectedSize = 0;
        int subtasksArraySize = taskManager.getEpicTaskById(id).getSubtasksId().size();

        assertEquals(expectedSize, subtasksArraySize,
                "Тест не пройден, задача Epic добавилась в самого себя в виде подзадачи");
    }
    // Проверяем, что внутри эпиков не должно оставаться неактуальных id подзадач
    @Test
    public void shouldNotStayIrrelevantSubTaskId() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        taskManager.createEpicTask(new EpicTask("Эпик-задача", "Описание", taskManager.getId(), TaskStatus.NEW)); // id = 0
        taskManager.createSubTask(new SubTask("Подзадача-1", "Описание", taskManager.getId(), TaskStatus.NEW, 0));
        taskManager.createSubTask(new SubTask("Ползадача-2", "Описание", taskManager.getId(), TaskStatus.NEW, 0));

        taskManager.deleteSubTaskById(1);

        EpicTask epic = taskManager.getEpicTaskById(0);
        boolean isIdExist = epic.getSubtasksId().contains(1);

        assertFalse(isIdExist, "Тест не пройден, неактуальные id содержаться в Эпик-задаче");
    }

}
