package com.project;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class Sudoku {
    private Board board;
    private ArrayList<Integer> tilesLogic;
    private ArrayList<Boolean> tilesPossibilities;
    private Deque<Board> sudokusToCome;

    public Sudoku(Board board) {
        this.board = board;
        sudokusToCome = new LinkedList<>();
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
        while (true) {
            updateLogic();
            if (isSolvable()) {
                int currentIndex = findSmallest();
                int size = board.getSize();
                int x = currentIndex / size;
                int y = currentIndex - x * size;
                if (tilesLogic.get(currentIndex) == 1) {
                    board.setTileValue(x, y, fetchNumber(x, y));
                } else if (tilesLogic.get(currentIndex) > 1) {
                    addSudokuOntoStack(x, y, currentIndex, size);
                }
            } else {
                if (!sudokusToCome.isEmpty()) {
                    board = sudokusToCome.removeLast();
                } else {
                    return;
                }
            }
        }
    }

    private void addSudokuOntoStack(int x, int y, int currentIndex, int size) {
        int doubleSize = size * size;
        int j = 0;
        for (int i = 1; i < tilesLogic.get(currentIndex); ++i) {
            for (; j < size; ++j) {
                if (Boolean.TRUE.equals(tilesPossibilities.get(x * doubleSize + y * size + j))) {
                    Board tempBoard = this.board;
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
            tilesLogic.set(x * board.getSize() + y, Integer.valueOf(count));
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
            tilesLogic.set(x * board.getSize() + y, Integer.valueOf(count));
        }
    }

    private void boxLogic(int x, int y) {
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

    private int findSmallest() {
        int smallest = board.getSize() + 1;
        int index = -1;
        for (int i = 0; i < board.getSize(); ++i) {
            for (int j = 0; j < board.getSize(); ++j) {
                if (tilesLogic.get(i * board.getSize() + j) != 0
                        && tilesLogic.get(i * board.getSize() + j) < smallest) {
                    smallest = tilesLogic.get(i * board.getSize() + j);
                    index = i * board.getSize() + j;
                }
            }
        }
        return index;
    }

    private int fetchNumber(int x, int y) {
        for (int i = 0; i < board.getSize(); ++i) {
            Boolean condition = tilesPossibilities.get(x * board.getSize() * board.getSize() + y * board.getSize() + i);
            if (Boolean.TRUE.equals(condition)) {
                return i + 1;
            }
        }
        return 0;
    }
}
