package com.project;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ControllerBoard {
  private static Logger log = LoggerFactory.getLogger(ControllerBoard.class);

  BackgroundFill red = new BackgroundFill(Color.RED, new CornerRadii(1), new Insets(0.0, 0.0, 0.0, 0.0));
  BackgroundFill white = new BackgroundFill(Color.WHITE, new CornerRadii(1), new Insets(0.0, 0.0, 0.0, 0.0));
  Stage mainStage = null;
  Stage currentStage = null;

  @FXML
  private GridPane sudokuBoard;
  @FXML
  private HBox setButtons;

  private void startTimer() {
    Date start = Calendar.getInstance().getTime();
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
      long countUp = Calendar.getInstance().getTime().getTime() - start.getTime();
      currentStage.setTitle("Sudoku - Time: " + TimeUnit.SECONDS.convert(countUp, TimeUnit.MILLISECONDS));
    }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
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
        new Alert(Alert.AlertType.INFORMATION, "Select fields that you want change").show();
      }
    };

    for (Node node : setButtons.getChildren()) {
      if (!(node instanceof Button)) {
        throw new IllegalArgumentException("Not Button in button grid!");
      }
      ((Button) node).setOnMouseClicked(handler);
    }
  }

  public void startup(Stage mainStage, Stage currentStage) {
    this.mainStage = mainStage;
    this.currentStage = currentStage;
    setupSudokuButtons();
    setupNumberButtons();
    startTimer();
  }
}
