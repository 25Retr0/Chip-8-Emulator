
package com.willtkelly;

import java.io.IOException;

public class Chip8 {

    private final double TARGET_FPS = 60.0;

    // Memory
    private final int MEMORY_CAPACITY = 4096; // 4KB
    private byte[] memory = new byte[MEMORY_CAPACITY];
    private int START_OF_PROGRAM = 0x200;
    
    // Registers
    private final int REGISTERS = 16;
    private byte[] V_registers = new byte[REGISTERS];
    private byte I_register = 0;

    // Program Counter
    private int programCounter = START_OF_PROGRAM;

    // Stack Pointer
    private final int MAX_SUBROUTINES = 16;
    private ChipStack stack = new ChipStack(MAX_SUBROUTINES);

    // Screen
    private final int width = 64;
    private final int height = 32;
    private byte[][] screen = new byte[height][width];
    Chip8_Display display_window;
    
    // Timers
    private int delay_timer = 0;
    private int sound_timer = 0;
    
    public Chip8() {
        this.load_fonts_to_memory();

        boolean rom_loaded = this.load_rom_to_memory("roms/Chip8_Picture.ch8");
        if (!rom_loaded) { return; }

        this.run();
    }

    public void setDisplay(Chip8_Display display) {
        this.display_window = display;
    }

