import org.junit.jupiter.api.Test;
import taskmanagers.InMemoryTaskManager;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.TaskStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SubTaskTest {
    //Проверяем присвоение Id при создании задачи
    @Test
    public void shouldAssignId() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        int id = taskManager.getId();
        LocalDateTime epicTaskTime = LocalDateTime.of(2024, 11, 13, 11, 11);
        LocalDateTime subTaskTime = LocalDateTime.of(2024, 11, 14, 11, 11);
        long duration = 30;


        taskManager.createEpicTask(new EpicTask("Задача-эпик", "Описание", id, TaskStatus.NEW, epicTaskTime, duration));
        id = taskManager.getId();
        taskManager.createSubTask(new SubTask("Подзадача", "Описание", id, TaskStatus.NEW, 0, subTaskTime, duration));
        int expectedId = 1;

        assertEquals(expectedId, id, "id не присвоен");
    }

    //Проверяем, что объект Subtask нельзя сделать своим же эпиком;
    @Test
    public void shouldNotBeEpicTask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        int id = taskManager.getId();
        LocalDateTime subTaskTime = LocalDateTime.of(2024, 11, 14, 11, 11);
        long duration = 30;

        taskManager.createSubTask(new SubTask("Подзадача", "Описание", id, TaskStatus.NEW, id, subTaskTime, duration));
        int expectedSize = 0;

        assertEquals(expectedSize, taskManager.getSubTaskMap().size(), "Объект Tasks.SubTask был своим же эпиком");
    }

    //Проверяем, что удаляемые подзадачи не должны хранить внутри себя старые id
    @Test
    public void deletedSubtasksShouldNotContainsOldIds() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        LocalDateTime epicTaskTime = LocalDateTime.of(2024, 11, 13, 11, 11);
        LocalDateTime subTaskTime1 = LocalDateTime.of(2024, 11, 14, 11, 11);
        LocalDateTime subTaskTime2 = LocalDateTime.of(2024, 11, 15, 11, 11);
        LocalDateTime subTaskTime3 = LocalDateTime.of(2024, 11, 15, 11, 11);

        long duration = 30;

        taskManager.createEpicTask(new EpicTask("Задача-эпик", "Описание", taskManager.getId(), TaskStatus.NEW, epicTaskTime, duration));
        taskManager.createSubTask(new SubTask("Подзадача-1", "Описание", taskManager.getId(), TaskStatus.NEW, 0, subTaskTime1, duration));
        taskManager.createSubTask(new SubTask("Подзадача-2", "Описание", taskManager.getId(), TaskStatus.NEW, 0, subTaskTime2, duration));


        taskManager.deleteSubTaskById(1);

        taskManager.createSubTask(new SubTask("Подзадача-3", "Описание", taskManager.getId(), TaskStatus.NEW, 0, subTaskTime3, duration));

        boolean isContainsOldId = taskManager.getId() < 4;

        assertFalse(isContainsOldId, "Тест не пройден, новой подзадаче присвоен старый ID");
    }

    //Проверяем, присвоена ли Эпик-задача для подзадачи
    @Test
    public void shouldContainsEpicTask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        LocalDateTime epicTaskTime = LocalDateTime.of(2024, 11, 13, 11, 11);
        LocalDateTime subTaskTime1 = LocalDateTime.of(2024, 11, 14, 11, 11);
        long duration = 30;

        taskManager.createEpicTask(new EpicTask("Задача-эпик", "Описание", taskManager.getId(),
                TaskStatus.NEW, epicTaskTime, duration));
        taskManager.createSubTask(new SubTask("Подзадача-1", "Описание", taskManager.getId(),
                TaskStatus.NEW, 0, subTaskTime1, duration));

        SubTask subTask = taskManager.getSubTaskById(1);

        int expectedEpicTaskId = subTask.getEpicTaskId();

        assertEquals(expectedEpicTaskId, 0, "Эпик-задача не присвоена к подзадаче");
    }
}

