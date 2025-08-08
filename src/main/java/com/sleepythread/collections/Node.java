package com.sleepythread.collections;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Node<T> {
    private T value;
    Node<T> next;

    public Node(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        var current = this;
        builder.append(current.getValue());
        if (next != null) {
            builder.append("->").append(next.getValue());
        }
        return builder.toString();
    }

    void reverse() {
        var newNext = next;
        this.next = null;
        newNext.next = this;

    }
}
