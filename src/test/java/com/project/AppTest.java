package com.project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;

import com.project.boards.Board6x6;
import com.project.exceptions.SudokuAlreadySolved;
import com.project.exceptions.SudokuUnsolvable;

class AppTest {
  @Test
  void testHint() throws SudokuAlreadySolved, SudokuUnsolvable {
    assertThrows(SudokuAlreadySolved.class, () -> {
      ArrayList<Integer> vals = new ArrayList<>(Arrays.asList(4, 5, 1, 2, 3, 6, 2, 6, 5, 3, 1, 4, 3, 1, 4, 6, 2, 5, 5,
          3, 2, 4, 6, 1, 1, 2, 6, 5, 4, 3, 6, 4, 3, 1, 5, 2));
      Board board = new Board6x6(vals);
      Sudoku.hint(board);
    });
    ArrayList<Integer> vals = new ArrayList<>(Arrays.asList(0, 5, 1, 2, 3, 6, 2, 6, 5, 3, 1, 4, 3, 1, 4, 6, 2, 5, 5, 3,
        2, 4, 6, 1, 1, 2, 6, 5, 4, 3, 6, 4, 3, 1, 5, 2));
    Board board = new Board6x6(vals);
    Hint hint = Sudoku.hint(board);
    Hint corretHint = new Hint(0, 0, 4);
    assertEquals(corretHint, hint);
  }
}
