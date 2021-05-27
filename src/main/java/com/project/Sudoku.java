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
    }

    public void solve() {
        while (!this.isSolved()) {
            this.updateLogic();
        }
    }

    private boolean isSolved() {
        for (int i = 0; i < board.getSize(); ++i) {
            for (int j = 0; j < board.getSize(); ++j) {
                if (board.getTileValue(i, j) == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateLogic() {

    }

    private void RowLogic() {
        for(int i = 0; i < this.board.getSize(); ++i)
        {
            
        }
    }

    private void ColumnLogic() {

    }

    private void BoxLogic() {

    }
}
