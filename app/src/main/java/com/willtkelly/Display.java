package com.willtkelly;

import com.badlogic.gdx.ApplicationAdapter;

public class Display extends ApplicationAdapter {

    private boolean window_open = false;

    @Override
    public void create() {
        // Initialization code
        System.out.println("Display Created!");
        this.window_open = true;
    }

    @Override
    public void render() {
        // Rendering code
    }

    @Override
    public void dispose() {
        System.out.println("Display Closed!");
       this.window_open = false;
    }

    public boolean isWindowOpen() {
        return this.window_open;
    }
}

