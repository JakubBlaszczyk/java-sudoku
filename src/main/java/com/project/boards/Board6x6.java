package com.project.boards;

import java.util.ArrayList;
import java.util.List;

public class Board6x6 extends BoardFather {

  public Board6x6(List<Integer> tilesValue) {
    super(tilesValue);
  }

  @Override
  public Board copy() {
    return new Board6x6(new ArrayList<>(tilesValue));
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
