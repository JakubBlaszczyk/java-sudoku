package com.project.boards;

import java.util.List;

import com.project.Board;

public class Board9x9 extends Board {
  
  public Board9x9(List<Integer> tilesValue) {
    super(tilesValue);
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