    private void run() {
        long lastTime = System.nanoTime();
        final double ns = 1_000_000.0 / TARGET_FPS;
        double delta = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                // TODO:
                
                // Fetch
                int opcode = this.fetch_instruction();

                // Increment PC
                this.programCounter += 2;

                // Decode and execute
                this.decode_execute(opcode);


                // Countdown Timers
                if (this.delay_timer > 0) {
                    this.delay_timer--;
                }

                if (this.sound_timer > 0) {
                    this.sound_timer--;
                }
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
                this.screen = new byte[height][width];
                
            } else if (opcode == 0x00EE) { // 00EE - RET
                // Return from a subroutine
                // The interpreter sets the PC to the address at top of stack.
                int return_address = this.stack.pop();
                if (return_address == 0) {
                    // FIX: Possible Error?
                }
                this.programCounter = return_address;

            } else { // 0nnn - SYS addr
                System.out.println("SYS addr command executed.");
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
            if (n == 0) { // 8xy0 - LD Vx, Vy
                // Set Vx = Vy
                this.V_registers[x] = this.V_registers[y];
            } else if (n == 1) { // 8xy1 - OR Vx, Vy
                // Set Vx = Vx OR Vy
                int result = this.V_registers[x] | this.V_registers[y];
                this.V_registers[x] = (byte) (result & 0xFF);
            } else if (n == 2) { // 8xy2 - AND Vx, Vy
                // Set Vx = Vx AND Vy
                int result = this.V_registers[x] & this.V_registers[y];
                this.V_registers[x] = (byte) (result & 0xFF);
            } else if (n == 3) { // 8xy3 - XOR Vx, Vy
                // Set Vx = Vx XOR Vy
                int result = this.V_registers[x] ^ this.V_registers[y];
                this.V_registers[x] = (byte) (result & 0xFF);
            } else if (n == 4) { // 8xy4 - ADD Vx, Vy
                // Set Vx = Vx + Vy, Set VF = carry
                int vx = V_registers[x] & 0xFF;
                int vy = V_registers[y] & 0XFF;
                int sum = vx + vy;
                V_registers[0xF] = (byte) (sum > 255 ? 1 : 0);
                V_registers[x] = (byte) (sum & 0xFF);
            } else if (n == 5) { // 8xy5 - SUB Vx, Vy
                // Set Vx = Vx - Vy, Set VF = NOT borrow
                int vx = V_registers[x] & 0xFF;
                int vy = V_registers[y] & 0XFF;
                V_registers[0xF] = (byte) (vx > vy ? 1 : 0);
                int result = vx - vy;
                V_registers[x] = (byte) (result & 0xFF);
            } else if (n == 6) { // 8xy6 - SHR Vx {, Vy}
                // Set Vx = Vx SHR 1
                // If the LSB f Vx is 1, then VF is set to 1, otherwise 0.
                // Then Vx is divided by 2.
                int vx = V_registers[x] & 0xFF;
                int lsb = vx & 0x01;
                V_registers[0xF] = (byte) (lsb & 0xFF);
                vx = vx >> 1; // SHR
                V_registers[x] = (byte) (vx & 0xFF);
            } else if (n == 7) { // 8xy7 - SUBN Vx, Vy
                // Set Vx = Vy - Vx, Set VF = NOT borrow
                int vx = V_registers[x] & 0xFF;
                int vy = V_registers[y] & 0XFF;
                V_registers[0xF] = (byte) (vy > vx ? 1 : 0);
                int result = vy - vx;
                V_registers[x] = (byte) (result & 0xFF);
            } else if (n == 0xE) { // 8xyE - SHL Vx {, Vy}
                // Set Vx = Vx SHL 1.
                // If the MSB of Vx is 1, Set VF to 1, otherwise 0. Then Vx
                // is multiplied by 2.
                int vx = V_registers[x] & 0xFF;
                int msb = (vx & 0x80) >> 7;  // msb will be 0 or 1
                V_registers[0xF] = (byte) (msb & 0xFF);
                vx = (vx << 1) & 0xFF; // SHL, mask to 8-bit
                V_registers[x] = (byte) (vx & 0xFF);
            }
        } else if (op == 9) { // 9xy0 - SNE Vx, Vy
            // Skip next instruction if Vx != Vy.
            if (this.V_registers[x] != this.V_registers[y]) {
                this.programCounter += 2;
            }
        } else if (op == 0xA) { // Annn - LD I, addr
            // Set I register to nnn.
            I_register = (byte) (nnn & 0xFF);
        } else if (op == 0xB) { // Bnnn - JP V0, addr
            // Jump to location nnn + V0.
            int v0 = this.V_registers[0] & 0xFF;
            int location = nnn + v0;
            this.programCounter = (byte) (location & 0xFF);
        } else if (op == 0xC) { // Cxkk - RND Vx, byte


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

    private void load_fonts_to_memory() {
        int font_start = 0x50;
        
        // '0'
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x90;
        this.memory[font_start++] = (byte) 0x90;
        this.memory[font_start++] = (byte) 0x90;
        this.memory[font_start++] = (byte) 0xF0;

        // '1'
        this.memory[font_start++] = (byte) 0x20;
        this.memory[font_start++] = (byte) 0x60;
        this.memory[font_start++] = (byte) 0x20;
        this.memory[font_start++] = (byte) 0x20;
        this.memory[font_start++] = (byte) 0x70;


        // '2'
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x10;
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x80;
        this.memory[font_start++] = (byte) 0xF0;

        // '3'
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x10;
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x10;
        this.memory[font_start++] = (byte) 0xF0;

        // '4'
        this.memory[font_start++] = (byte) 0x90;
        this.memory[font_start++] = (byte) 0x90;
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x10;
        this.memory[font_start++] = (byte) 0x10;

        // '5'
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x80;
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x10;
        this.memory[font_start++] = (byte) 0xF0;

        // '6'
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x80;
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x90;
        this.memory[font_start++] = (byte) 0xF0;

        // '7'
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x10;
        this.memory[font_start++] = (byte) 0x20;
        this.memory[font_start++] = (byte) 0x40;
        this.memory[font_start++] = (byte) 0x40;

        // '8'
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x90;
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x90;
        this.memory[font_start++] = (byte) 0xF0;

        // '9'
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x90;
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x10;
        this.memory[font_start++] = (byte) 0xF0;

        // 'A'
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x90;
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x90;
        this.memory[font_start++] = (byte) 0x90;

        // 'B'
        this.memory[font_start++] = (byte) 0xE0;
        this.memory[font_start++] = (byte) 0x90;
        this.memory[font_start++] = (byte) 0xE0;
        this.memory[font_start++] = (byte) 0x90;
        this.memory[font_start++] = (byte) 0xE0;

        // 'C'
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x80;
        this.memory[font_start++] = (byte) 0x80;
        this.memory[font_start++] = (byte) 0x80;
        this.memory[font_start++] = (byte) 0xF0;

        // 'D'
        this.memory[font_start++] = (byte) 0xE0;
        this.memory[font_start++] = (byte) 0x90;
        this.memory[font_start++] = (byte) 0x90;
        this.memory[font_start++] = (byte) 0x90;
        this.memory[font_start++] = (byte) 0xE0;

        // 'E'
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x80;
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x80;
        this.memory[font_start++] = (byte) 0xF0;

        // 'F'
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x80;
        this.memory[font_start++] = (byte) 0xF0;
        this.memory[font_start++] = (byte) 0x80;
        this.memory[font_start++] = (byte) 0x80;
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


    

}
