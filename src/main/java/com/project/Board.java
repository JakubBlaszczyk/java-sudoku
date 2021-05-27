package com.project;

import java.util.ArrayList;

public class Board {
    private ArrayList<Integer> tilesValue;

    public Board() {
        tilesValue = new ArrayList<>();
    }

    public int getSize() {
        return 0;
    }

    public int getBoxWidth() {
        return 0;
    }

    public int getBoxHeight() {
        return 0;
    }

    public int getTileValue(int x, int y) {
        return tilesValue.get(x * tilesValue.size() + y).intValue();
    }


    public void setTileValue(int x, int y, int value) {
        this.tilesValue.set(x * tilesValue.size() + y, Integer.valueOf(value));
    }

}
