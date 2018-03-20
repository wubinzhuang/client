package com.fxclient.framework.controls.dialog;

import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import com.fxclient.framework.appcontext.FxAppContext;
import com.fxclient.framework.fxbase.BasePage;

public class PageDialog extends BasePage
{


    public static BasePage showDialog(BasePage bp, int width, int height, BasePage owner) throws Exception
    {
        BasePage bpDialog = new PageDialog().loadPage();
        AnchorPane dialog = (AnchorPane) bpDialog.getRoot();
        dialog.getChildren().add(bp.getRoot());
        AnchorPane.setTopAnchor(bp.getRoot(), 30.0);
        AnchorPane.setLeftAnchor(bp.getRoot(), 10.0);
        AnchorPane.setRightAnchor(bp.getRoot(), 10.0);
        AnchorPane.setBottomAnchor(bp.getRoot(), 10.0);
        if (owner.getBox().getHeight() > height + 40)
        {
            dialog.setPrefHeight(height + 40.0);
        }
        else
        {
            dialog.setPrefHeight(owner.getBox().getWidth());
        }
        if (owner.getBox().getWidth() > width + 20)
        {
            dialog.setPrefWidth(width + 20.0);
        }
        else
        {
            dialog.setPrefWidth(owner.getBox().getWidth());
        }
        AnchorPane overlay = new AnchorPane(dialog);
        owner.getBox().getChildren().add(overlay);
        dialog.setLayoutX((owner.getBox().getWidth() - dialog.getPrefWidth()) / 2);
        dialog.setLayoutY((owner.getBox().getHeight() - dialog.getPrefHeight()) / 2);
        bp.setLastPage(bpDialog);
        bp.setOwner(owner);
        owner.getRoot().setDisable(true);
        return bpDialog;
    }

    public void closeDialog()
    {
        StackPane parent = (StackPane) getRoot().getParent().getParent();
        parent.getChildren().remove(getRoot().getParent());
        parent.getChildren().get(0).setDisable(false);
    }
    
    @FXML
    private Label      lblTitleText;
    
    @FXML
    private AnchorPane titleBar;
    
    @FXML
    private Button     btnClose;
    
    @FXML
    private AnchorPane dialogroot;
    
    @FXML
    private Button     btnMin;
    
    @FXML
    private Button     btnMax;
    
    private double     startDragX;
    
    private double     startDragY;
    
    private Point2D    dragAnchor;

    @FXML
    void mouseDragged(MouseEvent me)
    {
        double newTranslateX = startDragX + me.getSceneX() - dragAnchor.getX();
        double newTranslateY = startDragY + me.getSceneY() - dragAnchor.getY();
        dialogroot.setTranslateX(newTranslateX);
        dialogroot.setTranslateY(newTranslateY);
    }
    
    @FXML
    void mousePressed(MouseEvent me)
    {
        dialogroot.toFront();
        startDragX = dialogroot.getTranslateX();
        startDragY = dialogroot.getTranslateY();
        dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
    }
    
    @FXML
    void mouseReleased(MouseEvent event)
    {
        if (dialogroot.getTranslateX() < (10) && dialogroot.getTranslateX() > (-10) && dialogroot.getTranslateY() < (10) && dialogroot.getTranslateY() > (-10))
        {
            dialogroot.setTranslateX(0);
            dialogroot.setTranslateY(0);
        }
    }
    
    @FXML
    void closeDialog(ActionEvent event)
    {
        closeDialog();
    }
    
    @FXML
    void initialize()
    {
        btnMin.setVisible(false);
        btnMax.setVisible(false);
    }
    
    @Override
    public BasePage loadPage() throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(FxAppContext.getInstance().getLanguage());
        loader.setLocation(new URL("file:///" + FxAppContext.getInstance().getBaseDir() + "/static/page/controls/pagedialog/PageDialog.fxml"));
        Parent p = loader.load();
        BasePage bp = loader.getController();
        bp.setRoot(p);
        return bp;
    }
}
