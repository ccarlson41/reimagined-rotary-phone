package com.fmt;
/*
 * This class contains all functions and workings of Nodes within a linked list
 */
public class ListNode<T> {

    private ListNode<T> next;
    private final T o;

    public ListNode(T invoice) {
        this.o = invoice;
        this.next = null;
    }

    public T getO() {
        return o;
    }

    public ListNode<T> getNext() {
        return next;
    }

    public void setNext(ListNode<T> next) {
        this.next = next;
    }
    
    public boolean hasNext() {
        return(this.next != null);
    }
}
