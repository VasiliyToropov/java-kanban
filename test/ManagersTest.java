import org.junit.jupiter.api.Test;
import taskmanagers.FileBackedTaskManager;
import taskmanagers.TaskManager;
import utility.Managers;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManagersTest {
    //Убеждаемся, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
    @Test
    public void shouldGetBackTaskManagerObject() throws IOException {
        TaskManager taskManager = Managers.getDefault();
        FileBackedTaskManager taskManagerExpected = new FileBackedTaskManager();

        assertEquals(taskManagerExpected.getClass(), taskManager.getClass(),
                "Не возвращает нужный класс менеджера");
    }
}
