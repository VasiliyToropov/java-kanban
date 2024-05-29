import org.junit.jupiter.api.Test;
import taskmanagers.InMemoryTaskManager;
import tasks.Task;
import tasks.TaskStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    // Проверяем работоспособность метода расчёта пересечения интервалов при создании создач
    @Test
    public void shouldMethodIsOverLapIsWork() {
        // Проверяем, что задачи пересекаются. То есть при пересечении интервалов создаться лишь одна задача
        LocalDateTime taskTime1 = LocalDateTime.of(2024, 11, 1, 11, 10);
        LocalDateTime taskTime2 = LocalDateTime.of(2024, 11, 1, 11, 20);
        long duration = 30;

        taskManager.createTask(new Task("Обычная задача - 3", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW, taskTime1, duration)); // id = 1
        taskManager.createTask(new Task("Обычная задача - 4", "Описание обычной задачи",
                taskManager.getId(), TaskStatus.NEW, taskTime2, duration)); // id = 2

        int expectedTaskMapSize = 1;

        assertEquals(expectedTaskMapSize, taskManager.getTaskMap().size(),
                "Метод пересечения интервалов работает некорректно");
    }


}


