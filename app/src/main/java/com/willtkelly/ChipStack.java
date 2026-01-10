
package com.willtkelly;

public class ChipStack {

    private int[] data; 

    private int capacity;
    private int stack_pointer;

    public ChipStack(int max_capacity) {

        this.capacity = max_capacity;
        this.stack_pointer = 0;

        this.data = new int[max_capacity];
    }

    public void push(int elem) {
        if (this.isFull()) {
            throw new MaxSubroutineError(this.capacity);
        }

        this.data[this.stack_pointer++] = elem;
    }


    public int pop() {
        if (this.isEmpty()) {
            return 0;
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
