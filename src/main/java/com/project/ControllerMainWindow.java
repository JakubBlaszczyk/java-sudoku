package com.project;

import javax.naming.MalformedLinkException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import java.util.ArrayList;
import javafx.scene.control.MenuItem;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.project.boards.Board10x10;
import com.project.boards.Board12x12;
import com.project.boards.Board6x6;
import com.project.boards.Board8x8;
import com.project.boards.Board9x9;

public class ControllerMainWindow {
  private static Logger log = LoggerFactory.getLogger(ControllerMainWindow.class);
  private Stage mainStage;

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
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP6 - Czas 0");
          break;
        }
        case "Load8x8": {
          log.debug("Loading 8x8");
          loader = new FXMLLoader(getClass().getResource("/fxml/8x8v2.fxml"));
          newBoard = new Board8x8(new ArrayList<>());
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP8 - Czas 0");
          break;
        }
        case "Load9x9": {
          log.debug("Loading 9x9");
          loader = new FXMLLoader(getClass().getResource("/fxml/9x9v2.fxml"));
          newBoard = new Board9x9(new ArrayList<>());
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP9 - Czas 0");
          break;
        }
        case "Load10x10": {
          log.debug("Loading 10x10");
          loader = new FXMLLoader(getClass().getResource("/fxml/10x10v2.fxml"));
          newBoard = new Board10x10(new ArrayList<>());
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP10 - Czas 0");
          break;
        }
        case "Load12x12": {
          log.debug("Loading 12x12");
          loader = new FXMLLoader(getClass().getResource("/fxml/12x12v2.fxml"));
          newBoard = new Board12x12(new ArrayList<>());
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP12 - Czas 0");
          break;
        }
        default:
          log.error("Should never happen, Invalid ID: {}", mItem.getId());
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

  public void setStage(Stage stage) {
    mainStage = stage;
  }
}
