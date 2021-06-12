package com.project;

import com.project.boards.Board10x10;
import com.project.boards.Board12x12;
import com.project.boards.Board6x6;
import com.project.boards.Board8x8;
import com.project.boards.Board9x9;
import com.project.exceptions.InvalidSudokuData;
import com.project.exceptions.InvalidSudokuSize;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Board {
  private static Logger log = LoggerFactory.getLogger(Board.class);

  protected List<Integer> tilesValue;

  public Board(List<Integer> tilesValue) {
    this.tilesValue = tilesValue;
  }

  public Board copy() {
    return new Board(new ArrayList<>(tilesValue));
  }

  public int getSize() {
    return 0;
  }

  public int getBoxWidth() {
    return 0;
  }

  public int getBoxHeight() {
    return 0;
  }

  public int getTileValue(int x, int y) {
    return tilesValue.get(x * getSize() + y).intValue();
  }

  public int setTileValue(int x, int y, int value) {
    return tilesValue.set(x * getSize() + y, Integer.valueOf(value));
  }

  public List<Integer> getTilesValue() {
    return tilesValue;
  }

  public void setTilesValue(List<Integer> tilesValue) {
    this.tilesValue = tilesValue;
  }

  public void initializeList(int size, int value) {
    for (int i = 0; i < size; ++i) {
      tilesValue.add(value);
    }
  }
}
