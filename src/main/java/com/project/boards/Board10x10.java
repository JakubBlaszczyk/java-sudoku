package com.project.boards;

import java.util.List;

import com.project.Board;

public class Board10x10 extends Board {
  
  public Board10x10(List<Integer> tilesValue) {
    super(tilesValue);
  }
  
  @Override
  public int getSize() {
    return 10;
  }

  @Override
  public int getBoxWidth() {
      return 5;
  }

  @Override
  public int getBoxHeight() {
      return 2;
  }
}