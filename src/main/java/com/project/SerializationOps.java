package com.project;

import java.io.IOException;

import com.project.exceptions.MalformedFile;

public interface SerializationOps {
  void saveToFile(String filename) throws IOException;
  SudokuState loadFromFile(String filename) throws MalformedFile, IOException, ClassNotFoundException;
}
