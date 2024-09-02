import org.junit.jupiter.api.Test;
import taskmanagers.FileBackedTaskManager;
import taskmanagers.InMemoryTaskManager;
import tasks.Task;
import tasks.TaskStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    @Test
    public void shouldCastExceptions() {
        assertThrows(FileNotFoundException.class, () -> {
            File file = new File("wrong.txt");
            new BufferedReader(new FileReader(file));
        }, "Должно выводится сообщение о том, что файл не найден");

        assertThrows(NullPointerException.class, () -> {
            InMemoryTaskManager taskManager = new InMemoryTaskManager();
            taskManager.getTaskById(0);
        }, "Должно выводится сообщение о том что был возвращен null");
    }

    // Тестируем метод fromString()
    @Test
    public void shouldMethodFromStringWorksCorrectly() {
        String checkedText = "0,Task,Обычная задача - 1,NEW,Описание обычной задачи,2024-05-01T12:00,50";
        Task task = FileBackedTaskManager.fromString(checkedText);

        String expectedName = "Обычная задача - 1";
        int expectedId = 0;
        String expectedDescription = "Описание обычной задачи";
        TaskStatus expectedStatus = TaskStatus.NEW;
        LocalDateTime expectedStartTime = LocalDateTime.parse("2024-05-01T12:00");
        long expectedDuration = 50;

        assert task != null;
        assertEquals(expectedName, task.getName(), "Неправильное имя задачи");
        assertEquals(expectedId, task.getId(), "Неправильный ID");
        assertEquals(expectedDescription, task.getDescription(), "Неправильное описание");
        assertEquals(expectedStatus, task.getTaskStatus(), "Неправильный статус");
        assertEquals(expectedStartTime, task.getStartTime(), "Неправильное время начала");
        assertEquals(expectedDuration, task.getDuration().toMinutes(), "Неправильная продолжительность");
    }

    // Тестируем метод loadFromFile()
    @Test
    public void shouldMethodLoadFromFileWorksCorrectly() {
        File file = new File("src/filesfortest/test.csv");
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);

        int expectedTaskMapSize = 4;
        int expectedEpicTaskMapSize = 1;
        int expectedSubTasMapSize = 2;

        assertEquals(expectedTaskMapSize, fileBackedTaskManager.getTaskMap().size(), "Неверное чтение файла для обычных задач");
        assertEquals(expectedEpicTaskMapSize, fileBackedTaskManager.getEpicTaskMap().size(), "Неверное чтение файла для эпик-задач");
        assertEquals(expectedSubTasMapSize, fileBackedTaskManager.getSubTaskMap().size(), "Неверное чтение файла для подзадач");
    }

    // Тестируем метод toWrite()
    @Test
    public void shouldMethodToWriteWorksCorrectly() {
        File file = new File("src/filesfortest/test.csv");
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);

        LocalDateTime dateTime = LocalDateTime.of(2024, 11, 11, 11, 11);

        Task task = new Task("Обычная задача - 1", "Описание обычной задачи",
                fileBackedTaskManager.getId(), TaskStatus.NEW, dateTime, 30);

        String expectedTaskDesc = "7,Task,Обычная задача - 1,NEW,Описание обычной задачи,2024-11-11T11:11,PT30M\n";
        String actualTaskDesc = fileBackedTaskManager.toWrite(task);

        assertEquals(expectedTaskDesc, actualTaskDesc, "Метод работает некорректно");
    }

}

