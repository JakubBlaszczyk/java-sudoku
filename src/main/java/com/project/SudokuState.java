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
  @JsonIgnore
  private static transient ObjectMapper mapper;

  private Board currentBoard;
  private Board startingBoard;
  private long timeBase;
  private boolean editFlag;

  SudokuState() {
    mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.enableDefaultTyping();
  }

  SudokuState(Board currentBoard, Board startingBoard, long timeBase, boolean editFlag) {
    this.currentBoard = currentBoard;
    this.startingBoard = startingBoard;
    this.timeBase = timeBase;
    this.editFlag = editFlag;
    mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.enableDefaultTyping();
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
