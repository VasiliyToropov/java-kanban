import org.junit.jupiter.api.Test;
import taskmanagers.InMemoryTaskManager;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.TaskStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class EpicTaskTest {
    //Проверяем присвоение Id при создании задачи
    @Test
    public void shouldAssignId() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        LocalDateTime taskTime = LocalDateTime.now();
        long duration = 30;

        int id = taskManager.getId();

        new EpicTask("Подзадача", "Описание", id, TaskStatus.NEW, taskTime, duration);

        int expectedId = 0;
        assertEquals(expectedId, id, "id не присвоен");
    }

    //Проверяем, что объект Epic нельзя добавить в самого себя в виде подзадачи
    @Test
    public void shouldNotAddAsSubtask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        LocalDateTime epicTaskTime = LocalDateTime.now();
        LocalDateTime subTaskTime = LocalDateTime.of(2024, 11, 11, 11, 11);
        long duration = 30;

        int id = taskManager.getId();

        taskManager.createEpicTask(new EpicTask("Эпик-задача", "Описание", id,
                TaskStatus.NEW, epicTaskTime, duration));
        taskManager.createSubTask(new SubTask("Подзадача", "Описание", id,
                TaskStatus.NEW, id, subTaskTime, duration));

        int expectedSize = 0;
        int subtasksArraySize = taskManager.getEpicTaskById(id).getSubtasksId().size();

        assertEquals(expectedSize, subtasksArraySize,
                "Тест не пройден, задача Epic добавилась в самого себя в виде подзадачи");
    }

    // Проверяем, что внутри эпиков не должно оставаться неактуальных id подзадач
    @Test
    public void shouldNotStayIrrelevantSubTaskId() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        LocalDateTime epicTaskTime = LocalDateTime.now();
        LocalDateTime subTaskTime1 = LocalDateTime.of(2024, 11, 11, 11, 11);
        LocalDateTime subTaskTime2 = LocalDateTime.of(2024, 12, 11, 11, 11);
        long duration = 30;

        taskManager.createEpicTask(new EpicTask("Эпик-задача", "Описание", taskManager.getId(),
                TaskStatus.NEW, epicTaskTime, duration)); // id = 0
        taskManager.createSubTask(new SubTask("Подзадача-1", "Описание", taskManager.getId(),
                TaskStatus.NEW, 0, subTaskTime1, duration));
        taskManager.createSubTask(new SubTask("Ползадача-2", "Описание", taskManager.getId(),
                TaskStatus.NEW, 0, subTaskTime2, duration));

        taskManager.deleteSubTaskById(1);

        EpicTask epic = taskManager.getEpicTaskById(0);
        boolean isIdExist = epic.getSubtasksId().contains(1);

        assertFalse(isIdExist, "Тест не пройден, неактуальные id содержаться в Эпик-задаче");
    }

    // Проверяем, корректную смену статуса задачи Эпик, при смене статуса его подзадач
    @Test
    public void shouldChangeEpicTaskStatus() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        LocalDateTime epicTaskTime = LocalDateTime.now();
        LocalDateTime subTaskTime1 = LocalDateTime.of(2024, 11, 11, 11, 11);
        LocalDateTime subTaskTime2 = LocalDateTime.of(2024, 12, 11, 11, 11);
        long duration = 30;

        taskManager.createEpicTask(new EpicTask("Эпик-задача", "Описание", taskManager.getId(),
                TaskStatus.NEW, epicTaskTime, duration)); // id = 0
        taskManager.createSubTask(new SubTask("Подзадача-1", "Описание", taskManager.getId(),
                TaskStatus.NEW, 0, subTaskTime1, duration));
        taskManager.createSubTask(new SubTask("Ползадача-2", "Описание", taskManager.getId(),
                TaskStatus.NEW, 0, subTaskTime2, duration));

        EpicTask epic = taskManager.getEpicTaskById(0);

        // Все подзадачи со статусом NEW
        assertEquals(epic.getTaskStatus(), TaskStatus.NEW,
                "Тест не пройден, Эпик-задача имеет некорректный статус");

        //Все подзадачи со статусом DONE
        taskManager.changeSubTaskStatus(1, TaskStatus.DONE);
        taskManager.changeSubTaskStatus(2, TaskStatus.DONE);
        assertEquals(epic.getTaskStatus(), TaskStatus.DONE,
                "Тест не пройден, Эпик-задача имеет некорректный статус");

        //Подзадачи со статусами NEW и DONE
        taskManager.changeSubTaskStatus(1, TaskStatus.NEW);
        assertEquals(epic.getTaskStatus(), TaskStatus.IN_PROGRESS,
                "Тест не пройден, Эпик-задача имеет некорректный статус");

        //Подзадачи со статусом IN_PROGRESS.
        taskManager.changeSubTaskStatus(1, TaskStatus.IN_PROGRESS);
        taskManager.changeSubTaskStatus(1, TaskStatus.IN_PROGRESS);
        assertEquals(epic.getTaskStatus(), TaskStatus.IN_PROGRESS,
                "Тест не пройден, Эпик-задача имеет некорректный статус");
    }
}
