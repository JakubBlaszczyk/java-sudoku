package com.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.naming.MalformedLinkException;

import com.project.boards.Board10x10;
import com.project.boards.Board12x12;
import com.project.boards.Board6x6;
import com.project.boards.Board8x8;
import com.project.boards.Board9x9;
import com.project.exceptions.InvalidSudokuData;
import com.project.exceptions.InvalidSudokuSize;
import com.project.exceptions.SudokuAlreadySolved;
import com.project.exceptions.SudokuUnsolvable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

// TODO:
// switching windows : DONE
// Load from file : DONE
// Reafactor

public class ControllerBoard {
  private static Logger log = LoggerFactory.getLogger(ControllerBoard.class);

  private Background red = new Background(
      new BackgroundFill(Color.RED, new CornerRadii(1), new Insets(0.0, 0.0, 0.0, 0.0)));
  private Background white = new Background(
      new BackgroundFill(Color.WHITE, new CornerRadii(1), new Insets(0.0, 0.0, 0.0, 0.0)));
  private Stage mainStage = null;
  private Stage currentStage = null;
  private List<Button> allButtons = null;
  private Board board = null;
  private Board startingBoard = null;
  private boolean editFlag = true;
  private Date start = null;

  @FXML
  private GridPane sudokuBoard;
  @FXML
  private HBox setButtons;
  @FXML
  private Label modeLabel;
  @FXML
  private Button changeModeButton;
  @FXML
  private Button CheckButton;

  @FXML
  public void initialize() {
    allButtons = new ArrayList<>();
    for (Node node : sudokuBoard.getChildren()) {
      for (Node nodeInt : ((GridPane) node).getChildren()) {
        if (nodeInt instanceof Pane) {
          allButtons.add((Button) (((Pane) nodeInt).getChildren().get(0)));
        } else {
          allButtons.add((Button) nodeInt);
        }
      }
    }
    allButtons.sort((object1, object2) -> getIdValue(object1.getId()).compareTo(getIdValue(object2.getId())));
    for (Button button : allButtons) {
      log.debug(button.getId());
    }
  }

  private Integer getIdValue(String id) {
    return Integer.valueOf(id.substring(6));
  }

  private void startTimer() {
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
      if (editFlag) {
        return;
      }
      long countUp = Calendar.getInstance().getTime().getTime() - start.getTime();
      long seconds = TimeUnit.SECONDS.convert(countUp, TimeUnit.MILLISECONDS);
      long minutes = seconds / 60;
      seconds -= minutes * 60;
      currentStage.setTitle(formatTime(minutes, seconds));
    }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  private String formatTime(long minutes, long seconds) {
    String mins = String.valueOf(minutes);
    String secs = String.valueOf(seconds);
    if (mins.length() == 1) {
      mins = "0" + mins;
    }
    if (secs.length() == 1) {
      secs = "0" + secs;
    }
    return mins + ":" + secs;
  }

  private void setupSudokuButtons() {
    EventHandler<ActionEvent> handler = ev -> {
      Button button = ((Button) ev.getSource());
      if (button.getBackground().equals(red)) {
        button.setBackground(white);
      } else {
        button.setBackground(red);
      }
    };

    for (Button button : allButtons) {
      button.setOnAction(handler);
    }
  }

