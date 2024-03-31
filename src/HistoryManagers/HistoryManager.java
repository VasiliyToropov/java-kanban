package HistoryManagers;

import Tasks.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void addToHistory(Task task);

    ArrayList<Task> getHistory();
}