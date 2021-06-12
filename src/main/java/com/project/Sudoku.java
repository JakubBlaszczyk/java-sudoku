package com.project;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import com.project.boards.BoardInterface;
import com.project.exceptions.SudokuAlreadySolved;
import com.project.exceptions.SudokuUnsolvable;

public class Sudoku {

  private Sudoku() {
  }

  // **************************************
  // Public methods
  // **************************************
  /**
   * Funtion solving given board. It looks for one solution possible, discarding
   * others.
   * 
   * @param BoardInterface
   * @throws SudokuAlreadySolved
   * @throws SudokuUnsolvable
   * @author Jakub
   */
  public static void solve(BoardInterface board) throws SudokuAlreadySolved, SudokuUnsolvable {
    Deque<BoardInterface> sudokusToCome = new LinkedList<>();
    if (isSolved(board)) {
      throw new SudokuAlreadySolved();
    }
    if (!fillOneBoard(board, sudokusToCome)) {
      throw new SudokuUnsolvable();
    }
  }

  /**
   * Checks possible outcomes of original with current state of in. Returns all
   * deviances.
   * 
   * @param in       current state of original Board
   * @param original original Board that will be compared to
   * @return List of differences between solved sudoku and "in".
   * @throws SudokuUnsolvable
   * @throws SudokuAlreadySolved
   * @author Jakub
   */
  public static List<Hint> check(BoardInterface in, BoardInterface original)
      throws SudokuUnsolvable, SudokuAlreadySolved {
    Deque<BoardInterface> sudokusToCome = new LinkedList<>();
    Deque<BoardInterface> correctSudokus = new LinkedList<>();
    BoardInterface board = original.copy();
    if (isSolved(board)) {
      throw new SudokuAlreadySolved();
    }
    if (!fillOneBoard(board, sudokusToCome)) {
      throw new SudokuUnsolvable();
    }
    correctSudokus.push(board);
    if (sudokusToCome.isEmpty()) {
      throw new SudokuAlreadySolved();
    }
    BoardInterface temp = sudokusToCome.removeLast();
    while (fillOneBoard(temp, sudokusToCome) && correctSudokus.size() < 30) {
      correctSudokus.push(temp);
      temp = sudokusToCome.removeLast();
    }
    ArrayList<Hint> min = new ArrayList<>();
    for (int i = 0; i < in.getSize() * in.getSize(); ++i) {
      min.add(new Hint(0, 0, 0));
    }
    for (BoardInterface one : correctSudokus) {
      ArrayList<Hint> data = compareSudokus(in, one);
      if (data.size() <= min.size()) {
        min = data;
      }
    }
    return min;
  }

  /**
   * Returns location of next cell to be modified to solve sudoku.
   * 
   * @param BoardInterface
   * @return Hint that is coordinates and value for single cell to be changed
   * @throws SudokuAlreadySolved
   * @throws SudokuUnsolvable
   * @author Jakub
   */
  public static Hint hint(BoardInterface in) throws SudokuAlreadySolved, SudokuUnsolvable {
    Deque<BoardInterface> sudokusToCome = new LinkedList<>();
    ArrayList<Integer> tilesLogic;
    ArrayList<Boolean> tilesPossibilities;
    BoardInterface board = in.copy();
    int size = board.getSize();
    tilesLogic = new ArrayList<>(size * size);
    tilesPossibilities = new ArrayList<>(size * size * size);
    initializeArrayList(tilesPossibilities, size * size * size, true);
    initializeArrayList(tilesLogic, size * size, size);
    while (true) {
      if (updateLogic(board, tilesLogic, tilesPossibilities) && isSolvableInternal(board, tilesLogic)) {
        Hint temp = solveTick(board, tilesLogic, tilesPossibilities, sudokusToCome);
        if (temp == null) {
          return findHintForMultiplePossibilities(board, sudokusToCome);
        }
        return temp;
      } else {
        if (isSolved(board)) {
          throw new SudokuAlreadySolved();
        } else if (!sudokusToCome.isEmpty()) {
          board = sudokusToCome.removeLast();
        } else {
          throw new SudokuUnsolvable();
        }
      }
    }
  }

  /**
   * Performs solve of one board without changing contents of given board.
   * 
   * @param BoardInterface
   * @return true if is solvable and false if not
   * @author Jakub
   */
  public static Boolean isSolvable(BoardInterface board) {
    Deque<BoardInterface> sudokusToCome = new LinkedList<>();
    BoardInterface temp = board.copy();
    return fillOneBoard(temp, sudokusToCome);
  }

