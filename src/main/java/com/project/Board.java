package com.project;

import java.util.List;

public class Board {
    private List<Integer> tilesValue;

    public Board(List<Integer> tilesValue) {
        this.tilesValue = tilesValue;
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
    public int setTileValue(int x, int y, int value) {
        return tilesValue.set(x * tilesValue.size() + y, Integer.valueOf(value));
    }
}
