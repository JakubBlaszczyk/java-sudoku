package com.project;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("/fxml/Bazav2.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root, 300, 275);
    ControllerMainWindow cMainWindow = loader.getController();
    cMainWindow.setStage(stage);
    stage.setTitle("Choose board");
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
