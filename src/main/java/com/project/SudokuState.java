package com.project;

public class SudokuState {
  private Board currentBoard;
  private Board startingBoard;
  private long timeBase;
  private boolean editFlag;

  SudokuState(Board currentBoard, Board startingBoard, long timeBase, boolean editFlag) {
    this.currentBoard = currentBoard;
    this.startingBoard = startingBoard;
    this.timeBase = timeBase;
    this.editFlag = editFlag;
  }

  public Board getCurrentBoard() {
    return currentBoard;
  }

  public Board getStartingBoard() {
    return startingBoard;
  }

  public long getTimeBase() {
    return timeBase;
  }

  public boolean getEditFlag() {
    return editFlag;
  }

  public void setCurrentBoard(Board currentBoard) {
    this.currentBoard = currentBoard;
  }

  public void setStartingBoard(Board startingBoard) {
    this.startingBoard = startingBoard;
  }

  public void setTimeBase(long timeBase) {
    this.timeBase = timeBase;
  }

  public void setEditFlag(boolean editFlag) {
    this.editFlag = editFlag;
  }

  // // Each exception should have it's own message (GUI stuff)
  // public static Board loadBoard(String filePath) throws IOException, InvalidSudokuData, InvalidSudokuSize {
  //   List<String> nums = loadArray(filePath);
  //   if (!checkIfStringIsParsable(nums)) {
  //     log.warn("Sudoku data contains not parsable characters");
  //     throw new InvalidSudokuData();
  //   }
  //   Board board = createBoard(nums);
  //   board.tilesValue.clear();
  //   for (int i = 0; i < nums.size(); ++i) {
  //     board.tilesValue.add(0);
  //   }
  //   int arrSize = board.getSize();

  //   log.debug("Before trans: {}", nums);
  //   // Transform cols into rows
  //   for (int i = 0; i < arrSize; ++i) {
  //     for (int j = 0; j < arrSize; ++j) {
  //       board.tilesValue.set(i * arrSize + j, Integer.parseInt(nums.get(i + j * arrSize)));
  //     }
  //   }
  //   log.debug("After trans: {}", board.tilesValue);
  //   return board;
  // }

  // private static List<String> loadArray(String filePath) throws InvalidSudokuSize, IOException {
  //   BufferedReader reader;
  //   reader = new BufferedReader(new FileReader(filePath));
  //   List<String> nums = new ArrayList<>();
  //   while (true) {
  //     String line = reader.readLine();
  //     if (line == null) {
  //       break;
  //     }
  //     line = line.trim();
  //     nums.addAll(Arrays.asList(line.split("\\s+")));
  //   }
  //   reader.close();

  //   double testSize = Math.sqrt(nums.size());
  //   if (!((testSize == Math.floor(testSize)) && !Double.isInfinite(testSize))) {
  //     log.warn("Sudoku size has to be square value, size = {}", testSize);
  //     throw new InvalidSudokuSize();
  //   }
  //   return nums;
  // }

  // private static Board createBoard(List<String> list) throws InvalidSudokuSize {
  //   switch ((int) Math.sqrt(list.size())) {
  //     case 6:
  //       return new Board6x6(new ArrayList<>());
  //     case 8:
  //       return new Board8x8(new ArrayList<>());
  //     case 9:
  //       return new Board9x9(new ArrayList<>());
  //     case 10:
  //       return new Board10x10(new ArrayList<>());
  //     case 12:
  //       return new Board12x12(new ArrayList<>());
  //     default:
  //       log.warn("Sudoku size not within valid range, {}", list.size());
  //       throw new InvalidSudokuSize();
  //   }
  // }

  // // Data have to be colleded by rows
  // public static void saveBoard(String filePath, List<Integer> data) throws IOException {

  //   FileWriter wr = new FileWriter(filePath);
  //   // Data from GUI so it must be corret
  //   int arrSize = (int) Math.sqrt(data.size());
  //   int idx = 0;
  //   for (int i = 0; i < arrSize; ++i) {
  //     for (int j = 0; j < arrSize; ++j) {
  //       wr.append(String.valueOf(data.get(idx++)) + " ");
  //     }
  //     wr.append("\n");
  //   }
  //   wr.close();
  // }

  // private static boolean checkIfStringIsParsable(List<String> strNums) {
  //   try {
  //     for (String num : strNums) {
  //       Integer.parseInt(num);
  //     }
  //     return true;
  //   } catch (Exception e) {
  //     return false;
  //   }
  // }
}
