package HistoryManagers;

import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node<Task>> historyMap = new HashMap<>();
    private final HistoryLinkedList<Task> linkedTasks = new HistoryLinkedList<>();

    static class HistoryLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;

        public Node<T> linkLast(T addedTask) {
            Node<T> oldTail = tail;
            Node<T> newNode = new Node<>(oldTail, addedTask, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            return newNode;
        }

        public ArrayList<T> getTasks() {
            ArrayList<T> result = new ArrayList<>();

            for (Node<T> x = head; x != null; x = x.next)
                result.add(x.item);
            return result;
        }

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
        if(historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }

    public void removeNode(Node<Task> node) {
        Node<Task> prevNode = node.prev;
        Node<Task> nextNode = node.next;

        if(nextNode == null && prevNode == null) {
            linkedTasks.tail = null;
            linkedTasks.head = null;
            return;
        }

        if(nextNode == null) {
            linkedTasks.tail = prevNode;
            prevNode.next = null;
            return;
        }

        if(prevNode == null) {
            linkedTasks.head = nextNode;
            nextNode.prev = null;
            return;
        }

        prevNode.next = nextNode;
        nextNode.prev = prevNode;
    }
}