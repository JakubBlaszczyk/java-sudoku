package com.project;

import java.util.ArrayList;

public class Sudoku {
  private Board board;
  private ArrayList<Integer> tilesLogic;
  private ArrayList<Boolean> tilesPossibilities;
  private ArrayList<Board> sudokusToCome;

  public Sudoku(Board board) {
    this.board = board;
    sudokusToCome = new ArrayList<>();
    tilesLogic = new ArrayList<>(board.getSize() * board.getSize());
    tilesPossibilities = new ArrayList<>(board.getSize() * board.getSize() * board.getSize());
    for (int i = 0; i < tilesPossibilities.size(); ++i) {
      tilesPossibilities.set(i, true);
    }
    for (int i = 0; i < tilesLogic.size(); ++i) {
      tilesLogic.set(i, board.getSize());
    }
  }

  public void solve() {
    int boardSize = board.getSize();
    while (true) {
      updateLogic();
      if (isSolvable()) {
        int currentIndex = findSmallest();

        int x = currentIndex / boardSize;
        int y = currentIndex - x * boardSize;
        if (tilesLogic.get(currentIndex) == 1) {
          board.setTileValue(x, y, fetchNumber(x, y));
        } else if (tilesLogic.get(currentIndex) > 1) {
          int j = 0;
          for (int i = 1; i < tilesLogic.get(currentIndex); ++i) {
            for (; j < boardSize; ++j) {
              if (tilesPossibilities.get(x * boardSize * boardSize + y * boardSize + j)) {
                Board tempBoard = this.board;
                tempBoard.setTileValue(x, y, j + 1);
                sudokusToCome.add(tempBoard);
                ++j;
                break;
              }
            }
          }
          for (; j < boardSize; ++j) {
            if (tilesPossibilities.get(x * boardSize * boardSize + y * boardSize + j)) {
              board.setTileValue(x, y, j + 1);
              break;
            }
          }
        }
      } else {
        if (!sudokusToCome.isEmpty()) {
          board = sudokusToCome.remove(sudokusToCome.size() - 1);
        }
      }
    }
  }

  private boolean isSolvable() {
    for (int i = 0; i < board.getSize(); ++i) {
      for (int j = 0; j < board.getSize(); ++j) {
        if (board.getTileValue(i, j) == 0 && tilesLogic.get(i * board.getSize() + j) == 0) {
          return false;
        }
      }
    }
    return true;
  }

  private void updateLogic() {
    filledUpdate();
    for (int i = 0; i < board.getSize(); ++i) {
      for (int j = 0; j < board.getSize(); ++j) {
        rowLogic(i, j);
        columnLogic(i, j);
        boxLogic(i, j);
      }
    }
  }

  private void filledUpdate() {
    int boardSize = board.getSize();
    for (int i = 0; i < boardSize; ++i) {
      for (int j = 0; j < boardSize; ++j) {
        if (board.getTileValue(i, j) != 0) {
          int posInPossibilities = i * boardSize * boardSize + j * boardSize;
          tilesLogic.set(i * boardSize + j, Integer.valueOf(0));
          for (int k = 0; k < boardSize; ++k) {
            tilesPossibilities.set(posInPossibilities + k, false);
          }
        }
      }
    }
  }

  private void rowLogic(int x, int y) {
    // if it needs refreshing breeze through line
    if (board.getTileValue(x, y) == 0) {
      int posInPossibilities = x * board.getSize() * board.getSize() + y * board.getSize();
      int count = 0;
      for (int i = 0; i < board.getSize(); ++i) {
        if (board.getTileValue(i, y) != 0) {
          tilesPossibilities.set(posInPossibilities + board.getTileValue(i, y) - 1, true);
          ++count;
        }
      }
      tilesLogic.set(x * board.getSize() + y, count);
    }
  }

  private void columnLogic(int x, int y) {
    if (board.getTileValue(x, y) == 0) {
      int posInPossibilities = x * board.getSize() * board.getSize() + y * board.getSize();
      int count = 0;
      for (int i = 0; i < board.getSize(); ++i) {
        if (board.getTileValue(x, i) != 0) {
          tilesPossibilities.set(posInPossibilities + board.getTileValue(x, i) - 1, true);
          ++count;
        }
      }
      tilesLogic.set(x * board.getSize() + y, count);
    }
  }

  private void boxLogic(int x, int y) {
    if (board.getTileValue(x, y) == 0) {
      int tempX = (x / board.getBoxWidth()) * board.getBoxWidth();
      int tempY = (y / board.getBoxHeight()) * board.getBoxHeight();
      int posInPossibilities = x * board.getSize() * board.getSize() + y * board.getSize();
      int count = 0;
      for (int i = tempX; i < tempX + board.getBoxWidth(); ++i) {
        for (int j = tempX; j < tempY + board.getBoxHeight(); ++j) {
          if (board.getTileValue(i, j) == 0) {
            tilesPossibilities.set(posInPossibilities + board.getTileValue(i, j) - 1, true);
            ++count;
          }
        }
      }
      tilesLogic.set(x * board.getSize() + y, count);
    }
  }

  private int findSmallest() {
    int smallest = board.getSize() + 1;
    int index = -1;
    Integer value;
    for (int i = 0; i < board.getSize(); ++i) {
      for (int j = 0; j < board.getSize(); ++j) {
        value = tilesLogic.get(i * board.getSize() + j);
        if (value != 0 && value < smallest) {
          smallest = value;
          index = i * board.getSize() + j;
        }
      }
    }
    return index;
  }

  private int fetchNumber(int x, int y) {
    for (int i = 0; i < board.getSize(); ++i) {
      if (tilesPossibilities.get(x * board.getSize() * board.getSize() + y * board.getSize() + i)) {
        return i + 1;
      }
    }
    return 0;
  }
}
