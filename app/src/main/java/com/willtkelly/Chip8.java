
package com.willtkelly;

public class Chip8 {

    // Memory
    final int MEMORY_CAPACITY = 4096; // 4KB
    byte[] memory = new byte[MEMORY_CAPACITY];
    
    // Registers
    final int REGISTERS = 16;
    byte[] registers = new byte[REGISTERS];

    // Program Counter
    int programCounter = 0x200;

    // Timers
    
    public Chip8() {
    
    }

}
