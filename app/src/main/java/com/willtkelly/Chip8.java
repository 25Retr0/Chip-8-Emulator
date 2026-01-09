
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

    
    public Chip8() {
        load_rom_to_memory("resources/ROM/Chip8_Picture.ch8");
        System.out.println(memory);
    }


    public boolean load_rom_to_memory(String path) {
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

}
