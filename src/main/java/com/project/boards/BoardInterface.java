package com.project.boards;

import java.util.ArrayList;

public interface BoardInterface {

  public BoardInterface copy();

  public int getSize();

  public int getBoxWidth();

  public int getBoxHeight();

  public int getTileValue(int x, int y);

  public int setTileValue(int x, int y, int value);

  public ArrayList<Integer> getTilesValue();

  public void setTilesValue(ArrayList<Integer> tilesValue);

  public void initializeList(int size, int value);
}
