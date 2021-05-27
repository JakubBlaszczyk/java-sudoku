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
        while (true) {
            updateLogic();
            if (isSolvable()) {
                int currentIndex = findSmallest();

                int x = currentIndex / board.getSize();
                int y = currentIndex - x * board.getSize();
                if (tilesLogic.get(currentIndex) == 1) {
                    board.setTileValue(x, y, fetchNumber(x, y));
                }
                else if (tilesLogic.get(currentIndex) > 1) {
                    int j = 0;
                    for (int i = 1; i < tilesLogic.get(currentIndex); ++i) {
                        for (; j < board.getSize(); ++j) {
                            if (tilesPossibilities
                                    .get(x * board.getSize() * board.getSize() + y * board.getSize() + j) == true) {
                                Board tempBoard = this.board;
                                tempBoard.setTileValue(x, y, j + 1);
                                sudokusToCome.add(tempBoard);
                                ++j;
                                break;
                            }
                        }
                    }
                    for (; j < board.getSize(); ++j) {
                        if (tilesPossibilities
                                .get(x * board.getSize() * board.getSize() + y * board.getSize() + j) == true) {
                            board.setTileValue(x, y, j + 1);
                            break;
                        }
                    }
                }
            } else {
                // TODO fetch next sudoku to solve
                if (!sudokusToCome.isEmpty()) {
                    board = sudokusToCome.remove(sudokusToCome.size() - 1);
                }
            }
        }
    }

    private boolean isSolvable() {
        for (int i = 0; i < board.getSize(); ++i) {
            for (int j = 0; j < board.getSize(); ++j) {
                if (board.getTileValue(i, j) == 0) {
                    if (tilesLogic.get(i * board.getSize() + j) == 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void updateLogic() {
        FilledUpdate();
        for (int i = 0; i < board.getSize(); ++i) {
            for (int j = 0; j < board.getSize(); ++j) {
                RowLogic(i, j);
                ColumnLogic(i, j);
                BoxLogic(i, j);
            }
        }
    }

    private void FilledUpdate() {
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

    private void RowLogic(int x, int y) {
        // if it needs refreshing breeze through line
        if (board.getTileValue(x, y) == 0) {
            int posInPossibilities = x * board.getSize() * board.getSize() + y * board.getSize();
            for (int i = 0; i < board.getSize(); ++i) {
                if (board.getTileValue(i, y) != 0) {
                    tilesPossibilities.set(posInPossibilities + board.getTileValue(i, y) - 1, true);
                }
            }
        }
    }

    private void ColumnLogic(int x, int y) {
        if (board.getTileValue(x, y) == 0) {
            int posInPossibilities = x * board.getSize() * board.getSize() + y * board.getSize();
            for (int i = 0; i < board.getSize(); ++i) {
                if (board.getTileValue(x, i) != 0) {
                    tilesPossibilities.set(posInPossibilities + board.getTileValue(x, i) - 1, true);
                }
            }
        }
    }

    private void BoxLogic(int x, int y) {
        if (board.getTileValue(x, y) == 0) {
            int tempX, tempY;
            int posInPossibilities = x * board.getSize() * board.getSize() + y * board.getSize();
            tempX = (x / board.getBoxWidth()) * board.getBoxWidth();
            tempY = (y / board.getBoxHeight()) * board.getBoxHeight();
            for (int i = tempX; i < tempX + board.getBoxWidth(); ++i) {
                for (int j = tempX; j < tempY + board.getBoxHeight(); ++j) {
                    if (board.getTileValue(i, j) == 0) {
                        tilesPossibilities.set(posInPossibilities + board.getTileValue(i, j) - 1, true);
                    }
                }
            }
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
            if (tilesPossibilities.get(x * board.getSize() * board.getSize() + y * board.getSize() + i)) {
                return i + 1;
            }
        }
        return 0;
    }
}
