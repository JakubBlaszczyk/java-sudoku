package com.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.project.exceptions.InvalidSudokuData;
import com.project.exceptions.InvalidSudokuSize;

public class Board {
    private List<Integer> tilesValue;

    public Board(List<Integer> tilesValue) {
        this.tilesValue = tilesValue;
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
        return tilesValue.get(x * tilesValue.size() + y).intValue();
    }

    public int setTileValue(int x, int y, int value) {
        return tilesValue.set(x * tilesValue.size() + y, Integer.valueOf(value));
    }
    
    // Each exception should have it's own message (GUI stuff)
    public void loadBoard(String filePath) throws IOException, InvalidSudokuData, InvalidSudokuSize {
      tilesValue.clear();

      BufferedReader reader;
			reader = new BufferedReader(new FileReader(filePath));
      List<String> nums = new ArrayList<>();
			while (true) {
        String line = reader.readLine();
        if (line == null) {
          break;
        }
        nums.addAll(Arrays.asList(line.split(" ")));
      }
      reader.close();

      double testSize = Math.sqrt(nums.size());
      
      if (!((testSize == Math.floor(testSize)) && !Double.isInfinite(testSize))) {
        throw new InvalidSudokuSize();
      }
      int arrSize = (int)testSize;
      
      if (!checkIfStringIsParsable(nums)) {
        throw new InvalidSudokuData();
      }
      
      for (int i = 0; i < nums.size(); ++i) {
        tilesValue.add(0);
      }
      
      // Transform cols into rows
      for (int i = 0; i < arrSize; ++i) {
        for (int j = 0; j < arrSize; ++j) {
          tilesValue.set(i * arrSize + j, Integer.parseInt(nums.get(i + j * arrSize)));
        }
      }
      // System.out.println(tilesValue);
    }

    // Data have to be colleded by rows
    public void saveBoard(String filePath, List<Integer> data) throws IOException {

      FileWriter wr = new FileWriter(filePath);
      // Data from GUI so it must be corret
      int arrSize = (int)Math.sqrt(data.size());
      int idx = 0;
      for (int i = 0; i < arrSize; ++i) {
        for (int j = 0; j < arrSize; ++j) {
          wr.append(String.valueOf(data.get(idx++)));
          wr.append(" ");
        }
        wr.append("\n");
      }
      wr.flush();
      wr.close();
    }

    private boolean checkIfStringIsParsable(List<String> strNums) {
      try {
        for (String num : strNums) {
          Integer.parseInt(num);
        }
        return true;
      } catch (Exception e) {
        return false;
      }
    }
}
