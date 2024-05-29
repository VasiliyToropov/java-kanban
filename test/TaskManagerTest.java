import org.junit.jupiter.api.Test;
import taskmanagers.InMemoryTaskManager;
import taskmanagers.TaskManager;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    //Проверяем, что UtilityClasses.Managers.TaskManagers.InMemoryTaskManager  добавляет задачи разного типа
    @Test
    public void shouldAddDifferentTask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        LocalDateTime taskTime1 = LocalDateTime.of(2024, 11, 11, 11, 11);
        LocalDateTime taskTime2 = LocalDateTime.of(2024, 11, 12, 11, 11);
        LocalDateTime epicTaskTime1 = LocalDateTime.of(2024, 11, 13, 11, 11);
        LocalDateTime epicTaskTime2 = LocalDateTime.of(2024, 11, 14, 11, 11);
        LocalDateTime subTaskTime1 = LocalDateTime.of(2024, 11, 15, 11, 11);
        LocalDateTime subTaskTime2 = LocalDateTime.of(2024, 11, 16, 11, 11);
        LocalDateTime subTaskTime3 = LocalDateTime.of(2024, 11, 17, 11, 11);
        long duration = 15;

        taskManager.createTask(new Task("Обычная задача - 1", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW, taskTime1, duration)); // id = 0
        taskManager.createTask(new Task("Обычная задача - 2", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW, taskTime2, duration)); // id = 1
        taskManager.createEpicTask(new EpicTask("Эпическая задача - 1", "Описание эпической задачи",
                taskManager.getId(), TaskStatus.NEW, epicTaskTime1, duration)); // id = 2
        taskManager.createSubTask(new SubTask("Подзадача - 1", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 2, subTaskTime1, duration)); // id = 3
        taskManager.createSubTask(new SubTask("Подзадача - 2", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 2, subTaskTime2, duration)); // id = 4
        taskManager.createEpicTask(new EpicTask("Эпическая задача - 2", "Описание эпической задачи",
                taskManager.getId(), TaskStatus.NEW, epicTaskTime2, duration)); // id = 5
        taskManager.createSubTask(new SubTask("Подзадача - 3", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 5, subTaskTime3, duration)); // id = 6

        int expectedTasks = 2;
        int expectedEpicTasks = 2;
        int expectedSubTasks = 3;
        HashMap<Integer, Task> tasks = taskManager.getTaskMap();
        HashMap<Integer, EpicTask> epicTasks = taskManager.getEpicTaskMap();
        HashMap<Integer, SubTask> subTasks = taskManager.getSubTaskMap();

        Task expectedTask = taskManager.getTaskById(0);
        EpicTask expectedEpicTask = taskManager.getEpicTaskById(2);
        SubTask expectedSubTask = taskManager.getSubTaskById(3);

        assertEquals(expectedTasks, tasks.size(), "Не все задачи созданы");
        assertNotNull(tasks, "Задачи не созданы");
        assertEquals(expectedTask, tasks.get(0), "Задачи не совпадают");

        assertEquals(expectedEpicTasks, epicTasks.size(), "Не все эпик-задачи созданы");
        assertNotNull(epicTasks, "Эпик-задачи не созданы");
        assertEquals(expectedEpicTask, epicTasks.get(2), "Эпик-задачи не совпадают");

        assertEquals(expectedSubTasks, subTasks.size(), "Не все подзадачи созданы");
        assertNotNull(subTasks, "Подзадачи не созданы");
        assertEquals(expectedSubTask, subTasks.get(3), "Подзадачи не совпадают");
    }

    //Проверяем, что UtilityClasses.Managers.TaskManagers.InMemoryTaskManager может найти задачи по id
    @Test
    public void shouldTasksFindById() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        LocalDateTime taskTime1 = LocalDateTime.of(2024, 11, 11, 11, 11);
        LocalDateTime epicTaskTime1 = LocalDateTime.of(2024, 11, 14, 11, 11);
        LocalDateTime subTaskTime1 = LocalDateTime.of(2024, 11, 15, 11, 11);
        long duration = 15;

        taskManager.createTask(new Task("Обычная задача - 1", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW, taskTime1, duration)); // id = 0
        taskManager.createEpicTask(new EpicTask("Эпическая задача - 1", "Описание эпической задачи",
                taskManager.getId(), TaskStatus.NEW, epicTaskTime1, duration)); // id = 1
        taskManager.createSubTask(new SubTask("Подзадача - 1", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 1, subTaskTime1, duration)); // id = 2


        Task task = taskManager.getTaskById(0);
        EpicTask epicTask = taskManager.getEpicTaskById(1);
        SubTask subTask = taskManager.getSubTaskById(2);

        final int expectedTaskId = 0;
        final int expectedEpicTaskId = 1;
        final int expectedSubTaskId = 2;

        assertEquals(expectedTaskId, task.getId());
        assertEquals(expectedEpicTaskId, epicTask.getId());
        assertEquals(expectedSubTaskId, subTask.getId());
    }

    //Проверяем, что UtilityClasses.Managers.TaskManagers.InMemoryTaskManager может удалить все задачи или по id
    @Test
    public void shouldTasksDeleteById() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        LocalDateTime taskTime1 = LocalDateTime.of(2024, 11, 11, 11, 11);
        LocalDateTime taskTime2 = LocalDateTime.of(2024, 11, 12, 11, 11);
        LocalDateTime epicTaskTime1 = LocalDateTime.of(2024, 11, 13, 11, 11);
        LocalDateTime epicTaskTime2 = LocalDateTime.of(2024, 11, 14, 11, 11);
        LocalDateTime subTaskTime1 = LocalDateTime.of(2024, 11, 15, 11, 11);
        LocalDateTime subTaskTime2 = LocalDateTime.of(2024, 11, 16, 11, 11);
        LocalDateTime subTaskTime3 = LocalDateTime.of(2024, 11, 17, 11, 11);
        long duration = 15;

        taskManager.createTask(new Task("Обычная задача - 1", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW, taskTime1, duration)); // id = 0
        taskManager.createTask(new Task("Обычная задача - 2", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW, taskTime2, duration)); // id = 1
        taskManager.createEpicTask(new EpicTask("Эпическая задача - 1", "Описание эпической задачи",
                taskManager.getId(), TaskStatus.NEW, epicTaskTime1, duration)); // id = 2
        taskManager.createSubTask(new SubTask("Подзадача - 1", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 2, subTaskTime1, duration)); // id = 3
        taskManager.createSubTask(new SubTask("Подзадача - 2", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 2, subTaskTime2, duration)); // id = 4
        taskManager.createEpicTask(new EpicTask("Эпическая задача - 2", "Описание эпической задачи",
                taskManager.getId(), TaskStatus.NEW, epicTaskTime2, duration)); // id = 5
        taskManager.createSubTask(new SubTask("Подзадача - 3", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 5, subTaskTime3, duration)); // id = 6

        HashMap<Integer, Task> tasks = taskManager.getTaskMap();
        HashMap<Integer, EpicTask> epicTasks = taskManager.getEpicTaskMap();
        HashMap<Integer, SubTask> subTasks = taskManager.getSubTaskMap();

        taskManager.deleteTaskById(1);
        Integer expectedTasksSize = 1;
        assertEquals(expectedTasksSize, tasks.size(), "Задача не удалена по ID");

        //Проверяем что при удалении задачи по id, не удаляются все задачи
        assertFalse(tasks.isEmpty(), "Удалены все задачи при удалении отдельной задачи по ID");
        taskManager.deleteAllTasks();
        assertEquals(0, tasks.size(), "Удалены не все задачи");

        taskManager.deleteSubTaskById(4);
        Integer expectedSubTasksSize = 2;
        assertEquals(expectedSubTasksSize, subTasks.size(), "Задача не удалена по ID");
        assertFalse(subTasks.isEmpty(), "Удалены все задачи при удалении отдельной задачи по ID");

        taskManager.deleteEpicTaskById(2);
        Integer expectedEpicTasksSize = 1;
        assertEquals(expectedEpicTasksSize, epicTasks.size());

        //Проверяем, что при удалении Эпик-задачи, удалилась и его подзадача
        assertEquals(1, subTasks.size(), "Задача не удалена по ID");

        taskManager.deleteEpicTasks();
        assertTrue(epicTasks.isEmpty(), "Не все эпик-задачи были удалены");
        assertTrue(subTasks.isEmpty(), "Не все подзадачи были удалены");
    }

    //Проверяем, что UtilityClasses.Managers.TaskManagers.InMemoryTaskManager может корректно обновить задачи
    @Test
    public void shouldTasksCorrectlyUpdate() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        LocalDateTime taskTime1 = LocalDateTime.of(2024, 11, 11, 11, 11);
        LocalDateTime taskTime2 = LocalDateTime.of(2024, 11, 12, 11, 11);
        LocalDateTime epicTaskTime1 = LocalDateTime.of(2024, 11, 13, 11, 11);
        LocalDateTime epicTaskTime2 = LocalDateTime.of(2024, 11, 14, 11, 11);
        LocalDateTime subTaskTime1 = LocalDateTime.of(2024, 11, 15, 11, 11);
        LocalDateTime subTaskTime2 = LocalDateTime.of(2024, 11, 16, 11, 11);
        LocalDateTime subTaskTime3 = LocalDateTime.of(2024, 11, 17, 11, 11);
        long duration = 15;

        taskManager.createTask(new Task("Обычная задача - 1", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW, taskTime1, duration)); // id = 0
        taskManager.createTask(new Task("Обычная задача - 2", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW, taskTime2, duration)); // id = 1
        taskManager.createEpicTask(new EpicTask("Эпическая задача - 1", "Описание эпической задачи",
                taskManager.getId(), TaskStatus.NEW, epicTaskTime1, duration)); // id = 2
        taskManager.createSubTask(new SubTask("Подзадача - 1", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 2, subTaskTime1, duration)); // id = 3
        taskManager.createSubTask(new SubTask("Подзадача - 2", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 2, subTaskTime2, duration)); // id = 4
        taskManager.createEpicTask(new EpicTask("Эпическая задача - 2", "Описание эпической задачи",
                taskManager.getId(), TaskStatus.NEW, epicTaskTime2, duration)); // id = 5
        taskManager.createSubTask(new SubTask("Подзадача - 3", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 5, subTaskTime3, duration)); // id = 6

        Task task = taskManager.getTaskById(1);
        taskManager.updateTask(0, task);
        Integer unexpectedId = 0;

        assertNotEquals(unexpectedId, task.getId(), "id совпадают");

        SubTask subTask = taskManager.getSubTaskById(4);
        taskManager.updateSubTask(3, subTask);
        unexpectedId = 3;

        assertNotEquals(unexpectedId, subTask.getId(), "id совпадают");

        EpicTask epicTask = taskManager.getEpicTaskById(5);
        taskManager.updateEpicTask(2, epicTask);
        unexpectedId = 2;

        assertNotEquals(unexpectedId, epicTask.getId(), "id совпадают");
    }

    //Проверяем, что UtilityClasses.Managers.TaskManagers.InMemoryTaskManager может менять статус задачи
    @Test
    public void shouldTasksChangeStatus() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        LocalDateTime taskTime1 = LocalDateTime.of(2024, 11, 11, 11, 11);
        LocalDateTime taskTime2 = LocalDateTime.of(2024, 11, 12, 11, 11);
        LocalDateTime epicTaskTime1 = LocalDateTime.of(2024, 11, 13, 11, 11);
        LocalDateTime epicTaskTime2 = LocalDateTime.of(2024, 11, 14, 11, 11);
        LocalDateTime subTaskTime1 = LocalDateTime.of(2024, 11, 15, 11, 11);
        LocalDateTime subTaskTime2 = LocalDateTime.of(2024, 11, 16, 11, 11);
        LocalDateTime subTaskTime3 = LocalDateTime.of(2024, 11, 17, 11, 11);
        long duration = 15;

        taskManager.createTask(new Task("Обычная задача - 1", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW, taskTime1, duration)); // id = 0
        taskManager.createTask(new Task("Обычная задача - 2", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW, taskTime2, duration)); // id = 1
        taskManager.createEpicTask(new EpicTask("Эпическая задача - 1", "Описание эпической задачи",
                taskManager.getId(), TaskStatus.NEW, epicTaskTime1, duration)); // id = 2
        taskManager.createSubTask(new SubTask("Подзадача - 1", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 2, subTaskTime1, duration)); // id = 3
        taskManager.createSubTask(new SubTask("Подзадача - 2", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 2, subTaskTime2, duration)); // id = 4
        taskManager.createEpicTask(new EpicTask("Эпическая задача - 2", "Описание эпической задачи",
                taskManager.getId(), TaskStatus.NEW, epicTaskTime2, duration)); // id = 5
        taskManager.createSubTask(new SubTask("Подзадача - 3", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 5, subTaskTime3, duration)); // id = 6

        taskManager.changeTaskStatus(0, TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getTaskById(0).getTaskStatus(),
                "Статусы не совпадают");

        taskManager.changeTaskStatus(0, TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, taskManager.getTaskById(0).getTaskStatus(),
                "Статусы не совпадают");

        taskManager.changeTaskStatus(0, TaskStatus.NEW);
        assertEquals(TaskStatus.NEW, taskManager.getTaskById(0).getTaskStatus(),
                "Статусы не совпадают");

        taskManager.changeEpicTaskStatus(2, TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.NEW, taskManager.getEpicTaskById(2).getTaskStatus(),
                "Статусы не совпадают");
        assertEquals(TaskStatus.NEW, taskManager.getSubTaskById(3).getTaskStatus(),
                "Статус подзадачи поменялся");

        taskManager.changeEpicTaskStatus(2, TaskStatus.DONE);
        assertEquals(TaskStatus.NEW, taskManager.getEpicTaskById(2).getTaskStatus(),
                "Статусы не совпадают");
        assertEquals(TaskStatus.NEW, taskManager.getSubTaskById(3).getTaskStatus(),
                "Статус подзадачи поменялся");

        taskManager.changeSubTaskStatus(3, TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicTaskById(2).getTaskStatus(),
                "Статусы не совпадают");
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getSubTaskById(3).getTaskStatus(),
                "Статус подзадачи не поменялся");

        taskManager.changeSubTaskStatus(4, TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, taskManager.getSubTaskById(4).getTaskStatus(),
                "Статусы не совпадают");
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicTaskById(2).getTaskStatus(),
                "Статус поменялся");

        taskManager.changeSubTaskStatus(3, TaskStatus.DONE);
        taskManager.changeEpicTaskStatus(2, TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, taskManager.getEpicTaskById(2).getTaskStatus(),
                "Статусы не совпадают");
    }
}

