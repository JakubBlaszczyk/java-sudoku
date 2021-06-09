package com.project;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

import com.project.exceptions.SudokuAlreadySolved;
import com.project.exceptions.SudokuUnsolvable;

public class Sudoku {

  private Sudoku() {
  }

  public static void solve(Board board) throws SudokuAlreadySolved {
    Deque<Board> sudokusToCome = new LinkedList<>();
    if (fillOneBoard(board, sudokusToCome)) {
      throw new SudokuAlreadySolved();
    }
  }

  private static boolean fillOneBoard(Board board, Deque<Board> sudokusToCome) {
    ArrayList<Integer> tilesLogic;
    ArrayList<Boolean> tilesPossibilities;
    int size = board.getSize();
    tilesLogic = new ArrayList<>(size * size);
    tilesPossibilities = new ArrayList<>(size * size * size);
    for (int i = 0; i < tilesPossibilities.size(); ++i) {
      tilesPossibilities.set(i, true);
    }
    for (int i = 0; i < tilesLogic.size(); ++i) {
      tilesLogic.set(i, size);
    }
    while (true) {
      updateLogic(board, tilesLogic, tilesPossibilities);
      if (isSolvable(board, tilesLogic)) {
        solveTick(board, tilesLogic, tilesPossibilities, sudokusToCome);
      } else {
        if (!sudokusToCome.isEmpty()) {
          if (isSolved(board)) {
            return true;
          }
          board = sudokusToCome.removeLast();
        } else {
          return false;
        }
      }
    }
  }

  private static Hint solveTick(Board board, ArrayList<Integer> tilesLogic, ArrayList<Boolean> tilesPossibilities,
      Deque<Board> sudokusToCome) {
    int currentIndex = findSmallest(board, tilesLogic);
    int x = currentIndex / board.getSize();
    int y = currentIndex - x * board.getSize();
    if (tilesLogic.get(currentIndex) == 1) {
      Hint temp = new Hint(x, y, fetchNumber(board, tilesPossibilities, x, y));
      board.setTileValue(x, y, temp.getValue());
      return temp;
    } else if (tilesLogic.get(currentIndex) > 1) {
      addSudokuOntoStack(board, tilesLogic, tilesPossibilities, sudokusToCome, x, y, currentIndex);
      return null;
    }
    throw new InvalidParameterException("Shouldn't be here");
  }

  private static void addSudokuOntoStack(Board board, ArrayList<Integer> tilesLogic,
      ArrayList<Boolean> tilesPossibilities, Deque<Board> sudokusToCome, int x, int y, int currentIndex) {
    int size = board.getSize();
    int doubleSize = size * size;
    int j = 0;
    for (int i = 1; i < tilesLogic.get(currentIndex); ++i) {
      for (; j < size; ++j) {
        if (Boolean.TRUE.equals(tilesPossibilities.get(x * doubleSize + y * size + j))) {
          Board tempBoard = board;
          tempBoard.setTileValue(x, y, j + 1);
          sudokusToCome.add(tempBoard);
          ++j;
          break;
        }
      }
    }
    for (; j < size; ++j) {
      if (Boolean.TRUE.equals(tilesPossibilities.get(x * doubleSize + y * size + j))) {
        board.setTileValue(x, y, j + 1);
        break;
      }
    }
  }

  private static boolean isSolved(Board board) {
    for (int i = 0; i < board.getSize(); ++i) {
      for (int j = 0; j < board.getSize(); ++j) {
        if (board.getTileValue(i, j) == 0) {
          return false;
        }
      }
    }
    return true;
  }

  private static boolean isSolvable(Board board, ArrayList<Integer> tilesLogic) {
    for (int i = 0; i < board.getSize(); ++i) {
      for (int j = 0; j < board.getSize(); ++j) {
        if (board.getTileValue(i, j) == 0 && tilesLogic.get(i * board.getSize() + j) == 0) {
          return false;
        }
      }
    }
    return true;
  }

  private static void updateLogic(Board board, ArrayList<Integer> tilesLogic, ArrayList<Boolean> tilesPossibilities) {
    filledUpdate(board, tilesLogic, tilesPossibilities);
    for (int i = 0; i < board.getSize(); ++i) {
      for (int j = 0; j < board.getSize(); ++j) {
        rowLogic(board, tilesLogic, tilesPossibilities, i, j);
        columnLogic(board, tilesLogic, tilesPossibilities, i, j);
        boxLogic(board, tilesLogic, tilesPossibilities, i, j);
      }
    }
  }

  private static void filledUpdate(Board board, ArrayList<Integer> tilesLogic, ArrayList<Boolean> tilesPossibilities) {
    for (int i = 0; i < board.getSize(); ++i) {
      for (int j = 0; j < board.getSize(); ++j) {
        if (board.getTileValue(i, j) != 0) {
          int posInPossibilities = i * board.getSize() * board.getSize() + j * board.getSize();
          tilesLogic.set(i * board.getSize() + j, Integer.valueOf(0));
          for (int k = 0; k < board.getSize(); ++k) {
            tilesPossibilities.set(posInPossibilities + k, false);
          }
        }
      }
    }
  }

