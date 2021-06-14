package com.project.boards;

import java.util.ArrayList;
import java.util.List;

public class Board12x12 extends Board {

  public Board12x12() {

  }

  public Board12x12(List<Integer> tilesValue) {
    super(tilesValue);
  }

  @Override
  public BoardInterface copy() {
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
}
