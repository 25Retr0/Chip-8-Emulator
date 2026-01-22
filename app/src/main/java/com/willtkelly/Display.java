package com.willtkelly;

import com.badlogic.gdx.ApplicationAdapter;

public class Display extends ApplicationAdapter {

    private Chip8 chip;

    public void setChip(Chip8 chip) {
        this.chip = chip;
    }

    @Override
    public void create() {
        // Initialization code
        System.out.println("Display Created!");
    }

    @Override
    public void render() {
        // Rendering code
        chip.step();
    }

    @Override
    public void dispose() {
        System.out.println("Display Closed!");
    }
}

