package com.project;

import javax.naming.MalformedLinkException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.project.boards.Board10x10;
import com.project.boards.Board12x12;
import com.project.boards.Board6x6;
import com.project.boards.Board8x8;
import com.project.boards.Board9x9;
import com.project.exceptions.InvalidSudokuData;
import com.project.exceptions.InvalidSudokuSize;

public class ControllerMainWindow {
  private static Logger log = LoggerFactory.getLogger(ControllerMainWindow.class);
  private Stage mainStage;

  public void handleNewBoard(ActionEvent ev) {
    log.debug("handleNewBoard, ActionEvent: {}", ev);
    Stage stage;
    Parent root;
    FXMLLoader loader;
    Board newBoard;
    Button button = (Button) ev.getSource();
    try {
      switch (button.getId()) {
        case "Board6x6": {
          log.debug("Board 6x6");
          loader = new FXMLLoader(getClass().getResource("/fxml/6x6v2.fxml"));
          newBoard = new Board6x6(new ArrayList<>());
          newBoard.initializeList(6*6, 0);
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP6 - Czas 0");
          break;
        }
        case "Board8x8": {
          log.debug("Board 8x8");
          loader = new FXMLLoader(getClass().getResource("/fxml/8x8v2.fxml"));
          newBoard = new Board8x8(new ArrayList<>());
          newBoard.initializeList(8*8, 0);
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP8 - Czas 0");
          break;
        }
        case "Board9x9": {
          log.debug("Board 9x9");
          loader = new FXMLLoader(getClass().getResource("/fxml/9x9v2.fxml"));
          newBoard = new Board9x9(new ArrayList<>());
          newBoard.initializeList(9*9, 0);
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP9 - Czas 0");
          break;
        }
        case "Board10x10": {
          log.debug("Board 10x10");
          loader = new FXMLLoader(getClass().getResource("/fxml/10x10v2.fxml"));
          newBoard = new Board10x10(new ArrayList<>());
          newBoard.initializeList(10*10, 0);
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP10 - Czas 0");
          break;
        }
        case "Board12x12": {
          log.debug("Board 12x12");
          loader = new FXMLLoader(getClass().getResource("/fxml/12x12v2.fxml"));
          newBoard = new Board12x12(new ArrayList<>());
          newBoard.initializeList(12*12, 0);
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP12 - Czas 0");
          break;
        }
        default:
          log.error("Should never happen, Invalid ID: {}", button.getId());
          throw new MalformedLinkException("TMP");
      }
      stage.setScene(new Scene(root));
      ControllerBoard cBoard = loader.getController();
      cBoard.startup(mainStage, stage, newBoard);
      mainStage.close();
      stage.show();
    } catch (Exception e) {
      log.error("Should never happen, Exception in new stage", e);
    }
  }

  public void handleLoadFile(ActionEvent ev) throws MalformedLinkException {
    FileChooser fChooser = new FileChooser();
    fChooser.setTitle("Choose sudoku board file");
    File fHandle = fChooser.showOpenDialog(mainStage);
    Board board;
    if (fHandle == null) {
      return;
    }
    try {
      board = Board.loadBoard(fHandle.getAbsolutePath());
      List<Integer> vals = board.getTilesValue();
      // for (int i = 0; i < vals.size(); ++i) {
      // allButtons.get(i).setText(String.valueOf(vals.get(i)));
      // }
      ///
      Stage stage;
      Parent root;
      FXMLLoader loader;
      switch (board.getSize()) {
        case 6: {
          log.debug("Loading 6x6 from file");
          loader = new FXMLLoader(getClass().getResource("/fxml/6x6v2.fxml"));
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP6 - Czas 0");
          break;
        }
        case 8: {
          log.debug("Loading 8x8 from file");
          loader = new FXMLLoader(getClass().getResource("/fxml/8x8v2.fxml"));
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP8 - Czas 0");
          break;
        }
        case 9: {
          log.debug("Loading 9x9 from file");
          loader = new FXMLLoader(getClass().getResource("/fxml/9x9v2.fxml"));
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP9 - Czas 0");
          break;
        }
        case 10: {
          log.debug("Loading 10x10 from file");
          loader = new FXMLLoader(getClass().getResource("/fxml/10x10v2.fxml"));
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP10 - Czas 0");
          break;
        }
        case 12: {
          log.debug("Loading 12x12 from file");
          loader = new FXMLLoader(getClass().getResource("/fxml/12x12v2.fxml"));
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP12 - Czas 0");
          break;
        }
        default:
          // log.error("Should never happen, Invalid ID: {}", mItem.getId());
          throw new MalformedLinkException("TMP");
      }
      stage.setScene(new Scene(root));
      ControllerBoard cBoard = loader.getController();
      cBoard.startup(mainStage, stage, board);
      mainStage.close();
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

  public void setStage(Stage stage) {
    mainStage = stage;
  }
}
