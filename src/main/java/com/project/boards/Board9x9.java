package com.project.boards;

import java.util.ArrayList;

public class Board9x9 extends Board {

  public Board9x9() {
  }

  public Board9x9(ArrayList<Integer> tilesValue) {
    super(tilesValue);
  }

  @Override
  public BoardInterface copy() {
    return new Board9x9(new ArrayList<>(tilesValue));
  }

  @Override
  public int getSize() {
    return 9;
  }

  @Override
  public int getBoxWidth() {
    return 3;
  }

  @Override
  public int getBoxHeight() {
    return 3;
  }
}
