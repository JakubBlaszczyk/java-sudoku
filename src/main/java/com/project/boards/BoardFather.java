package com.project.boards;

import java.util.ArrayList;
import java.util.List;

public class BoardFather implements Board {

    protected List<Integer> tilesValue;

    public BoardFather(List<Integer> tilesValue) {
        this.tilesValue = tilesValue;
    }

    public Board copy() {
        return new BoardFather(new ArrayList<>(tilesValue));
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
        return tilesValue.get(x * getSize() + y).intValue();
    }

    public int setTileValue(int x, int y, int value) {
        return tilesValue.set(x * getSize() + y, Integer.valueOf(value));
    }

    public List<Integer> getTilesValue() {
        return tilesValue;
    }

    public void setTilesValue(List<Integer> tilesValue) {
        this.tilesValue = tilesValue;
    }

    public void initializeList(int size, int value) {
        for (int i = 0; i < size; ++i) {
            tilesValue.add(value);
        }
    }
}
