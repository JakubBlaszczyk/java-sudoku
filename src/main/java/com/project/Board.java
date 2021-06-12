package com.project;

import java.util.List;

public interface Board {

  public Board copy();

  public int getSize();

  public int getBoxWidth();

  public int getBoxHeight();

  public int getTileValue(int x, int y);

  public int setTileValue(int x, int y, int value);

  public List<Integer> getTilesValue();

  public void setTilesValue(List<Integer> tilesValue);

  public void initializeList(int size, int value);
}
