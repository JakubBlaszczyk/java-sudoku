package com.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
  @JsonIgnore
  private static transient ObjectMapper mapper;

  private Board currentBoard;
  private Board startingBoard;
  private long timeBase;
  private boolean editFlag;

  /**
   * Upon creation of instance assing mapper. The rest is default.
   * 
   * @author Arkadiusz
   */
  public SudokuState() {
    mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.enableDefaultTyping();
  }

  /**
   * Constructor with initialization.
   * 
   * @param currentBoard Board on which player solves. 
   * @param startingBoard Board that playes started solving on.
   * @param timeBase 
   * @param editFlag Specified in witch state you are in. True is edit, false is solve state.
   * @author Arkadiusz
   */
  public SudokuState(Board currentBoard, Board startingBoard, long timeBase, boolean editFlag) {
    this.currentBoard = currentBoard;
    this.startingBoard = startingBoard;
    this.timeBase = timeBase;
    this.editFlag = editFlag;
    mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.enableDefaultTyping();
  }

  /**
   * Getter for currentBoard.
   * 
   * @return current currentBoard
   * @author Arkadiusz
   */
  public Board getCurrentBoard() {
    return currentBoard;
  }

  /**
   * Getter for startingBoard.
   * 
   * @return current startingBoard.
   * @author Arkadiusz
   */
  public Board getStartingBoard() {
    return startingBoard;
  }

  /**
   * Getter for timeBase.
   * 
   * @return current timeBase.
   * @author Arkadiusz
   */
  public long getTimeBase() {
    return timeBase;
  }

  /**
   * Getter for editFlag.
   * 
   * @return current editFlag.
   * @author Arkadiusz
   */
  public boolean getEditFlag() {
    return editFlag;
  }

  /**
   * Setter for currentBoard.
   * 
   * @param currentBoard Board to be set.
   * @author Arkadiusz
   */
  public void setCurrentBoard(Board currentBoard) {
    this.currentBoard = currentBoard;
  }

  /**
   * Setter for startingBoard.
   * 
   * @param startingBoard Board to be set.
   * @author Arkadiusz
   */
  public void setStartingBoard(Board startingBoard) {
    this.startingBoard = startingBoard;
  }

  /**
   * Setter for timeBase.
   * 
   * @param timeBase long to be set.
   * @author Arkadiusz
   */
  public void setTimeBase(long timeBase) {
    this.timeBase = timeBase;
  }

  /**
   * Setter for editFlag.
   * 
   * @param editFlag boolean to be set.
   * @author Arkadiusz
   */
  public void setEditFlag(boolean editFlag) {
    this.editFlag = editFlag;
  }
  
  /**
   * Saves instance of object to the file.
   * 
   * @param filename name of file to be saved.
   * @throws IOException
   * @override
   * @author Arkadiusz
   */
  @Override
  public void saveToFile(String filename) throws IOException {
    serializeXML(filename, this);
  }

  /**
   * Loades SudokuState from file.
   * 
   * @param filename name of file to be loaded.
   * @return SudokuState to be set.
   * @override
   * @author Arkadiusz
   */
  @Override
  public SudokuState loadFromFile(String filename) throws MalformedFile, IOException, ClassNotFoundException {
    return deserializeXML(filename);
  }

  private static void serializeXML(String filename, SudokuState state) throws IOException {
    if (filename != null) {
      mapper.writeValue(new File(filename), state);
    }
  }

  private static SudokuState deserializeXML(String filename) throws MalformedFile, IOException {
    StringBuilder xml = new StringBuilder();
    String strLine = "";
    if (filename == null) {
      throw new InvalidParameterException("Filename is null, should never happen");
    }
    try (FileReader f = new FileReader(filename); BufferedReader r = new BufferedReader(f);) {
      while ((strLine = r.readLine()) != null) {
        xml.append(strLine);
      }
      return mapper.readValue(xml.toString(), SudokuState.class);
    } catch (ClassCastException e) {
      log.error("Cannot cast Object from XML", e);
      throw new MalformedFile();
    }
  }
}
