package com.project;

import javax.naming.MalformedLinkException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControllerMainWindow {
  private static Logger log = LoggerFactory.getLogger(ControllerMainWindow.class);
  private Stage mainStage;

  public void handleNewBoard(ActionEvent ev) {
    Stage stage;
    Parent root;
    FXMLLoader loader;
    MenuItem mItem = (MenuItem) ev.getSource();
    try {
      switch (mItem.getId()) {
        case "x6": {
          loader = new FXMLLoader(getClass().getResource("/fxml/6x6.fxml"));
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP6 - Czas 0");
          break;
        }
        case "x8": {
          loader = new FXMLLoader(getClass().getResource("/fxml/8x8.fxml"));
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP8 - Czas 0");
          break;
        }
        case "x9": {
          loader = new FXMLLoader(getClass().getResource("/fxml/9x9.fxml"));
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP9 - Czas 0");
          break;
        }
        case "x10": {
          loader = new FXMLLoader(getClass().getResource("/fxml/10x10.fxml"));
          root = (Parent) loader.load();
          stage = new Stage();
          stage.setTitle("TMP10 - Czas 0");
          break;
        }
        case "x12": {
          loader = new FXMLLoader(getClass().getResource("/fxml/12x12.fxml"));
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
      cBoard.startup(mainStage, stage);
      mainStage.close();
      stage.show();
    } catch (Exception e) {
      log.error("Should never happen", e);
    }
  }

  public void setStage(Stage stage) {
    mainStage = stage;
  }
}
