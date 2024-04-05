package historymanagers;

import java.util.ArrayList;

class HistoryLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;

    public static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

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

    public void removeNode(Node<T> node) {
        Node<T> prevNode = node.prev;
        Node<T> nextNode = node.next;

        if (nextNode == null && prevNode == null) {
            tail = null;
            head = null;
            return;
        }

        if (nextNode == null) {
            tail = prevNode;
            prevNode.next = null;
            return;
        }

        if (prevNode == null) {
            head = nextNode;
            nextNode.prev = null;
            return;
        }

        prevNode.next = nextNode;
        nextNode.prev = prevNode;
    }
}
