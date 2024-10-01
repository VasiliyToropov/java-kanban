package httphandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanagers.TaskManager;
import tasks.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {
    public SubTaskHandler(TaskManager manager) {
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
                String readedSubTask = convertInputStreamToString(inputStream);
                SubTask requestSubTask = gson.fromJson(readedSubTask, SubTask.class);


                if (Pattern.matches("^/subtasks$", path)) {
                    taskManager.createSubTask(requestSubTask);
                    response = gson.toJson(requestSubTask);
                    sendText201(exchange, response);
                    return;
                    /* Статус 406 писать нет смысла, так как при создании задачи мы не передаем id. Он присваивается в
                     методе createSubTask();*/
                }

                if (Pattern.matches("^/subtasks/\\d+$", path)) {
                    String pathID = path.replaceFirst("/subtasks/", "");
                    int id = parsePathId(pathID);

                    if (id != -1) {
                        taskManager.updateSubTask(id, requestSubTask);
                        response = gson.toJson(requestSubTask);
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

                if (Pattern.matches("^/subtasks$", path)) {
                    response = gson.toJson(taskManager.getAllSubtasks());
                    sendText200(exchange, response);
                    return;
                }

                if (Pattern.matches("^/subtasks/\\d+$", path)) {
                    String pathID = path.replaceFirst("/subtasks/", "");
                    int id = parsePathId(pathID);

                    if (id != -1) {
                        response = gson.toJson(taskManager.getSubTaskById(id));
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

                if (Pattern.matches("^/subtasks/\\d+$", path)) {
                    String pathID = path.replaceFirst("/subtasks/", "");
                    int id = parsePathId(pathID);

                    if (id != -1) {
                        taskManager.deleteSubTaskById(id);
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
