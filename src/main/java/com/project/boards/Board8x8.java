package com.project.boards;

import java.util.List;

import com.project.Board;

public class Board8x8 extends Board {
  
  public Board8x8(List<Integer> tilesValue) {
    super(tilesValue);
  }
  
  @Override
  public int getSize() {
    return 8;
  }

  @Override
  public int getBoxWidth() {
      return 4;
  }

  @Override
  public int getBoxHeight() {
      return 2;
  }
}
