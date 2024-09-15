import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import httphandlers.BaseHttpHandler;
import httpserver.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanagers.InMemoryTaskManager;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest {

    // создаём экземпляр InMemoryTaskManager
    InMemoryTaskManager manager = new InMemoryTaskManager();

    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    BaseHttpHandler handler = new BaseHttpHandler(manager);

    Gson gson = handler.getGson();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllTasks();
        manager.deleteEpicTasks();
        manager.deleteSubTasks();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    //Тестируем добавление задач всех типов
    @Test
    public void testAddTask() throws IOException, InterruptedException {

        /* Тестируем добавление обычной задачи */


        Task task = new Task("Обычная задача - 1", "Описание обычной задачи",
                manager.getId(), TaskStatus.NEW, LocalDateTime.now(), 10); //id = 0

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI taskUri = URI.create("http://localhost:8080/tasks");
        HttpRequest taskRequest = HttpRequest.newBuilder().uri(taskUri).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Обычная задача - 1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");


        /* Тестируем добавление эпик-задачи */


        LocalDateTime startForEpic = LocalDateTime.of(2024, 9, 15, 14, 0);
        EpicTask epicTask = new EpicTask("Эпическая задача - 1", "Описание эпик-задачи", manager.getId(), TaskStatus.NEW, startForEpic, 30);

        String epicTaskJson = gson.toJson(epicTask);

        URI epicTaskUri = URI.create("http://localhost:8080/epics");
        HttpRequest epicTaskRequest = HttpRequest.newBuilder().uri(epicTaskUri).POST(HttpRequest.BodyPublishers.ofString(epicTaskJson)).build();
        response = client.send(epicTaskRequest, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<EpicTask> epicTasksFromManager = manager.getAllEpicTasks();
        assertNotNull(epicTasksFromManager, "Задачи не возвращаются");
        assertEquals(1, epicTasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Эпическая задача - 1", epicTasksFromManager.get(0).getName(), "Некорректное имя задачи");


        /* Тестируем добавление подзадачи */

        LocalDateTime startForSub = LocalDateTime.of(2024, 9, 15, 14, 0);
        SubTask subTask = new SubTask("Подзадача - 1", "Описание подзадачи", manager.getId(), TaskStatus.NEW, 1, startForSub, 15);

        String subTaskJson = gson.toJson(subTask);

        URI subTaskUri = URI.create("http://localhost:8080/subtasks");
        HttpRequest subTaskRequest = HttpRequest.newBuilder().uri(subTaskUri).POST(HttpRequest.BodyPublishers.ofString(subTaskJson)).build();
        response = client.send(subTaskRequest, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<SubTask> subTasksFromManager = manager.getAllSubtasks();
        assertNotNull(subTasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subTasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Подзадача - 1", subTasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    //Тестируем обновление задач
    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        // создаём задачи
        LocalDateTime startForSub = LocalDateTime.of(2024, 9, 15, 14, 0);
        LocalDateTime startForEpic = LocalDateTime.of(2024, 9, 15, 14, 0);

        Task task = new Task("Обычная задача - 1", "Описание обычной задачи",
                manager.getId(), TaskStatus.NEW, LocalDateTime.now(), 10); // id = 0

        manager.createTask(task);

        EpicTask epicTask = new EpicTask("Эпическая задача - 1", "Описание эпик-задачи", manager.getId(), TaskStatus.NEW, startForEpic, 30); // id = 1

        manager.createEpicTask(epicTask);

        SubTask subTask = new SubTask("Подзадача - 1", "Описание подзадачи", manager.getId(), TaskStatus.NEW, 1, startForSub, 15); // id = 2

        manager.createSubTask(subTask);

        Task updatedTask = new Task("Обновленная задача - 1", "Описание обычной задачи",
                3, TaskStatus.NEW, LocalDateTime.now(), 10);
        SubTask updatedSubTask = new SubTask("Обновленная подзадача - 1", "Описание подзадачи", 4, TaskStatus.NEW, 1, startForSub, 15);

        // конвертируем задачи для обновления в JSON
        String taskJson = gson.toJson(updatedTask);
        String subTaskJson = gson.toJson(updatedSubTask);

        // создаём HTTP-клиент и запросы
        HttpClient client = HttpClient.newHttpClient();

        URI taskUri = URI.create("http://localhost:8080/tasks/0");
        URI subTaskUri = URI.create("http://localhost:8080/subtasks/2");

        HttpRequest taskRequest = HttpRequest.newBuilder().uri(taskUri).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpRequest subTaskRequest = HttpRequest.newBuilder().uri(subTaskUri).POST(HttpRequest.BodyPublishers.ofString(subTaskJson)).build();


        // Проверяем тестами обновление обычной задачи
        HttpResponse<String> response = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что обновилась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Обновленная задача - 1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");


        // Проверяем тестами обновление подзадачи
        response = client.send(subTaskRequest, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что обновилась одна задача с корректным именем
        List<SubTask> subTasksFromManager = manager.getAllSubtasks();

        assertNotNull(subTasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subTasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Обновленная подзадача - 1", subTasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    //Тестируем получение задач всех типов
    @Test
    public void testGetTask() throws IOException, InterruptedException {

        /* Тестируем получение списка обычных задач */


        Task task1 = new Task("Обычная задача - 1", "Описание обычной задачи",
                manager.getId(), TaskStatus.NEW, LocalDateTime.now(), 10); //id = 0

        manager.createTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI taskUri = URI.create("http://localhost:8080/tasks");
        HttpRequest taskRequest = HttpRequest.newBuilder().uri(taskUri).GET().build();
        HttpResponse<String> response = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());

        Type TaskTypeList = new TypeToken<ArrayList<Task>>() {
        }.getType();

        List<Task> receivedTasks = gson.fromJson(response.body(), TaskTypeList);

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что получили список
        assertNotNull(receivedTasks, "Список задач не получен");
        assertEquals(1, receivedTasks.size(), "Некорректное количество задач");
        assertEquals("Обычная задача - 1", receivedTasks.get(0).getName(), "Некорректное имя задачи");


        /* Тестируем получение обычной задачи по ID */

        taskUri = URI.create("http://localhost:8080/tasks/0");
        taskRequest = HttpRequest.newBuilder().uri(taskUri).GET().build();
        response = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());

        Type TaskType = new TypeToken<Task>() {
        }.getType();

        Task receivedTask = gson.fromJson(response.body(), TaskType);

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что получили задачу
        assertNotNull(receivedTask, "Нужная задача не получена");
        assertEquals("Обычная задача - 1", receivedTask.getName(), "Некорректное имя задачи");

        /* Тестируем получение списка эпик-задач */

        LocalDateTime startForEpic = LocalDateTime.of(2024, 9, 15, 14, 0);
        EpicTask epicTask = new EpicTask("Эпик задача - 1", "Описание эпик задачи",
                manager.getId(), TaskStatus.NEW, startForEpic, 30); //id = 1

        manager.createEpicTask(epicTask);

        taskUri = URI.create("http://localhost:8080/epics");
        taskRequest = HttpRequest.newBuilder().uri(taskUri).GET().build();
        response = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());

        Type EpicTaskTypeList = new TypeToken<ArrayList<EpicTask>>() {
        }.getType();

        List<Task> receivedEpicTasks = gson.fromJson(response.body(), EpicTaskTypeList);

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что получили список
        assertNotNull(receivedEpicTasks, "Список задач не получен");
        assertEquals(1, receivedEpicTasks.size(), "Некорректное количество задач");
        assertEquals("Эпик задача - 1", receivedEpicTasks.get(0).getName(), "Некорректное имя задачи");


        /* Тестируем получение эпик задачи по ID */

        taskUri = URI.create("http://localhost:8080/epics/1");
        taskRequest = HttpRequest.newBuilder().uri(taskUri).GET().build();
        response = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());

        Type EpicTaskType = new TypeToken<EpicTask>() {
        }.getType();

        Task receivedEpicTask = gson.fromJson(response.body(), EpicTaskType);

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что получили задачу
        assertNotNull(receivedEpicTask, "Нужная задача не получена");
        assertEquals("Эпик задача - 1", receivedEpicTask.getName(), "Некорректное имя задачи");

        /* Тестируем получение списка подзадач */

        LocalDateTime startForSubTask = LocalDateTime.of(2024, 9, 15, 14, 0);
        SubTask subTask = new SubTask("Подзадача - 1", "Описание эпик задачи",
                manager.getId(), TaskStatus.NEW, 1, startForSubTask, 15); //id = 2

        manager.createSubTask(subTask);

        taskUri = URI.create("http://localhost:8080/subtasks");
        taskRequest = HttpRequest.newBuilder().uri(taskUri).GET().build();
        response = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());

        Type SubTaskTypeList = new TypeToken<ArrayList<SubTask>>() {
        }.getType();

        List<Task> receivedSubTasks = gson.fromJson(response.body(), SubTaskTypeList);

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что получили список
        assertNotNull(receivedSubTasks, "Список задач не получен");
        assertEquals(1, receivedSubTasks.size(), "Некорректное количество задач");
        assertEquals("Подзадача - 1", receivedSubTasks.get(0).getName(), "Некорректное имя задачи");


        /* Тестируем получение подзадачи по ID */

        taskUri = URI.create("http://localhost:8080/subtasks/2");
        taskRequest = HttpRequest.newBuilder().uri(taskUri).GET().build();
        response = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());

        Type SubTaskType = new TypeToken<SubTask>() {
        }.getType();

        Task receivedSubTask = gson.fromJson(response.body(), SubTaskType);

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что получили задачу
        assertNotNull(receivedSubTask, "Нужная задача не получена");
        assertEquals("Подзадача - 1", receivedSubTask.getName(), "Некорректное имя задачи");
    }

    //Тестируем удаление задач всех типов
    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        // Создаем задачи и добавляем в менеджер
        Task task1 = new Task("Обычная задача - 1", "Описание обычной задачи",
                manager.getId(), TaskStatus.NEW, LocalDateTime.now(), 10); //id = 0

        manager.createTask(task1);

        LocalDateTime startForEpic = LocalDateTime.of(2024, 9, 15, 14, 0);
        EpicTask epicTask = new EpicTask("Эпик задача - 1", "Описание эпик задачи",
                manager.getId(), TaskStatus.NEW, startForEpic, 30); //id = 1

        manager.createEpicTask(epicTask);

        LocalDateTime startForSubTask = LocalDateTime.of(2024, 9, 15, 14, 0);
        SubTask subTask = new SubTask("Подзадача - 1", "Описание эпик задачи",
                manager.getId(), TaskStatus.NEW, 1, startForSubTask, 15); //id = 2

        manager.createSubTask(subTask);

        /* Тестируем удаление обычной задачи */

        HttpClient client = HttpClient.newHttpClient();
        URI taskUri = URI.create("http://localhost:8080/tasks/0");
        HttpRequest taskRequest = HttpRequest.newBuilder().uri(taskUri).DELETE().build();
        HttpResponse<String> response = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        //Проверяем что задачу удалили из списка
        assertEquals(0, manager.getAllTasks().size(), "Задача не удалена из списка");

        /* Тестируем удаление подзадачи */

        taskUri = URI.create("http://localhost:8080/subtasks/2");
        taskRequest = HttpRequest.newBuilder().uri(taskUri).DELETE().build();
        response = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        //Проверяем что задачу удалили из списка
        assertEquals(0, manager.getAllSubtasks().size(), "Задача не удалена из списка");

        /* Тестируем удаление эпик задачи */

        taskUri = URI.create("http://localhost:8080/epics/1");
        taskRequest = HttpRequest.newBuilder().uri(taskUri).DELETE().build();
        response = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        //Проверяем что задачу удалили из списка
        assertEquals(0, manager.getAllEpicTasks().size(), "Задача не удалена из списка");
    }

    //Тестируем получение истории
    @Test
    public void testGetHistory() throws IOException, InterruptedException {
        // Создаем задачи и добавляем в менеджер
        Task task = new Task("Обычная задача - 1", "Описание обычной задачи",
                manager.getId(), TaskStatus.NEW, LocalDateTime.now(), 10); //id = 0

        manager.createTask(task);

        LocalDateTime startForEpic = LocalDateTime.of(2024, 9, 15, 14, 0);
        EpicTask epicTask = new EpicTask("Эпик задача - 1", "Описание эпик задачи",
                manager.getId(), TaskStatus.NEW, startForEpic, 30); //id = 1

        manager.createEpicTask(epicTask);

        LocalDateTime startForSubTask = LocalDateTime.of(2024, 9, 15, 14, 0);
        SubTask subTask = new SubTask("Подзадача - 1", "Описание эпик задачи",
                manager.getId(), TaskStatus.NEW, 1, startForSubTask, 15); //id = 2

        manager.createSubTask(subTask);

        //Создаем записи в историю
        manager.getTaskById(0);
        manager.getEpicTaskById(1);
        manager.getSubTaskById(2);


        HttpClient client = HttpClient.newHttpClient();
        URI taskUri = URI.create("http://localhost:8080/history");
        HttpRequest taskRequest = HttpRequest.newBuilder().uri(taskUri).GET().build();
        HttpResponse<String> response = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());

        Type TaskTypeList = new TypeToken<ArrayList<Task>>() {
        }.getType();

        List<Task> receivedTasks = gson.fromJson(response.body(), TaskTypeList);

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что получили список
        assertNotNull(receivedTasks, "Список задач не получен");
        assertEquals(3, receivedTasks.size(), "Некорректное количество задач");
        assertEquals("Обычная задача - 1", receivedTasks.get(0).getName(), "Некорректное имя задачи");
    }

    //Тестируем получение листа приоритета
    @Test
    public void testPriority() throws IOException, InterruptedException {
        // Создаем задачи и добавляем в менеджер
        Task task = new Task("Обычная задача - 1", "Описание обычной задачи",
                manager.getId(), TaskStatus.NEW, LocalDateTime.now(), 10); //id = 0

        manager.createTask(task);

        LocalDateTime startForEpic = LocalDateTime.of(2024, 9, 15, 14, 0);
        EpicTask epicTask = new EpicTask("Эпик задача - 1", "Описание эпик задачи",
                manager.getId(), TaskStatus.NEW, startForEpic, 30); //id = 1

        manager.createEpicTask(epicTask);

        LocalDateTime startForSubTask = LocalDateTime.of(2024, 9, 16, 14, 5);
        SubTask subTask = new SubTask("Подзадача - 1", "Описание эпик задачи",
                manager.getId(), TaskStatus.NEW, 1, startForSubTask, 15); //id = 2

        manager.createSubTask(subTask);


        HttpClient client = HttpClient.newHttpClient();
        URI taskUri = URI.create("http://localhost:8080/prioritized");
        HttpRequest taskRequest = HttpRequest.newBuilder().uri(taskUri).GET().build();
        HttpResponse<String> response = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());

        Type TaskTypeSet = new TypeToken<Set<Task>>() {
        }.getType();

        Set<Task> receivedTasks = gson.fromJson(response.body(), TaskTypeSet);

        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что получили список
        assertNotNull(receivedTasks, "Список задач не получен");

        //проверяем, что получили верное количество задач
        assertEquals(3, receivedTasks.size(), "Некорректное количество задач");
    }
}
