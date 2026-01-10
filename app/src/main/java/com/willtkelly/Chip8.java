
package com.willtkelly;

import java.io.IOException;

public class Chip8 {

    final double TARGET_FPS = 60.0;

    // Memory
    final int MEMORY_CAPACITY = 4096; // 4KB
    byte[] memory = new byte[MEMORY_CAPACITY];
    int START_OF_PROGRAM = 0x200;
    
    // Registers
    final int REGISTERS = 16;
    byte[] V_registers = new byte[REGISTERS];
    int I_register = 0;

    // Program Counter
    int programCounter = START_OF_PROGRAM;

    // Stack Pointer
    final int MAX_SUBROUTINES = 16;
    ChipStack stack = new ChipStack(MAX_SUBROUTINES);
    
    
    public Chip8() {
        // Initialization
        this.load_rom_to_memory("roms/Chip8_Picture.ch8");

        this.run();
    }

    private void run() {
        long lastTime = System.nanoTime();
        final double ns = 1_000_000.0 / TARGET_FPS;
        double delta = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                // TODO:
                
                // Fetch
                int opcode = this.fetch_instruction();

                // Increment PC
                this.programCounter += 2;

                // Decode and execute
                this.decode_execute(opcode);

                return; // Temporary
            }
        }
    }


    private int fetch_instruction() {
        // NOTE: Fetch 2 bytes everytime.
        int high = this.memory[this.programCounter] & 0xFF;
        int low = this.memory[this.programCounter + 1] & 0xFF;
        return (high << 8) | low;
    }


    private void decode_execute(int opcode) {
        // Nibbles
        int op = (opcode >> 12) & 0xF;
        int x = (opcode >> 8) & 0xF;
        int y = (opcode >> 4) & 0xF;
        int n = opcode & 0xF;
        int kk = opcode & 0xFF;
        int nnn = opcode & 0xFFF;

        // Standard Chip-8 Instructions
        
        if (op == 0) {
            if (opcode == 0x00E0) { // 00E0 - CLS
                // TODO: Clear the display
                
            } else if (opcode == 0x00EE) { // 00EE - RET
                // Return from a subroutine
                // The interpreter sets the PC to the address at top of stack.
                int return_address = this.stack.pop();
                if (return_address == 0) {
                    // FIX: Possible Error?
                }
                this.programCounter = return_address;

            } else { // 0nnn - SYS addr

            }
        } else if (op == 1) { // 1nnn - JP addr
            // Jump to location nnn. Set PC to nnn.
            this.programCounter = nnn;
        } else if (op == 2) { // 2nnn - CALL addr
            // Call subroutine at nnn. Put current PC on top of the stack.
            // PC is then set to nnn.
            
            // FIX: stack.push can throw an exception
            this.stack.push(this.programCounter);
            this.programCounter = nnn;
        } else if (op == 3) { // 3xkk - SE Vx, byte
            // Skip next instruction if Vx = kk.
            if ((this.V_registers[x] & 0xFF) == kk) {
                this.programCounter += 2;
            }
        } else if (op == 4) { // 4xkk - SNE Vx, byte
            // Skip next instruction if Vx != kk.
            if ((this.V_registers[x] & 0xFF) != kk) {
                this.programCounter += 2;
            }
        } else if (op == 5) { // 5xy0 - SE Vx, Vy
            // Skip next instruction if Vx = Vy.
            if (this.V_registers[x] == this.V_registers[y]) {
                this.programCounter += 2;
            }
        } else if (op == 6) { // 6xkk - LD Vx, byte
            // The interpreter puts the value kk into register Vx.
            this.V_registers[x] = (byte) (kk & 0xFF);
        } else if (op == 7) { // 7xkk - Add Vx, byte
            // Adds the value kk to the value of register Vx. Stores in Vx.
            int Vx = this.V_registers[x] & 0xFF;
            Vx = Vx + kk;
            this.V_registers[x] = (byte) (Vx & 0xFF);
        } else if (op == 8) {
            if (n == 0) { // LD Vx, Vy
                // Set Vx = Vy
                this.V_registers[x] = this.V_registers[y];
            } else if (n == 1) {

            }
        }
    }


    /**
     */
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


    /**
     */
    private void print_memory_values() {
        for (int i = START_OF_PROGRAM; i < MEMORY_CAPACITY; i++) {
            System.out.println(memory[i] & 0xFF);
        }
    }


    /**
     */
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


    /**
     */
    

}
