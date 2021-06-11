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

  // Each exception should have it's own message (GUI stuff)
  public static Board loadBoard(String filePath) throws IOException, InvalidSudokuData, InvalidSudokuSize {
    List<String> nums = loadArray(filePath);
    if (!checkIfStringIsParsable(nums)) {
      log.warn("Sudoku data contains not parsable characters");
      throw new InvalidSudokuData();
    }
    Board board = createBoard(nums);
    board.tilesValue.clear();
    for (int i = 0; i < nums.size(); ++i) {
      board.tilesValue.add(0);
    }
    int arrSize = board.getSize();

    log.debug("Before trans: {}", nums);
    // Transform cols into rows
    for (int i = 0; i < arrSize; ++i) {
      for (int j = 0; j < arrSize; ++j) {
        board.tilesValue.set(i * arrSize + j, Integer.parseInt(nums.get(i + j * arrSize)));
      }
    }
    log.debug("After trans: {}", board.tilesValue);
    return board;
  }

  private static List<String> loadArray(String filePath) throws InvalidSudokuSize, IOException {
    BufferedReader reader;
    reader = new BufferedReader(new FileReader(filePath));
    List<String> nums = new ArrayList<>();
    while (true) {
      String line = reader.readLine();
      if (line == null) {
        break;
      }
      line = line.trim();
      nums.addAll(Arrays.asList(line.split("\\s+")));
    }
    reader.close();

    double testSize = Math.sqrt(nums.size());
    if (!((testSize == Math.floor(testSize)) && !Double.isInfinite(testSize))) {
      log.warn("Sudoku size has to be square value, size = {}", testSize);
      throw new InvalidSudokuSize();
    }
    return nums;
  }

  private static Board createBoard(List<String> list) throws InvalidSudokuSize {
    switch ((int) Math.sqrt(list.size())) {
      case 6:
        return new Board6x6(new ArrayList<>());
      case 8:
        return new Board8x8(new ArrayList<>());
      case 9:
        return new Board9x9(new ArrayList<>());
      case 10:
        return new Board10x10(new ArrayList<>());
      case 12:
        return new Board12x12(new ArrayList<>());
      default:
        log.warn("Sudoku size not within valid range, {}", list.size());
        throw new InvalidSudokuSize();
    }
  }

  // Data have to be colleded by rows
  public static void saveBoard(String filePath, List<Integer> data) throws IOException {

    FileWriter wr = new FileWriter(filePath);
    // Data from GUI so it must be corret
    int arrSize = (int) Math.sqrt(data.size());
    int idx = 0;
    for (int i = 0; i < arrSize; ++i) {
      for (int j = 0; j < arrSize; ++j) {
        wr.append(String.valueOf(data.get(idx++)) + " ");
      }
      wr.append("\n");
    }
    wr.close();
  }

  private static boolean checkIfStringIsParsable(List<String> strNums) {
    try {
      for (String num : strNums) {
        Integer.parseInt(num);
      }
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public void initializeList(int size, int value) {
    for (int i = 0; i < size; ++i) {
      tilesValue.add(value);
    }
  }
}
