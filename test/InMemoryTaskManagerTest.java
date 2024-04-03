import taskManagers.*;
import Tasks.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    static InMemoryTaskManager taskManager = new InMemoryTaskManager();
    final HashMap<Integer, Task> tasks = taskManager.getTaskMap();
    final HashMap<Integer, EpicTask> epicTasks = taskManager.getEpicTaskMap();
    final HashMap<Integer, SubTask> subTasks = taskManager.getSubTaskMap();

    @BeforeAll
    public static void beforeAll() {
        taskManager.createTask(new Task("Обычная задача - 1", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW)); // id = 0
        taskManager.createTask(new Task("Обычная задача - 2", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW)); // id = 1
        taskManager.createEpicTask(new EpicTask("Эпическая задача - 1", "Описание эпической задачи",
                taskManager.getId(), TaskStatus.NEW)); // id = 2
        taskManager.createSubTask(new SubTask("Подзадача - 1", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 2)); // id = 3
        taskManager.createSubTask(new SubTask("Подзадача - 2", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 2)); // id = 4
        taskManager.createEpicTask(new EpicTask("Эпическая задача - 2", "Описание эпической задачи",
                taskManager.getId(), TaskStatus.NEW)); // id = 5
        taskManager.createSubTask(new SubTask("Подзадача - 3", "Описание подзадачи",
                taskManager.getId(), TaskStatus.NEW, 5)); // id = 6
    }

    //Проверяем, что UtilityClasses.Managers.TaskManagers.InMemoryTaskManager  добавляет задачи разного типа
    @Test
    public void shouldAddDifferentTask() {

        int expectedTasks = 2;
        int expectedEpicTasks = 2;
        int expectedSubTasks = 3;

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
        Task task = taskManager.getTaskById(0);
        EpicTask epicTask = taskManager.getEpicTaskById(2);
        SubTask subTask = taskManager.getSubTaskById(3);

        final int expectedTaskId = 0;
        final int expectedEpicTaskId = 2;
        final int expectedSubTaskId = 3;

        assertEquals(expectedTaskId, task.getId());
        assertEquals(expectedEpicTaskId, epicTask.getId());
        assertEquals(expectedSubTaskId, subTask.getId());
    }

    //Проверяем, что UtilityClasses.Managers.TaskManagers.InMemoryTaskManager может удалить все задачи или по id
    @Test
    public void shouldTasksDeleteById() {

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


