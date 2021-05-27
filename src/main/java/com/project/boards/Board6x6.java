package com.project.boards;

import java.util.List;

import com.project.Board;

public class Board6x6 extends Board {
  
  public Board6x6(List<Integer> tilesValue) {
    super(tilesValue);
  }
  
  @Override
  public int getSize() {
    return 6;
  }

  @Override
  public int getBoxWidth() {
      return 3;
  }

  @Override
  public int getBoxHeight() {
      return 2;
  }
}
