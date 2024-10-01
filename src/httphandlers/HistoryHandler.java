package httphandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanagers.TaskManager;

import java.io.IOException;
import java.util.regex.Pattern;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    public HistoryHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            String response = "История не найдена";

            if (Pattern.matches("^/history$", path)) {
                response = gson.toJson(taskManager.getHistoryManager().getHistory());
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