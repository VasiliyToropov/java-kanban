package httphandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanagers.TaskManager;

import java.io.IOException;
import java.util.regex.Pattern;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    public PrioritizedHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            String response = "Задачи не найдены";

            if (Pattern.matches("^/prioritized$", path)) {
                response = gson.toJson(taskManager.getPrioritizedTasks());
                sendText200(exchange, response);
            } else {
                sendNotFound404(exchange, response);
            }

        } else {
            System.out.println("Internal Server Error");
            exchange.sendResponseHeaders(500, 0);
        }
    }
}
