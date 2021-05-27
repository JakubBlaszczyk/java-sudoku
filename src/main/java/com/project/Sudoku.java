package com.project;

import java.util.ArrayList;

public class Sudoku {
    private Board board;
    private ArrayList<Integer> tilesLogic;
    private ArrayList<Boolean> tilesPossibilities;

    public Sudoku(Board board) {
        this.board = board;
        tilesLogic = new ArrayList<>(this.board.getSize() * this.board.getSize());
        tilesPossibilities = new ArrayList<>(this.board.getSize() * this.board.getSize() * this.board.getSize());
        for (int i = 0; i < this.tilesPossibilities.size(); ++i) {
            this.tilesPossibilities.set(i, true);
        }
        for (int i = 0; i < this.tilesLogic.size(); ++i) {
            this.tilesLogic.set(i, this.board.getSize());
        }
    }

    public void solve() {
        // TODO: real loop
        while (this.isSolvable()) {
            this.updateLogic();
        }
    }

    private boolean isSolvable() {
        for (int i = 0; i < this.board.getSize(); ++i) {
            for (int j = 0; j < this.board.getSize(); ++j) {
                if (this.board.getTileValue(i, j) == 0) {
                    if (this.tilesLogic.get(i * this.board.getSize() + j) == 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void updateLogic() {
        this.FilledUpdate();
        for (int i = 0; i < this.board.getSize(); ++i) {
            for (int j = 0; j < this.board.getSize(); ++j) {
                this.RowLogic(i, j);
                this.ColumnLogic(i, j);
                this.BoxLogic(i, j);
            }
        }
    }

    private void FilledUpdate() {
        for (int i = 0; i < this.board.getSize(); ++i) {
            for (int j = 0; j < this.board.getSize(); ++j) {
                if (this.board.getTileValue(i, j) != 0) {
                    int posInPossibilities = i * this.board.getSize() * this.board.getSize() + j * this.board.getSize();
                    this.tilesLogic.set(i * this.board.getSize() + j, Integer.valueOf(0));
                    for (int k = 0; k < this.board.getSize(); ++k) {
                        this.tilesPossibilities.set(posInPossibilities + k, false);
                    }
                }
            }
        }
    }

    private void RowLogic(int x, int y) {
        // if it needs refreshing breeze through line
        if (this.board.getTileValue(x, y) == 0) {
            int posInPossibilities = x * this.board.getSize() * this.board.getSize() + y * this.board.getSize();
            for (int i = 0; i < this.board.getSize(); ++i) {
                if (this.board.getTileValue(i, y) != 0) {
                    this.tilesPossibilities.set(posInPossibilities + this.board.getTileValue(i, y) - 1, true);
                }
            }
        }
    }

    private void ColumnLogic(int x, int y) {
        if (this.board.getTileValue(x, y) == 0) {
            int posInPossibilities = x * this.board.getSize() * this.board.getSize() + y * this.board.getSize();
            for (int i = 0; i < this.board.getSize(); ++i) {
                if (this.board.getTileValue(x, i) != 0) {
                    this.tilesPossibilities.set(posInPossibilities + this.board.getTileValue(x, i) - 1, true);
                }
            }
        }
    }

    private void BoxLogic(int x, int y) {
        if (this.board.getTileValue(x, y) == 0) {
            int tempX, tempY;
            int posInPossibilities = x * this.board.getSize() * this.board.getSize() + y * this.board.getSize();
            tempX = (x / this.board.getBoxWidth()) * this.board.getBoxWidth();
            tempY = (y / this.board.getBoxHeight()) * this.board.getBoxHeight();
            for (int i = tempX; i < tempX + this.board.getBoxWidth(); ++i) {
                for (int j = tempX; j < tempY + this.board.getBoxHeight(); ++j) {
                    if (this.board.getTileValue(i, j) == 0) {
                        this.tilesPossibilities.set(posInPossibilities + this.board.getTileValue(i, j) - 1, true);
                    }
                }
            }
        }
    }

    private int findSmallest() {
        int smallest = this.board.getSize() + 1;
        int index = -1;
        for (int i = 0; i < this.board.getSize(); ++i) {
            for (int j = 0; j < this.board.getSize(); ++j) {
                if(this.tilesLogic.get(i * this.board.getSize() + j) != 0 && this.tilesLogic.get(i * this.board.getSize() + j) < smallest)
                {
                    smallest = this.tilesLogic.get(i * this.board.getSize() + j);
                    index = i * this.board.getSize() + j;
                }
            }
        }
        return index;
    }
}
