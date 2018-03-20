package com.fxclient.framework.controls.dialog;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;

import com.fxclient.framework.appcontext.FxAppContext;

public class MessageBox
{
    public static void showInfo(String message)
    {
        createAlert(AlertType.INFORMATION, message, null).showAndWait();
    }
    
    public static void showWarning(String message)
    {
        createAlert(AlertType.WARNING, message, null).showAndWait();
    }
    
    public static void showError(String message)
    {
        createAlert(AlertType.ERROR, message, null).showAndWait();
    }
    
    public static void showConfirm(String message, Runnable runnable)
    {
        Alert alert = createAlert(AlertType.CONFIRMATION, message, null);
        alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
            if (runnable != null)
            {
                Platform.runLater(runnable);
            }
        });
    }
    
    public static void showInfo(String message, String title)
    {
        createAlert(AlertType.INFORMATION, message, title).showAndWait();
    }
    
    public static void showWarning(String message, String title)
    {
        createAlert(AlertType.WARNING, message, title).showAndWait();
    }
    
    public static void showError(String message, String title)
    {
        createAlert(AlertType.ERROR, message, title).showAndWait();
    }
    
    public static void showConfirm(String message, String title, Runnable runnable)
    {
        Alert alert = createAlert(AlertType.CONFIRMATION, message, title);
        alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
            if (runnable != null)
            {
                Platform.runLater(runnable);
            }
        });
    }
    
    private static Alert createAlert(AlertType type, String message, String title)
    {
        Alert alert = new Alert(type, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(FxAppContext.getInstance().getCurrentStage());
        alert.getDialogPane().setContentText(message);
        alert.getDialogPane().setHeaderText(title);
        return alert;
    }
    
    public static void showException(Throwable th, Runnable runnable)
    {
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setTitle(FxAppContext.getInstance().getLanguage().getString("mainpage.error"));
        final DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContentText(FxAppContext.getInstance().getLanguage().getString("mainpage.errorContent"));
        dialogPane.getButtonTypes().addAll(ButtonType.OK);
        dialogPane.setContentText(th.getMessage());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(FxAppContext.getInstance().getCurrentStage());
        Label label = new Label(FxAppContext.getInstance().getLanguage().getString("mainpage.stacktrace"));
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        th.printStackTrace(pw);
        pw.close();
        TextArea textArea = new TextArea(sw.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane root = new GridPane();
        root.setVisible(false);
        root.setMaxWidth(Double.MAX_VALUE);
        root.add(label, 0, 0);
        root.add(textArea, 0, 1);
        dialogPane.setExpandableContent(root);
        dialog.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
            if (runnable != null)
            {
                Platform.runLater(runnable);
            }
        });
    }
}
