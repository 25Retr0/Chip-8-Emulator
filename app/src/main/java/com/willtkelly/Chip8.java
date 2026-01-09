
package com.willtkelly;

import java.io.IOException;

public class Chip8 {

    // Memory
    final int MEMORY_CAPACITY = 4096; // 4KB
    byte[] memory = new byte[MEMORY_CAPACITY];
    int START_OF_PROGRAM = 0x200;
    
    // Registers
    final int REGISTERS = 16;
    byte[] registers = new byte[REGISTERS];

    // Program Counter
    int programCounter = START_OF_PROGRAM;

    // Stack Pointer
    final int MAX_SUBROUTINES = 16;
    ChipStack<int> stack = new ChipStack<>(MAX_SUBROUTINES);
    
    
    public Chip8() {
        load_rom_to_memory("roms/Chip8_Picture.ch8");
    }


    private boolean load_rom_to_memory(String path) {
        try {
            byte[] rom = RomLoader.loadRom(path);

            for (int i = 0; i < rom.length; i++) {
                this.memory[START_OF_PROGRAM + i] = rom[i];            
            }

            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }


    private void print_memory_values() {
        for (int i = START_OF_PROGRAM; i < MEMORY_CAPACITY; i++) {
            System.out.println(memory[i] & 0xFF);
        }
    }


    private void print_memory_hex() {
        int counter = 1;
        for (int i = START_OF_PROGRAM; i < MEMORY_CAPACITY; i++) {
            counter++;
            System.out.printf("%02X ", memory[i] & 0xFF);

            if (counter == 4) {
                System.out.println();
                counter = 0;
            }
        }
    }

}