  // **************************************
  // Private methods
  // **************************************
  /**
   * Internal functions to fill given board with auxiliary queue. Proceeds as long
   * as there is any sudoku in queue or one sudoku is solved.
   * 
   * @param board         current work board
   * @param sudokusToCome internal queue for sudokus to be fetched
   * @return true if sudoku is solved, false if it is not solvable
   * @author Jakub
   */
  private static boolean fillOneBoard(BoardInterface board, Deque<BoardInterface> sudokusToCome) {
    ArrayList<Integer> tilesLogic;
    ArrayList<Boolean> tilesPossibilities;
    int size = board.getSize();
    tilesLogic = new ArrayList<>(size * size);
    tilesPossibilities = new ArrayList<>(size * size * size);
    initializeArrayList(tilesPossibilities, size * size * size, true);
    initializeArrayList(tilesLogic, size * size, size);
    while (true) {
      if (updateLogic(board, tilesLogic, tilesPossibilities) && isSolvableInternal(board, tilesLogic)) {
        solveTick(board, tilesLogic, tilesPossibilities, sudokusToCome);
      } else {
        if (isSolved(board)) {
          return true;
        } else if (!sudokusToCome.isEmpty()) {
          board = sudokusToCome.removeLast();
          for (int i = 0; i < size * size * size; ++i) {
            tilesPossibilities.set(i, true);
          }
        } else {
          return false;
        }
      }
    }
  }

  /**
   * Function for initializing list with value. Integer variant.
   * 
   * @param list
   * @param size
   * @param value
   * @author Jakub
   */
  private static void initializeArrayList(ArrayList<Integer> list, int size, Integer value) {
    for (int i = 0; i < size; ++i) {
      list.add(value);
    }
  }

  /**
   * Function for initializing list with value. Boolean variant.
   * 
   * @param list
   * @param size
   * @param value
   * @author Jakub
   */
  private static void initializeArrayList(ArrayList<Boolean> list, int size, Boolean value) {
    for (int i = 0; i < size; ++i) {
      list.add(value);
    }
  }

