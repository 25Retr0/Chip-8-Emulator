package com.willtkelly;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public class Keyboard implements KeyListener {

    private int[] keymap;
    private boolean[] keyState;

    public Keyboard() {
        this.keymap = new int[256];
        Arrays.fill(this.keymap, -1);

        this.keymap[KeyEvent.VK_1] = 0x1;
        this.keymap[KeyEvent.VK_2] = 0x2;
        this.keymap[KeyEvent.VK_3] = 0x3;
        this.keymap[KeyEvent.VK_4] = 0xC;

        this.keymap[KeyEvent.VK_Q] = 0x4;
        this.keymap[KeyEvent.VK_W] = 0x5;
        this.keymap[KeyEvent.VK_E] = 0x6;
        this.keymap[KeyEvent.VK_R] = 0xD;

        this.keymap[KeyEvent.VK_A] = 0x7;
        this.keymap[KeyEvent.VK_S] = 0x8;
        this.keymap[KeyEvent.VK_D] = 0x9;
        this.keymap[KeyEvent.VK_F] = 0xE;

        this.keymap[KeyEvent.VK_Z] = 0xA;
        this.keymap[KeyEvent.VK_X] = 0x0;
        this.keymap[KeyEvent.VK_C] = 0xB;
        this.keymap[KeyEvent.VK_V] = 0xF;


        this.keyState = new boolean[16];
        Arrays.fill(this.keyState, false);
    }

    public void setKeymap(int[] keymap) {
        this.keymap = keymap;
    }

    public boolean isKeyPressed(int key) {
        return this.keyState[key];
    }

    public boolean[] getKeysPressed() {
        return Arrays.copyOf(keyState, keyState.length);
    }


    @Override
    public void keyTyped(KeyEvent keyEvent) {
        // Unused for Chip-8 Emulators
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code < 0 || code >= keymap.length) return;

        int chip8Key = keymap[code];
        if (chip8Key != -1) {
            keyState[chip8Key] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code < 0 || code >= keymap.length) return;

        int chip8Key = keymap[code];
        if (chip8Key != -1) {
            keyState[chip8Key] = false;
        }
    }
}

