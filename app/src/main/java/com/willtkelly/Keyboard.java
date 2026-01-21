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

        this.keymap['1'] = 0x1;
        this.keymap['2'] = 0x2;
        this.keymap['3'] = 0x3;
        this.keymap['4'] = 0xC;

        this.keymap['q'] = 0x4;
        this.keymap['w'] = 0x5;
        this.keymap['e'] = 0x6;
        this.keymap['r'] = 0xD;

        this.keymap['a'] = 0x7;
        this.keymap['s'] = 0x8;
        this.keymap['d'] = 0x9;
        this.keymap['f'] = 0xE;

        this.keymap['z'] = 0xA;
        this.keymap['x'] = 0x0;
        this.keymap['c'] = 0xB;
        this.keymap['v'] = 0xF;


        this.keyState = new boolean[16];
        Arrays.fill(this.keyState, false);
    }

    public void setKeymap(int[] keymap) {
        this.keymap = keymap;
    }

    public boolean isKeyPressed(int key) {
        return this.keyState[key];
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        // Unused for Chip-8 Emulators
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        // Set the pressed key to true in keyState
        int key = keyEvent.getKeyCode();
        this.keyState[key] = true;
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        // Set the pressed key to false in the keyState
        int key = keyEvent.getKeyCode();
        this.keyState[key] = false;
    }
}

