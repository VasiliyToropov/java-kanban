import Taskmanagers.*;
import Tasks.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SubTaskTest {
    //Проверяем присвоение Id при создании задачи
    @Test
    public void shouldAssignId() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        int id = taskManager.getId();
        taskManager.createEpicTask(new EpicTask("Задача-эпик", "Описание", id, TaskStatus.NEW));
        id = taskManager.getId();
        taskManager.createSubTask(new SubTask("Подзадача", "Описание", id, TaskStatus.NEW, 0));
        int expectedId = 1;

        assertEquals(expectedId, id, "id не присвоен");
    }

    //Проверяем, что объект Subtask нельзя сделать своим же эпиком;
    @Test
    public void shouldNotBeEpicTask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        int id = taskManager.getId();
        taskManager.createSubTask(new SubTask("Подзадача", "Описание", id, TaskStatus.NEW, id));
        int expectedSize = 0;

        assertEquals(expectedSize, taskManager.getSubTaskMap().size(), "Объект Tasks.SubTask был своим же эпиком");
    }

    //Проверяем, что удаляемые подзадачи не должны хранить внутри себя старые id
    @Test
    public void deletedSubtasksShouldNotContainsOldIds() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.createEpicTask(new EpicTask("Задача-эпик", "Описание", taskManager.getId(), TaskStatus.NEW));
        taskManager.createSubTask(new SubTask("Подзадача-1", "Описание", taskManager.getId(), TaskStatus.NEW, 0));
        taskManager.createSubTask(new SubTask("Подзадача-2", "Описание", taskManager.getId(), TaskStatus.NEW, 0));

        taskManager.deleteSubTaskById(1);

        taskManager.createSubTask(new SubTask("Подзадача-3", "Описание", taskManager.getId(), TaskStatus.NEW, 0));

        boolean isContainsOldId = taskManager.getId() < 4;

        assertFalse(isContainsOldId,"Тест не пройден, новой подзадаче присвоен старый ID");
    }
}

