package com.willtkelly;

import com.badlogic.gdx.ApplicationAdapter;

public class Display extends ApplicationAdapter {

    private Chip8 chip;
    private int width;
    private int height;
    private int scale;

    public void setChip(Chip8 chip) {
        this.chip = chip;
    }

    public Display(int width, int height, int scale) {
        this.width = width;
        this.height = height;
        this.scale = scale;
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
        byte[][] screen = chip.getScreen();
        draw(screen);
    }

    private void draw(byte[][] screen) {

    }

    @Override
    public void dispose() {
        System.out.println("Display Closed!");
    }
}

