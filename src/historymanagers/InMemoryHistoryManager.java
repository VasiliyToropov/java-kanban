package historymanagers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, HistoryLinkedList.Node<Task>> historyMap;
    private final HistoryLinkedList<Task> linkedTasks;

    public InMemoryHistoryManager() {
        historyMap = new HashMap<>();
        linkedTasks = new HistoryLinkedList<>();
    }

    @Override
    public void addToHistory(Task task) {
        remove(task.getId());
        historyMap.put(task.getId(), linkedTasks.linkLast(task));
    }

    @Override
    public ArrayList<Task> getHistory() {
        return linkedTasks.getTasks();
    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            linkedTasks.removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }


}