package HistoryManagers;

import Tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final int historyLimit = 10;
    private final ArrayList<Task> tasksInHistory = new ArrayList<>(historyLimit);

    @Override
    public void addToHistory(Task task) {
        if (task != null) {
            if (tasksInHistory.size() == historyLimit) {
                tasksInHistory.removeFirst();
            }
            tasksInHistory.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return tasksInHistory;
    }
}
