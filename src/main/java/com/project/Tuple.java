package com.project;

public class Tuple {
  private int x;
  private int y;
  private int value;

  public Tuple(int x, int y, int value) {
    this.x = x;
    this.y = y;
    this.value = value;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getValue() {
    return value;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void setValue(int value) {
    this.value = value;
  }
}
