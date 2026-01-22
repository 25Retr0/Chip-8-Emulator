package com.willtkelly;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Keyboard {

    private final boolean[] keys = new boolean[16];

    public void update() {
        keys[0x0] = Gdx.input.isKeyPressed(Input.Keys.X);
        keys[0x1] = Gdx.input.isKeyPressed(Input.Keys.NUM_1);
        keys[0x2] = Gdx.input.isKeyPressed(Input.Keys.NUM_2);
        keys[0x3] = Gdx.input.isKeyPressed(Input.Keys.NUM_3);

        keys[0x4] = Gdx.input.isKeyPressed(Input.Keys.Q);
        keys[0x5] = Gdx.input.isKeyPressed(Input.Keys.W);
        keys[0x6] = Gdx.input.isKeyPressed(Input.Keys.E);
        keys[0x7] = Gdx.input.isKeyPressed(Input.Keys.A);

        keys[0x8] = Gdx.input.isKeyPressed(Input.Keys.S);
        keys[0x9] = Gdx.input.isKeyPressed(Input.Keys.D);
        keys[0xA] = Gdx.input.isKeyPressed(Input.Keys.Z);
        keys[0xB] = Gdx.input.isKeyPressed(Input.Keys.C);

        keys[0xC] = Gdx.input.isKeyPressed(Input.Keys.NUM_4);
        keys[0xD] = Gdx.input.isKeyPressed(Input.Keys.R);
        keys[0xE] = Gdx.input.isKeyPressed(Input.Keys.F);
        keys[0xF] = Gdx.input.isKeyPressed(Input.Keys.V);
    }

    public boolean isEscapePressed() {
        return Gdx.input.isKeyPressed(Input.Keys.ESCAPE);
    }

    public int getPressedKey() {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i]) return i;
        }
        return -1;
    }

    public boolean isKeyPressed(int keycode) {
        return keys[keycode & 0xF];
    }
}

