
package com.willtkelly;

public class MaxSubroutineError extends RuntimeException {
    public MaxSubroutineError(int max_subroutines) {
        super("CHIP-8 stack overflow: maximum " + max_subroutines + " subroutines");
    }
}

