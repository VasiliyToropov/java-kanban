import httpserver.HttpTaskServer;
import taskmanagers.TaskManager;
import utility.Managers;

import java.io.IOException;

public class Main {
    static TaskManager taskManager;

    static {
        try {
            taskManager = Managers.getDefault();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {

        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
        httpTaskServer.stop();

    }
}
