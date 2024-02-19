package com.fmt;

import java.util.Comparator;
import java.util.Iterator;

public class LinkedList<T> implements Iterable<T> {

	private ListNode<T> head;
	private int size;
	private final Comparator<T> comparator;

	/**
	 * Constructor for head and size
	 * 
	 * @param head
	 * @param size
	 */
	public LinkedList(Comparator<T> comparator) {
		super();
		this.comparator = comparator;
		this.head = null;
		this.size = 0;
	}

	/**
	 * This function returns the size of the list, the number of elements currently
	 * stored in it.
	 * 
	 * @return
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * This function clears out the contents of the list, making it an empty list.
	 */
	public void clear() {
		head = null;
		size = 0;
	}

	/**
	 * This adds an element to the linked list, using comparators to place it in the right spot.
	 * 
	 * @param t
	 */

	public void add(T o) {
		ListNode<T> newNode = new ListNode<T>(o);
		if (this.head == null) {
			this.head = newNode;
			this.size++;
		} else {
			ListNode<T> curr = this.head;
			ListNode<T> prev = null;
			
			if (comparator.compare(curr.getO(), o) > 0) {
				curr = this.head;
				
				while (curr != null && comparator.compare(curr.getO(), o) > 0) {
					prev = curr;
					curr = curr.getNext();
				}
				if (curr != null) {
					newNode.setNext(curr);
				}
				if (prev != null) {
					prev.setNext(newNode);
				}
			} else {
			    newNode.setNext(this.head);
			    this.head = newNode;
			}
			this.size++;
		}
	}

	/**
	 * This method removes the {@link Invoice} from the given <code>position</code>,
	 * indices start at 0. Implicitly, the remaining elements' indices are reduced.
	 * 
	 * @param position
	 */
	public void remove(int position) {
		if (position < 0 || position >= this.size) {
			throw new IllegalArgumentException("Invalid Index" + position);
		} else if (position == 0) {
			this.head = this.head.getNext();
			this.size--;
		} else {
			ListNode<T> previous = this.getListNode(position - 1);
			ListNode<T> current = previous.getNext();
			ListNode<T> next = current.getNext();

			previous.setNext(next);
			this.size--;
		}
	}

	/**
	 * This is a private helper method that returns a {@link invoiceListNode}
	 * corresponding to the given position. Implementing this method is optional but
	 * may help you with other methods.
	 * 
	 * @param position
	 * @return
	 */
	private ListNode<T> getListNode(int position) {
		ListNode<T> current = this.head;
		for (int i = 0; i < position; i++) {
			current = current.getNext();
		}
		return current;
	}

	/**
	 * Returns the {@link Invoice} element stored at the given (O meaning 'object')
	 * <code>position</code>.
	 * 
	 * @param position
	 * @return
	 */
	public T getO(int position) {
		T o;
		if (position < 0 || position >= this.size) {
			throw new IllegalArgumentException("Invalid Index " + position);
		} else {
			o = this.getListNode(position).getO();

			return o;
		}
	}

	/**
	 * Prints this list to the standard output.
	 */
	public void print() {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");

		if (this.size > 0) {
			ListNode<T> current = this.head;
			for (int i = 0; i < this.size - 1; i++) {
				sb.append(current.getO() + ", ");
				current = current.getNext();
			}
			sb.append(" ]");
			System.out.println(sb);
		}

	}

	/*
	 * this method implements Iterator, allowing for the use of enhanced for loops in the main function.
	 */
	@Override
    public Iterator<T> iterator() {
        Iterator<T> it = new Iterator<T>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public T next() {
                return getO(currentIndex++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }

}
