package com.project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import  javafx.scene.control.Button;
//6x6
public class Controller  {
    @FXML
    public void initialize() {
   //     buttons.add(Button00);
    }

//    private List<Button>  buttons = new ArrayList<>();
    @FXML
    private Button Button00; // Id 00,01,02...-55

    @FXML
    private Button Set1;
    private  boolean selected = false;
    BackgroundFill Red = new BackgroundFill(Color.RED, new CornerRadii(1),
        new Insets(0.0,0.0,0.0,0.0));
    BackgroundFill White = new BackgroundFill(Color.WHITE, new CornerRadii(1),
        new Insets(0.0,0.0,0.0,0.0));

    public void ChangeValue(ActionEvent event){

    Button00.setBackground(new Background(Red));
    selected = true;

    }
    public void SetValue1(ActionEvent event) {
        if (selected==false) {
            // prompt wybieraj cos
        } else {
            for (int i = 0; i < 6;i++) {
                for (int j = 0; j < 6;j++) {
                    if (i==j) {
                        Button00.setText("1");
                        Button00.setBackground(new Background(White));
                        selected = false;
                    }

                }
            }
        }
    }
}