  /**
   * Function that performs one logic cycle.
   * 
   * @param board
   * @param tilesLogic
   * @param tilesPossibilities
   * @param sudokusToCome
   * @return Hint value after one tick
   * @author Jakub
   */
  private static Hint solveTick(BoardInterface board, ArrayList<Integer> tilesLogic,
      ArrayList<Boolean> tilesPossibilities, Deque<BoardInterface> sudokusToCome) {
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

  /**
   * Adds sudoku onto sudokusToCome.
   * 
   * @param board
   * @param tilesLogic
   * @param tilesPossibilities
   * @param sudokusToCome
   * @param x
   * @param y
   * @param currentIndex
   * @author Jakub
   */
  private static void addSudokuOntoStack(BoardInterface board, ArrayList<Integer> tilesLogic,
      ArrayList<Boolean> tilesPossibilities, Deque<BoardInterface> sudokusToCome, int x, int y, int currentIndex) {
    int size = board.getSize();
    int doubleSize = size * size;
    int j = 0;
    for (int i = 1; i < tilesLogic.get(currentIndex); ++i) {
      for (; j < size; ++j) {
        if (Boolean.TRUE.equals(tilesPossibilities.get(x * doubleSize + y * size + j))) {
          BoardInterface tempBoard = board.copy();
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

  /**
   * Checks if sudoku is solved.
   * 
   * @param board
   * @return true if is solved
   * @author Jakub
   */
  private static boolean isSolved(BoardInterface board) {
    for (int i = 0; i < board.getSize(); ++i) {
      for (int j = 0; j < board.getSize(); ++j) {
        if (board.getTileValue(i, j) == 0) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Checks solvability.
   * 
   * @param board
   * @param tilesLogic
   * @return true if sudoku is solvable
   * @author Jakub
   */
  private static boolean isSolvableInternal(BoardInterface board, ArrayList<Integer> tilesLogic) {
    boolean anyValue = false;
    for (int i = 0; i < board.getSize(); ++i) {
      for (int j = 0; j < board.getSize(); ++j) {
        boolean temp = board.getTileValue(i, j) == 0;
        if (temp && tilesLogic.get(i * board.getSize() + j) == 0) {
          return false;
        } else if (temp) {
          anyValue = true;
        }
      }
    }
    return anyValue;
  }

  /**
   * Function resposible for all the logic updates made on tilesLogic and
   * tilesPossibilties.
   * 
   * @param board
   * @param tilesLogic
   * @param tilesPossibilities
   * @return false if sudoku is unsolvable, true if everything is good
   * @author Jakub
   */
  private static boolean updateLogic(BoardInterface board, ArrayList<Integer> tilesLogic,
      ArrayList<Boolean> tilesPossibilities) {
    filledUpdate(board, tilesLogic, tilesPossibilities);
    for (int i = 0; i < board.getSize(); ++i) {
      for (int j = 0; j < board.getSize(); ++j) {
        try {
          rowLogic(board, tilesPossibilities, i, j);
          columnLogic(board, tilesPossibilities, i, j);
          boxLogic(board, tilesPossibilities, i, j);
        } catch (SudokuUnsolvable s) {
          return false;
        }
        updateTilesLogic(tilesLogic, tilesPossibilities, i, j, board.getSize());
      }
    }
    return true;
  }

  /**
   * Updates logic of fields that are filled.
   * 
   * @param board
   * @param tilesLogic
   * @param tilesPossibilities
   * @author Jakub
   */
  private static void filledUpdate(BoardInterface board, ArrayList<Integer> tilesLogic,
      ArrayList<Boolean> tilesPossibilities) {
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

  /**
   * Updates logic in row relative to the specified coordinates (x, y).
   * 
   * @param board
   * @param tilesPossibilities
   * @param x
   * @param y
   * @throws SudokuUnsolvable
   * @author Jakub
   */
  private static void rowLogic(BoardInterface board, ArrayList<Boolean> tilesPossibilities, int x, int y)
      throws SudokuUnsolvable {
    // if it needs refreshing breeze through line
    if (board.getTileValue(x, y) == 0) {
      ArrayList<Boolean> valid = new ArrayList<>();
      for (int i = 0; i < board.getSize(); ++i) {
        valid.add(true);
      }
      int posInPossibilities = x * board.getSize() * board.getSize() + y * board.getSize();
      for (int i = 0; i < board.getSize(); ++i) {
        int boardValue = board.getTileValue(i, y);
        updateInsideLogic(valid, tilesPossibilities, boardValue, posInPossibilities);
      }
    }
  }

  /**
   * Updates logic in column relative to the specified coordinates (x, y).
   * 
   * @param board
   * @param tilesPossibilities
   * @param x
   * @param y
   * @throws SudokuUnsolvable
   * @author Jakub
   */
  private static void columnLogic(BoardInterface board, ArrayList<Boolean> tilesPossibilities, int x, int y)
      throws SudokuUnsolvable {
    if (board.getTileValue(x, y) == 0) {
      ArrayList<Boolean> valid = new ArrayList<>();
      for (int i = 0; i < board.getSize(); ++i) {
        valid.add(true);
      }
      int posInPossibilities = x * board.getSize() * board.getSize() + y * board.getSize();
      for (int i = 0; i < board.getSize(); ++i) {
        int boardValue = board.getTileValue(x, i);
        updateInsideLogic(valid, tilesPossibilities, boardValue, posInPossibilities);
      }
    }
  }

  /**
   * Updates logic in box, that is specified in Board with getBoxWidth and
   * getBoxHeight, relative to the specified coordinates (x, y).
   * 
   * @param board
   * @param tilesPossibilities
   * @param x
   * @param y
   * @throws SudokuUnsolvable
   * @author Jakub
   */
  private static void boxLogic(BoardInterface board, ArrayList<Boolean> tilesPossibilities, int x, int y)
      throws SudokuUnsolvable {
    if (board.getTileValue(x, y) == 0) {
      ArrayList<Boolean> valid = new ArrayList<>();
      for (int i = 0; i < board.getSize(); ++i) {
        valid.add(true);
      }
      int tempX;
      int tempY;
      int posInPossibilities = x * board.getSize() * board.getSize() + y * board.getSize();
      tempX = (x / board.getBoxWidth()) * board.getBoxWidth();
      tempY = (y / board.getBoxHeight()) * board.getBoxHeight();
      for (int i = tempX; i < tempX + board.getBoxWidth(); ++i) {
        for (int j = tempY; j < tempY + board.getBoxHeight(); ++j) {
          int boardValue = board.getTileValue(i, j);
          updateInsideLogic(valid, tilesPossibilities, boardValue, posInPossibilities);
        }
      }
    }
  }

  /**
   * Inside function to all Logic functions. Updates tilesPossibilites with
   * boardValue at posInPossibilities.
   * 
   * @param validation
   * @param tilesPossibilities
   * @param boardValue
   * @param posInPossibilities
   * @throws SudokuUnsolvable
   * @author Jakub
   */
  private static void updateInsideLogic(ArrayList<Boolean> validation, ArrayList<Boolean> tilesPossibilities,
      int boardValue, int posInPossibilities) throws SudokuUnsolvable {
    if (boardValue != 0) {
      tilesPossibilities.set(posInPossibilities + boardValue - 1, false);
      if (Boolean.FALSE.equals(validation.get(boardValue - 1))) {
        throw new SudokuUnsolvable();
      }
      validation.set(boardValue - 1, false);
    }
  }

  /**
   * Logic method that updates tilesLogic to current status of tilesPossibilities.
   * 
   * @param tilesLogic
   * @param tilesPossibilities
   * @param x
   * @param y
   * @param size
   * @author Jakub
   */
  private static void updateTilesLogic(ArrayList<Integer> tilesLogic, ArrayList<Boolean> tilesPossibilities, int x,
      int y, int size) {
    int count = 0;
    int posInPossibilities = x * size * size + y * size;
    for (int i = 0; i < size; ++i) {
      if (Boolean.TRUE.equals(tilesPossibilities.get(posInPossibilities + i))) {
        ++count;
      }
    }
    tilesLogic.set(x * size + y, Integer.valueOf(count));
  }

  /**
   * Finds index with smallest amount of possibilities in whole board.
   * 
   * @param board
   * @param tilesLogic
   * @return index of position with smallest number
   * @author Jakub
   */
  private static int findSmallest(BoardInterface board, ArrayList<Integer> tilesLogic) {
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

  /**
   * Fetches first number under smallest coordinates (x, y). Operates at
   * tilesPossibilities.
   * 
   * @param board
   * @param tilesPossibilities
   * @param x
   * @param y
   * @return number to be replaced with already incremented by 1.
   * @author Jakub
   */
  private static int fetchNumber(BoardInterface board, ArrayList<Boolean> tilesPossibilities, int x, int y) {
    for (int i = 0; i < board.getSize(); ++i) {
      Boolean condition = tilesPossibilities.get(x * board.getSize() * board.getSize() + y * board.getSize() + i);
      if (Boolean.TRUE.equals(condition)) {
        return i + 1;
      }
    }
    return 0;
  }

  /**
   * Returns hint to the first move from all the tables.
   * 
   * @param board
   * @param sudokusToCome
   * @return hint to be returned in "hint" function
   * @throws SudokuUnsolvable
   * @author Jakub
   */
  private static Hint findHintForMultiplePossibilities(BoardInterface board, Deque<BoardInterface> sudokusToCome)
      throws SudokuUnsolvable {
    for (int i = 0; i < sudokusToCome.size();) {
      BoardInterface toGive = sudokusToCome.removeFirst();
      ArrayList<Hint> temp2 = compareSudokus(board, toGive);
      if (fillOneBoard(toGive, sudokusToCome)) {
        return temp2.get(0);
      } else {
        throw new SudokuUnsolvable();
      }
    }
    throw new InvalidParameterException("Unknown error");
  }

  /**
   * Compares sudokus. Any differences are saved in ArrayList. Does not fill empty
   * fields.
   * 
   * @param main
   * @param solved
   * @return array of hints pointing to fields that were already filled.
   * @author Jakub
   */
  private static ArrayList<Hint> compareSudokus(BoardInterface main, BoardInterface solved) {
    ArrayList<Hint> returnValue = new ArrayList<>();
    for (int i = 0; i < main.getSize() * main.getSize(); ++i) {
      int x = i / main.getSize();
      int y = i % main.getSize();
      if (main.getTileValue(x, y) != 0 && main.getTileValue(x, y) != solved.getTileValue(x, y)) {
        returnValue.add(new Hint(x, y, solved.getTileValue(x, y)));
      }
    }
    return returnValue;
  }

}
