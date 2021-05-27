package com.project;

import java.util.ArrayList;

public class Board {
    private ArrayList<Integer> tilesValue;
    private ArrayList<Integer> tilesLogic;

    Board() {
        tilesValue = new ArrayList<>();
        tilesLogic = new ArrayList<>();
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
    public int getTileLogic(int x, int y) {
        return tilesLogic.get(x * tilesValue.size() + y).intValue();
    }
}
