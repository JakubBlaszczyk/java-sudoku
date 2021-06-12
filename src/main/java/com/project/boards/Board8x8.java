package com.project.boards;

import java.util.ArrayList;
import java.util.List;

public class Board8x8 extends Board {
  
  public Board8x8(List<Integer> tilesValue) {
    super(tilesValue);
  }

  @Override
  public BoardInterface copy() {
    return new Board8x8(new ArrayList<>(tilesValue));
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
