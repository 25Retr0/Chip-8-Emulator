package com.willtkelly;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.lwjgl.opengl.GL20;

public class Display extends ApplicationAdapter {

    private Chip8 chip;
    private int rows = 32;
    private int pixles_per_bytes = 8;
    private int scale = 10;

    ShapeRenderer shapeRenderer;

    public void setChip(Chip8 chip) {
        this.chip = chip;
    }

    @Override
    public void create() {
        // Initialization code
        this.shapeRenderer = new ShapeRenderer();
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Drawing commands here
        shapeRenderer.setColor(1, 1, 1, 1); // White colour

        for (int row = 0; row < this.rows; row++) {
            for (int xByte = 0; xByte < 8; xByte++) {
                int b = screen[row][xByte] & 0xFF;

                for (int bit = 7; bit >=0; bit--) {
                    if(((b >> bit) & 1) == 1) {
                        int x = (xByte * 8 + (7 - bit)) * this.scale;
                        int y = row * scale;
                        shapeRenderer.rect(x, y, this.scale, this.scale);
                    }
                }
            }
        }

        shapeRenderer.end();

    }

    @Override
    public void dispose() {
        System.out.println("Display Closed!");
    }
}

