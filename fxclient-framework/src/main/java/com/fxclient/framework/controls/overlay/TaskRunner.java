package com.fxclient.framework.controls.overlay;

import javafx.concurrent.Service;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

import com.fxclient.framework.fxbase.BasePage;

public class TaskRunner
{
    public static <T> void run(BasePage owner, Service<T> task)
    {
        if (owner.getBox() != null)
        {
            ProgressIndicator p = new ProgressIndicator(-1);
            Label l = new Label("请稍候……");
            VBox box = new VBox(p, l);
            box.setMinSize(200, 100);
            box.setMaxSize(200, 100);
            box.setStyle("-fx-background-color:white;");
            box.setAlignment(Pos.CENTER);
            VBox box1 = new VBox(box);
            box1.setAlignment(Pos.CENTER);
            owner.getBox().getChildren().get(0).setDisable(true);
            owner.getBox().getChildren().add(box1);
            task.runningProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue)
                {
                    owner.getBox().getChildren().get(0).setDisable(false);
                    owner.getBox().getChildren().remove(box1);
                }
            });
        }
        task.restart();
    }
}
