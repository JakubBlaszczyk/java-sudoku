package com.project;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Controller {
  @FXML
  private Button Button00; // Id 00,01,02...-55

  BackgroundFill red = new BackgroundFill(Color.RED, new CornerRadii(1), new Insets(0.0, 0.0, 0.0, 0.0));
  BackgroundFill white = new BackgroundFill(Color.WHITE, new CornerRadii(1), new Insets(0.0, 0.0, 0.0, 0.0));
  Object lastClickedButton = null;

  @FXML
  private GridPane sudokuBoard;
  @FXML
  private HBox setButtons;

  @FXML
  public void initialize() {
    // setup
    setupSudokuButtons();
    setupNumberButtons();
  }

  private void setupSudokuButtons() {
    EventHandler<ActionEvent> handler = ev -> {
      Button button = ((Button) ev.getSource());
      if (button.getBackground().equals(new Background(red))) {
        button.setBackground(new Background(white));
      } else {
        button.setBackground(new Background(red));
      }
    };

    for (Node node : sudokuBoard.getChildren()) {
      if (!(node instanceof Pane)) {
        throw new IllegalArgumentException("Missing Pane in pane grid!");
      }
      Button button = (Button) (((Pane) node).getChildren().get(0));
      button.setOnAction(handler);
    }
  }

  private void setupNumberButtons() {
    EventHandler<MouseEvent> handler = ev -> {
      int clicked = 0;
      for (Node node : sudokuBoard.getChildren()) {
        if (!(node instanceof Pane)) {
          throw new IllegalArgumentException("Missing Pane in pane grid!");
        }
        Button button = (Button) (((Pane) node).getChildren().get(0));
        if (button.getBackground().equals(new Background(red))) {
          ++clicked;
          button.setText(((Button) ev.getSource()).getText());
          button.setBackground(new Background(white));
        }
      }
      if (clicked == 0) {
        // TODO add popup
      }
    };

    for (Node node : setButtons.getChildren()) {
      if (!(node instanceof Button)) {
        throw new IllegalArgumentException("Not Button in button grid!");
      }
      ((Button) node).setOnMouseClicked(handler);
    }
  }

  public void setSudokuButtonValue(ActionEvent ev) {
    Button button = ((Button) ev.getSource());
    if (button.getBackground().equals(new Background(red))) {
      button.setBackground(new Background(white));
    } else {
      button.setBackground(new Background(red));
    }
  }

  public void onClickSudokuNumber(MouseEvent ev) {
    int clicked = 0;
    for (Node node : sudokuBoard.getChildren()) {
      if (!(node instanceof Pane)) {
        throw new IllegalArgumentException("Missing Pane in pane grid!");
      }
      Button button = (Button) (((Pane) node).getChildren().get(0));
      if (button.getBackground().equals(new Background(red))) {
        ++clicked;
        button.setText(((Button) ev.getSource()).getText());
      }
    }
    if (clicked == 0) {
      // TODO add popup
    }
  }
}
