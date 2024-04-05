package historymanagers;

import tasks.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void addToHistory(Task task);

    ArrayList<Task> getHistory();

    void remove(int id);
}