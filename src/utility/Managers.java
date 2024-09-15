package utility;

import historymanagers.*;
import taskmanagers.*;

import java.io.IOException;

public class Managers {
    public static TaskManager getDefault() throws IOException {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
