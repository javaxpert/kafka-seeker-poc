package com.sleepythread.collections;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinkedList<T> {
    private Node<T> head;

    public void add(T value) {
        var item = new Node<T>(value);
        if (head == null) {
            head = item;
        }
        else {
            var current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(item);
        }
    }

    public void reverse() {
        Node prev = null;
        Node curr = head;
        Node next = null;
        while (curr != null) {
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        head = prev;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        if (head != null) {
            var current = head;
            builder.append(current);
            while (current.getNext() != null) {
                builder.append(",");
                current = current.getNext();
                builder.append(current);
            }
        }
        return builder.toString();
    }

    private void logPCN(Node p, Node c, Node n) {
        log.info("P:{} C:{} N:{}", p, c, n);

    }
}
