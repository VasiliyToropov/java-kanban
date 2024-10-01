package httphandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanagers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    public TaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        switch (method) {
            case "POST": {
                String response = "Задача с таким ID не найдена";
                InputStream inputStream = exchange.getRequestBody();
                String readedTask = convertInputStreamToString(inputStream);
                Task requestTask = gson.fromJson(readedTask, Task.class);


                if (Pattern.matches("^/tasks$", path)) {
                    taskManager.createTask(requestTask);
                    response = gson.toJson(requestTask);
                    sendText201(exchange, response);
                    return;
                    /* Статус 406 писать нет смысла, так как при создании задачи мы не передаем id. Он присваивается в
                     методе createTask();*/
                }

                if (Pattern.matches("^/tasks/\\d+$", path)) {
                    String pathID = path.replaceFirst("/tasks/", "");
                    int id = parsePathId(pathID);

                    if (id != -1) {
                        taskManager.updateTask(id, requestTask);
                        response = gson.toJson(requestTask);
                        sendText201(exchange, response);
                    } else {
                        sendNotFound404(exchange, response);
                        break;
                    }
                } else {
                    sendNotFound404(exchange, response);
                    break;
                }

                break;
            }

            case "GET": {
                String response = "Задача с таким ID не найдена";

                if (Pattern.matches("^/tasks$", path)) {
                    response = gson.toJson(taskManager.getAllTasks());
                    sendText200(exchange, response);
                    return;
                }

                if (Pattern.matches("^/tasks/\\d+$", path)) {
                    String pathID = path.replaceFirst("/tasks/", "");
                    int id = parsePathId(pathID);

                    if (id != -1) {
                        response = gson.toJson(taskManager.getTaskById(id));
                        sendText200(exchange, response);
                    } else {
                        sendNotFound404(exchange, response);
                        break;
                    }
                } else {
                    sendNotFound404(exchange, response);
                    break;
                }

                break;
            }

            case "DELETE": {
                String response = "Задача с таким ID не найдена";

                if (Pattern.matches("^/tasks/\\d+$", path)) {
                    String pathID = path.replaceFirst("/tasks/", "");
                    int id = parsePathId(pathID);

                    if (id != -1) {
                        taskManager.deleteTaskById(id);
                        response = "Задача с ID " + id + " удалена";
                        sendText200(exchange, response);
                    } else {
                        sendNotFound404(exchange, response);
                        break;
                    }
                } else {
                    sendNotFound404(exchange, response);
                    break;
                }
                break;
            }

            default: {
                System.out.println("Internal Server Error");
                exchange.sendResponseHeaders(500, 0);
            }
        }
    }
}
