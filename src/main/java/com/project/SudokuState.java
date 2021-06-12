package com.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidParameterException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.boards.Board;
import com.project.exceptions.MalformedFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SudokuState implements SerializationOps, Serializable {
  @JsonIgnore
  private static transient Logger log = LoggerFactory.getLogger(SudokuState.class);

  private Board currentBoard;
  private Board startingBoard;
  private long timeBase;
  private boolean editFlag;

  SudokuState() {}

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

  @Override
  public void saveToFile(String filename) throws IOException {
    serializeXML(filename, this);
  }

  @Override
  public SudokuState loadFromFile(String filename) throws MalformedFile, IOException, ClassNotFoundException {
    return deserializeXML(filename);
  }

  public static void serializeXML(String filename, SudokuState state) throws IOException {
    if (filename != null) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      mapper.enableDefaultTyping();
      mapper.writeValue(new File(filename), state);
    }
  }

  // public static void serializeXML(String filename, SudokuState state) throws IOException {
  //   if (filename != null) {
  //     try (FileWriter f = new FileWriter(filename); BufferedWriter out = new BufferedWriter(f);) {
  //       XStream mapping = new XStream(new DomDriver());
  //       mapping.addPermission(NoTypePermission.NONE);
  //     mapping.allowTypesByRegExp(new String[] { ".*" });
  //       String xml = mapping.toXML(state);
  //       out.write(xml);
  //     }
  //   }
  // }

  // public static void serializeXML(String filename, SudokuState state) {
  //   try (FileOutputStream fos = new FileOutputStream(filename); ObjectOutputStream oos = new ObjectOutputStream(fos);) {
  //       oos.writeObject(state);
  //   } catch (Exception e) {
  //     e.printStackTrace();
  //   }
  // }

  public static SudokuState deserializeXML(String filename) throws MalformedFile, IOException {
    StringBuilder xml = new StringBuilder();
    String strLine = "";
    ObjectMapper mapper = new ObjectMapper();
    if (filename == null) {
      throw new InvalidParameterException("Filename is null, should never happen");
    }
    try (FileReader f = new FileReader(filename); BufferedReader r = new BufferedReader(f);) {
      while ((strLine = r.readLine()) != null) {
        xml.append(strLine);
      }
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      mapper.enableDefaultTyping();
      return mapper.readValue(xml.toString(), SudokuState.class);
      // return (SudokuState) mapping.fromXML(xml.toString());
    } catch (ClassCastException e) {
      log.error("Cannot cast Object from XML", e);
      throw new MalformedFile();
    }
  }

  // public static SudokuState deserializeXML(String filename) throws MalformedFile, IOException {
  //   StringBuilder xml = new StringBuilder();
  //   String strLine = "";
  //   if (filename == null) {
  //     throw new InvalidParameterException("Filename is null, should never happen");
  //   }
  //   try (FileReader f = new FileReader(filename); BufferedReader r = new BufferedReader(f);) {
  //     while ((strLine = r.readLine()) != null) {
  //       xml.append(strLine);
  //     }
  //     XStream mapping = new XStream(new DomDriver());
  //     mapping.addPermission(NoTypePermission.NONE);
  //     mapping.allowTypesByRegExp(new String[] { ".*" });
  //     return (SudokuState) mapping.fromXML(xml.toString());
  //   } catch (ClassCastException e) {
  //     log.error("Cannot cast Object from XML", e);
  //     throw new MalformedFile();
  //   }
  // }

  // public static SudokuState deserializeXML(String filename) throws ClassNotFoundException, IOException {
  //   SudokuState state = null;
  //   try (FileInputStream fis = new FileInputStream(filename); ObjectInputStream ois = new ObjectInputStream(fis);) {
  //       state = (SudokuState)ois.readObject();
  //       return state;
  //   }
  // }
  }
//

// // Each exception should have it's own message (GUI stuff)
// public static Board loadBoard(String filePath) throws IOException,
// InvalidSudokuData, InvalidSudokuSize {
// List<String> nums = loadArray(filePath);
// if (!checkIfStringIsParsable(nums)) {
// log.warn("Sudoku data contains not parsable characters");
// throw new InvalidSudokuData();
// }
// Board board = createBoard(nums);
// board.tilesValue.clear();
// for (int i = 0; i < nums.size(); ++i) {
// board.tilesValue.add(0);
// }
// int arrSize = board.getSize();

// log.debug("Before trans: {}", nums);
// // Transform cols into rows
// for (int i = 0; i < arrSize; ++i) {
// for (int j = 0; j < arrSize; ++j) {
// board.tilesValue.set(i * arrSize + j, Integer.parseInt(nums.get(i + j *
// arrSize)));
// }
// }
// log.debug("After trans: {}", board.tilesValue);
// return board;
// }

// private static List<String> loadArray(String filePath) throws
// InvalidSudokuSize, IOException {
// BufferedReader reader;
// reader = new BufferedReader(new FileReader(filePath));
// List<String> nums = new ArrayList<>();
// while (true) {
// String line = reader.readLine();
// if (line == null) {
// break;
// }
// line = line.trim();
// nums.addAll(Arrays.asList(line.split("\\s+")));
// }
// reader.close();

// double testSize = Math.sqrt(nums.size());
// if (!((testSize == Math.floor(testSize)) && !Double.isInfinite(testSize))) {
// log.warn("Sudoku size has to be square value, size = {}", testSize);
// throw new InvalidSudokuSize();
// }
// return nums;
// }

// private static Board createBoard(List<String> list) throws InvalidSudokuSize
// {
// switch ((int) Math.sqrt(list.size())) {
// case 6:
// return new Board6x6(new ArrayList<>());
// case 8:
// return new Board8x8(new ArrayList<>());
// case 9:
// return new Board9x9(new ArrayList<>());
// case 10:
// return new Board10x10(new ArrayList<>());
// case 12:
// return new Board12x12(new ArrayList<>());
// default:
// log.warn("Sudoku size not within valid range, {}", list.size());
// throw new InvalidSudokuSize();
// }
// }

// // Data have to be colleded by rows
// public static void saveBoard(String filePath, List<Integer> data) throws
// IOException {

// FileWriter wr = new FileWriter(filePath);
// // Data from GUI so it must be corret
// int arrSize = (int) Math.sqrt(data.size());
// int idx = 0;
// for (int i = 0; i < arrSize; ++i) {
// for (int j = 0; j < arrSize; ++j) {
// wr.append(String.valueOf(data.get(idx++)) + " ");
// }
// wr.append("\n");
// }
// wr.close();
// }

// private static boolean checkIfStringIsParsable(List<String> strNums) {
// try {
// for (String num : strNums) {
// Integer.parseInt(num);
// }
// return true;
// } catch (Exception e) {
// return false;
// }
// }