  private void setupNumberButtons() {
    EventHandler<MouseEvent> handler = ev -> {
      int clicked = 0;
      for (Button button : allButtons) {
        if (button.getBackground().equals(red)) {
          ++clicked;
          button.setText(((Button) ev.getSource()).getText());
          button.setBackground(white);
        }
      }
      if (clicked == 0) {
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

  public void startup(Stage mainStage, Stage currentStage, Board board) {
    log.debug("Startup");
    this.board = board;
    this.mainStage = mainStage;
    this.currentStage = currentStage;
    setupSudokuButtons();
    setupNumberButtons();
    startTimer();
    Window wnd = currentStage.getScene().getWindow();
    if (wnd == null) {
      log.error("Window is null");
      throw new NullPointerException();
    }
    // TODO Some popup to prevent?
    wnd.setOnCloseRequest(ev -> {
      ev.consume();
      currentStage.hide();
      mainStage.show();
    });
    if (allButtons.size() == board.getTilesValue().size()) {
      for (int i = 0; i < allButtons.size(); ++i) {
        allButtons.get(i).setText(String.valueOf(board.getTilesValue().get(i)));
      }
    }
  }

  public void handleNewBoard(ActionEvent ev) {
    log.debug("handleNewBoard, ActionEvent: {}", ev);
    Stage stage;
    Parent root;
    FXMLLoader loader;
    Board newBoard;
    MenuItem mItem = (MenuItem) ev.getSource();
    try {
      switch (mItem.getId()) {
        case "Load6x6": {
          log.debug("Loading 6x6");
          loader = new FXMLLoader(getClass().getResource("/fxml/6x6v2.fxml"));
          newBoard = new Board6x6(new ArrayList<>());
          newBoard.initializeList(6 * 6, 0);
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("00:00");
          break;
        }
        case "Load8x8": {
          log.debug("Loading 8x8");
          loader = new FXMLLoader(getClass().getResource("/fxml/8x8v2.fxml"));
          newBoard = new Board8x8(new ArrayList<>());
          newBoard.initializeList(8 * 8, 0);
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("00:00");
          break;
        }
        case "Load9x9": {
          log.debug("Loading 9x9");
          loader = new FXMLLoader(getClass().getResource("/fxml/9x9v2.fxml"));
          newBoard = new Board9x9(new ArrayList<>());
          newBoard.initializeList(9 * 9, 0);
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("00:00");
          break;
        }
        case "Load10x10": {
          log.debug("Loading 10x10");
          loader = new FXMLLoader(getClass().getResource("/fxml/10x10v2.fxml"));
          newBoard = new Board10x10(new ArrayList<>());
          newBoard.initializeList(10 * 10, 0);
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("00:00");
          break;
        }
        case "Load12x12": {
          log.debug("Loading 12x12");
          loader = new FXMLLoader(getClass().getResource("/fxml/12x12v2.fxml"));
          newBoard = new Board12x12(new ArrayList<>());
          newBoard.initializeList(12 * 12, 0);
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("00:00");
          break;
        }
        default:
          log.error("Should never happen, Invalid ID: {}", mItem.getId());
          throw new MalformedLinkException("TMP");
      }
      stage.setScene(new Scene(root));
      ControllerBoard cBoard = loader.getController();
      cBoard.startup(mainStage, stage, newBoard);
      currentStage.close();
      stage.setResizable(false);
      stage.show();
    } catch (Exception e) {
      log.error("Should never happen, Excetpion in new stage", e);
    }
  }

  public void LoadFromFile(ActionEvent ev) throws MalformedLinkException {
    FileChooser fChooser = new FileChooser();
    fChooser.setTitle("Choose sudoku board file");
    File fHandle = fChooser.showOpenDialog(currentStage);
    if (fHandle == null) {
      return;
    }
    try {
      board = Board.loadBoard(fHandle.getAbsolutePath());
      Stage stage;
      Parent root;
      FXMLLoader loader;
      switch (board.getSize()) {
        case 6: {
          log.debug("Loading 6x6 from file");
          loader = new FXMLLoader(getClass().getResource("/fxml/6x6v2.fxml"));
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("00:00");
          break;
        }
        case 8: {
          log.debug("Loading 8x8 from file");
          loader = new FXMLLoader(getClass().getResource("/fxml/8x8v2.fxml"));
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("00:00");
          break;
        }
        case 9: {
          log.debug("Loading 9x9 from file");
          loader = new FXMLLoader(getClass().getResource("/fxml/9x9v2.fxml"));
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("00:00");
          break;
        }
        case 10: {
          log.debug("Loading 10x10 from file");
          loader = new FXMLLoader(getClass().getResource("/fxml/10x10v2.fxml"));
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("00:00");
          break;
        }
        case 12: {
          log.debug("Loading 12x12 from file");
          loader = new FXMLLoader(getClass().getResource("/fxml/12x12v2.fxml"));
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("00:00");
          break;
        }
        default:
          log.error("Should never happen, Invalid size: {}", board.getSize());
          throw new MalformedLinkException("TMP");
      }
      stage.setScene(new Scene(root));
      ControllerBoard cBoard = loader.getController();
      cBoard.startup(mainStage, stage, board);
      mainStage.close();
      currentStage.close();
      stage.setResizable(false);
      stage.show();
      ///
      // TODO add handling
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InvalidSudokuData e) {
      new Alert(Alert.AlertType.INFORMATION, "Not parsable characters in sudoku board").show();
    } catch (InvalidSudokuSize e) {
      new Alert(Alert.AlertType.INFORMATION, "Invalid sudoku size, should be 6x6, 8x8, 9x9, 10x10, 12x12").show();
    }
  }

  private void updateBoard(Board board) {
    List<Integer> boardValues = board.getTilesValue();
    for (int i = 0; i < allButtons.size(); ++i) {
      boardValues.set(i, Integer.valueOf(allButtons.get(i).getText()));
    }
  }

  private void updateButtons(Board board) {
    List<Integer> newValues = board.getTilesValue();
    for (int i = 0; i < newValues.size(); ++i) {
      allButtons.get(i).setText(String.valueOf(newValues.get(i)));
    }
  }

  public void handleHint(ActionEvent ev) {
    updateBoard(this.board);
    try {
      log.info("Calling hint");
      Hint hint = Sudoku.hint(board);
      int idx = hint.getX() * board.getSize() + hint.getY();
      log.debug("Hint: {}, {}, {}", hint.getX(), hint.getY(), hint.getValue());
      log.debug("IDX: {}", idx);
      Button changedButton = allButtons.get(idx);
      changedButton.setText(String.valueOf(hint.getValue()));
      // TODO ?
      changedButton.setBackground(red);
    } catch (SudokuAlreadySolved e) {
      log.debug("Already solved", e);
      new Alert(Alert.AlertType.INFORMATION, "Sudoku already solved").show();
    } catch (SudokuUnsolvable e) {
      log.debug("Unsolvable", e);
      new Alert(Alert.AlertType.INFORMATION, "Sudoku unsolvable").show();
    }
  }

  public void handleSolve(ActionEvent ev) {
    updateBoard(this.board);
    try {
      // Board will be changed?
      log.info("Calling solve");
      Sudoku.solve(board);
      // Stop timer
      updateButtons(this.board);
    } catch (SudokuAlreadySolved e) {
      log.debug("Already solved", e);
      new Alert(Alert.AlertType.INFORMATION, "Sudoku already solved").show();
    } catch (SudokuUnsolvable e) {
      log.debug("Unsolvable", e);
      new Alert(Alert.AlertType.INFORMATION, "Sudoku unsolvable").show();
    }
  }

  public void handleCheck(ActionEvent ev) {
    updateBoard(this.board);
    changeMode(null);
    try {
      log.info("Calling check");
      List<Hint> mistakes = Sudoku.check(board, startingBoard);
      log.debug("Check passed");
      for (Hint hint : mistakes) {
        int idx = hint.getX() * board.getSize() + hint.getY();
        log.debug("Hint: {}, {}, {}", hint.getX(), hint.getY(), hint.getValue());
        log.debug("IDX: {}", idx);
        Button changedButton = allButtons.get(idx);
        changedButton.setText(String.valueOf(hint.getValue()));
      }
      // Show mistakes
    } catch (SudokuUnsolvable e) {
      log.debug("Unsolvable", e);
      new Alert(Alert.AlertType.INFORMATION, "Starting board is unsolvable. Check cannot verify your progress, start again").show();
    } catch (SudokuAlreadySolved e) {
      log.debug("Already solved", e);
      new Alert(Alert.AlertType.INFORMATION, "Already solved").show();
    }
  }

  public void changeMode(ActionEvent ev) {
    if (editFlag) {
      // TODO add timer reloading
      editFlag = false;
      modeLabel.setText("Solve mode");
      startingBoard = board.copy();
      log.debug("updating startingBoard");
      updateBoard(startingBoard);
      start = Calendar.getInstance().getTime();
      changeModeButton.setDisable(true);
    }
  }

  public void handleSaveToFile(ActionEvent ev) {
    Menu file = new Menu("File");
    MenuItem item = new MenuItem("Save");
    file.getItems().addAll(item);
    // Creating a File chooser
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save");
    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
    File fHandle = fileChooser.showSaveDialog(currentStage);
    if (fHandle == null) {
      return;
    }
    log.debug(fHandle.getAbsolutePath());
    updateBoard(this.board);
    try {
      Board.saveBoard(fHandle.getAbsolutePath(), this.board.getTilesValue());
    } catch (IOException e) {
      log.error("Cannot save file in the specified path", e);
      new Alert(Alert.AlertType.ERROR, "Cannot save file in the specified path").show();
    }
  }
}