  private static void rowLogic(Board board, ArrayList<Integer> tilesLogic, ArrayList<Boolean> tilesPossibilities, int x,
      int y) {
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
      tilesLogic.set(x * board.getSize() + y, Integer.valueOf(count));
    }
  }

  private static void columnLogic(Board board, ArrayList<Integer> tilesLogic, ArrayList<Boolean> tilesPossibilities,
      int x, int y) {
    if (board.getTileValue(x, y) == 0) {
      int posInPossibilities = x * board.getSize() * board.getSize() + y * board.getSize();
      int count = 0;
      for (int i = 0; i < board.getSize(); ++i) {
        if (board.getTileValue(x, i) != 0) {
          tilesPossibilities.set(posInPossibilities + board.getTileValue(x, i) - 1, true);
          ++count;
        }
      }
      tilesLogic.set(x * board.getSize() + y, Integer.valueOf(count));
    }
  }

  private static void boxLogic(Board board, ArrayList<Integer> tilesLogic, ArrayList<Boolean> tilesPossibilities, int x,
      int y) {
    if (board.getTileValue(x, y) == 0) {
      int tempX;
      int tempY;
      int posInPossibilities = x * board.getSize() * board.getSize() + y * board.getSize();
      int count = 0;
      tempX = (x / board.getBoxWidth()) * board.getBoxWidth();
      tempY = (y / board.getBoxHeight()) * board.getBoxHeight();
      for (int i = tempX; i < tempX + board.getBoxWidth(); ++i) {
        for (int j = tempX; j < tempY + board.getBoxHeight(); ++j) {
          if (board.getTileValue(i, j) == 0) {
            tilesPossibilities.set(posInPossibilities + board.getTileValue(i, j) - 1, true);
            ++count;
          }
        }
      }
      tilesLogic.set(x * board.getSize() + y, Integer.valueOf(count));
    }
  }

  private static int findSmallest(Board board, ArrayList<Integer> tilesLogic) {
    int smallest = board.getSize() + 1;
    int index = -1;
    for (int i = 0; i < board.getSize(); ++i) {
      for (int j = 0; j < board.getSize(); ++j) {
        if (tilesLogic.get(i * board.getSize() + j) != 0 && tilesLogic.get(i * board.getSize() + j) < smallest) {
          smallest = tilesLogic.get(i * board.getSize() + j);
          index = i * board.getSize() + j;
        }
      }
    }
    return index;
  }

  private static int fetchNumber(Board board, ArrayList<Boolean> tilesPossibilities, int x, int y) {
    for (int i = 0; i < board.getSize(); ++i) {
      Boolean condition = tilesPossibilities.get(x * board.getSize() * board.getSize() + y * board.getSize() + i);
      if (Boolean.TRUE.equals(condition)) {
        return i + 1;
      }
    }
    return 0;
  }

  public static Hint hint(Board in) throws SudokuAlreadySolved, SudokuUnsolvable {
    Deque<Board> sudokusToCome = new LinkedList<>();
    ArrayList<Integer> tilesLogic;
    ArrayList<Boolean> tilesPossibilities;
    Board board = in;
    int size = board.getSize();
    tilesLogic = new ArrayList<>(size * size);
    tilesPossibilities = new ArrayList<>(size * size * size);
    for (int i = 0; i < tilesPossibilities.size(); ++i) {
      tilesPossibilities.set(i, true);
    }
    for (int i = 0; i < tilesLogic.size(); ++i) {
      tilesLogic.set(i, size);
    }
    while (true) {
      updateLogic(board, tilesLogic, tilesPossibilities);
      if (isSolvable(board, tilesLogic)) {
        return solveTick(board, tilesLogic, tilesPossibilities, sudokusToCome);
      } else {
        if (!sudokusToCome.isEmpty()) {
          if (isSolved(board)) {
            throw new SudokuAlreadySolved();
          }
          board = sudokusToCome.removeLast();
        } else {
          throw new SudokuUnsolvable();
        }
      }
    }
  }

  // this method checks if this Board has an solution
  // if even one solution exists, will return true
  public static Boolean check(Board in) {
    Deque<Board> sudokusToCome = new LinkedList<>();
    Deque<Board> correctSudokus = new LinkedList<>();
    Board board = in;
    if (!fillOneBoard(board, sudokusToCome)) {
      throw new NoSuchFieldError();
    }
    correctSudokus.push(board);
    Board temp = sudokusToCome.removeLast();
    while (fillOneBoard(temp, sudokusToCome)) {
      correctSudokus.push(temp);
      temp = sudokusToCome.removeLast();
    }
    for (Board one : correctSudokus) {
      if (one == in) {
        return true;
      }
    }
    return false;
  }
}
