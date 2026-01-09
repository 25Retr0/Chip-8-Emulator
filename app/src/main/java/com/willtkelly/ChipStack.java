
package com.willtkelly;

public class ChipStack<T> {

    private T[] data; 

    private int capacity;
    private int stack_pointer;

    @SuppressWarnings("unchecked")
    public ChipStack(int max_capacity) {

        this.capacity = max_capacity;
        this.stack_pointer = 0;

        this.data = (T[]) new Object[max_capacity];
    }

    public void push(T elem) {
        if (this.isFull()) {
            throw new MaxSubroutineError(this.capacity);
        }

        this.data[this.stack_pointer++] = elem;
    }


    public T pop() {
        if (this.isEmpty()) {
            return null;
        }

        return this.data[--this.stack_pointer];
    }


    public int size() {
        return this.stack_pointer;
    }


    public boolean isEmpty() {
        return this.stack_pointer == 0;
    }

    
    public boolean isFull() {
        return this.stack_pointer == this.capacity;
    }

}
