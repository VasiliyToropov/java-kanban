import taskmanagers.*;
import Utility.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManagersTest {
    //Убеждаемся, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
    @Test
    public void shouldGetBackTaskManagerObject() {
        TaskManager taskManager = Managers.getDefault();
        InMemoryTaskManager taskManagerExpected = new InMemoryTaskManager();

        assertEquals(taskManagerExpected.getClass(), taskManager.getClass(),
                "Не возвращает нужный класс менеджера");
    }
}
