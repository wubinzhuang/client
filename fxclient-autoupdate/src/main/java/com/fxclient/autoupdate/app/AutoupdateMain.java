package com.fxclient.autoupdate.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AutoupdateMain extends Application
{
    public static void main(String[] args)
    {
        Application.launch(AutoupdateMain.class);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("自动更新");
        Parent p = FXMLLoader.load(getClass().getResource("Autoupdate.fxml"));
        Scene s = new Scene(p);
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
