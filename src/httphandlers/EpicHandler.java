package httphandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanagers.TaskManager;
import tasks.EpicTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    public EpicHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        switch (method) {
            case "POST": {
                InputStream inputStream = exchange.getRequestBody();
                String readedTask = convertInputStreamToString(inputStream);
                EpicTask requestTask = gson.fromJson(readedTask, EpicTask.class);


                if (Pattern.matches("^/epics$", path)) {
                    taskManager.createEpicTask(requestTask);
                    String response = gson.toJson(requestTask);
                    sendText201(exchange, response);
                    return;
                    /* Статус 406 писать нет смысла, так как при создании задачи мы не передаем id. Он присваивается в
                     методе createEpicTask();*/
                }

                break;
            }

            case "GET": {
                String response = "Задача с таким ID не найдена";

                if (Pattern.matches("^/epics$", path)) {
                    response = gson.toJson(taskManager.getAllEpicTasks());
                    sendText200(exchange, response);
                    return;
                }

                if (Pattern.matches("^/epics/\\d+$", path)) {
                    String pathID = path.replaceFirst("/epics/", "");
                    int id = parsePathId(pathID);

                    if (id != -1) {
                        response = gson.toJson(taskManager.getEpicTaskById(id));
                        sendText200(exchange, response);
                    } else {
                        sendNotFound404(exchange, response);
                        break;
                    }
                } else {
                    sendNotFound404(exchange, response);
                    break;
                }

                if (Pattern.matches("^/epics/\\d+/subtasks$", path)) {
                    String pathID = path.replace("/epics/", "").replace("/subtasks", "");
                    int id = parsePathId(pathID);

                    if (id != -1) {
                        response = gson.toJson(taskManager.getEpicTaskById(id).getSubtasksId());
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

                if (Pattern.matches("^/epics/\\d+$", path)) {
                    String pathID = path.replaceFirst("/epics/", "");
                    int id = parsePathId(pathID);

                    if (id != -1) {
                        taskManager.deleteEpicTaskById(id);
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
