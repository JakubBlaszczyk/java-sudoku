package com.project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import  javafx.scene.control.Button;

public class Controller {
    @FXML
    private Button Button00; // Id 00,01,02...-55
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
