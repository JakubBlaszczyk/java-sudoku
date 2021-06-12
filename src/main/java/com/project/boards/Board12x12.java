package com.project.boards;

import java.util.ArrayList;
import java.util.List;

import com.project.Board;

public class Board12x12 implements Board {

  private List<Integer> tilesValue;

  public Board12x12(List<Integer> tilesValue) {
    this.tilesValue = tilesValue;
  }

  @Override
  public Board copy() {
    return new Board12x12(new ArrayList<>(tilesValue));
  }

  @Override
  public int getSize() {
    return 12;
  }

  @Override
  public int getBoxWidth() {
      return 4;
  }

  @Override
  public int getBoxHeight() {
      return 3;
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
