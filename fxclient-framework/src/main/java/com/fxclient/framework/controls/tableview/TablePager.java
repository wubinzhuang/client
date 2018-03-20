package com.fxclient.framework.controls.tableview;

import java.io.File;
import java.net.MalformedURLException;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.StrokeType;

import com.fxclient.framework.appcontext.FxAppContext;
import com.fxclient.framework.controls.overlay.TaskRunner;
import com.fxclient.framework.fxbase.BasePage;

public class TablePager<T> extends HBox
{
    private Button               btnFirst;
    
    private Button               btnNext;
    
    private Label                lblDetail;
    
    private TextField            txtCurPage;
    
    private Button               btnLast;
    
    private Button               btnPrevious;
    
    private ChoiceBox<String>    chxPageSize;
    
    private Label                lblTotlePage;
    
    private TableView<T>         tableView;
    
    private StringProperty       curPage   = new SimpleStringProperty("1")
                                           {
                                               protected void invalidated()
                                               {
                                                   disableBtn();
                                               };
                                           };
    
    private StringProperty       pageSize  = new SimpleStringProperty("10")
                                           {
                                               protected void invalidated()
                                               {
                                                   disableBtn();
                                               };
                                           };  ;
    
    private IntegerProperty      totlePage = new SimpleIntegerProperty(1)
                                           {
                                               protected void invalidated()
                                               {
                                                   lblTotlePage.setText("共" + get() + "页");
                                                   disableBtn();
                                               };
                                           };
    
    private BasePage             owner;
    
    private Service<PageData<T>> pageDataTask;
    
    public TablePager()
    {
        this.setMaxHeight(25.0);
        this.setMinHeight(25.0);
        this.setPrefHeight(25.0);
        this.setPrefWidth(500.0);
        try
        {
            this.getStylesheets().add(
                    new File(FxAppContext.getInstance().getBaseDir() + "" + File.separator + "static" + File.separator + "page" + File.separator + "controls"
                            + File.separator + "pager" + File.separator + "Pager.css").toURI().toURL().toExternalForm());
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        this.getStyleClass().add("pagination");
        HBox leftBox = new HBox();
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.setPadding(new Insets(0, 0, 0, 20));
        leftBox.setSpacing(5.0);
        this.getChildren().add(leftBox);
        setHgrow(leftBox, Priority.ALWAYS);
        btnFirst = new Button();
        btnFirst.setPrefSize(16, 16);
        btnFirst.getStyleClass().add("first");
        btnFirst.setOnAction((event) -> {
            goFirst(event);
        });
        leftBox.getChildren().add(btnFirst);
        btnPrevious = new Button();
        btnPrevious.setPrefSize(16, 16);
        btnPrevious.getStyleClass().add("previous");
        btnPrevious.setOnAction((event) -> {
            goPrevious(event);
        });
        leftBox.getChildren().add(btnPrevious);
        Ellipse e1 = new Ellipse();
        e1.setFill(Color.valueOf("#cecece"));
        e1.setStroke(Color.valueOf("#cecece"));
        e1.setRadiusX(1);
        e1.setRadiusY(10);
        e1.setStrokeType(StrokeType.INSIDE);
        leftBox.getChildren().add(e1);
        txtCurPage = new TextField();
        txtCurPage.setPrefSize(33.0, 20.0);
        txtCurPage.setOnKeyReleased((event) -> {
            goPage(event);
        });
        leftBox.getChildren().add(txtCurPage);
        lblTotlePage = new Label("共1页");
        leftBox.getChildren().add(lblTotlePage);
        Ellipse e2 = new Ellipse();
        e2.setFill(Color.valueOf("#cecece"));
        e2.setStroke(Color.valueOf("#cecece"));
        e2.setRadiusX(1);
        e2.setRadiusY(10);
        e2.setStrokeType(StrokeType.INSIDE);
        leftBox.getChildren().add(e2);
        btnNext = new Button();
        btnNext.setPrefSize(16, 16);
        btnNext.getStyleClass().add("next");
        btnNext.setOnAction((event) -> {
            goNext(event);
        });
        leftBox.getChildren().add(btnNext);
        btnLast = new Button();
        btnLast.setPrefSize(16, 16);
        btnLast.getStyleClass().add("last");
        btnLast.setOnAction((event) -> {
            goLast(event);
        });
        leftBox.getChildren().add(btnLast);
        chxPageSize = new ChoiceBox<>();
        chxPageSize.setPrefSize(50, 20);
        chxPageSize.setMaxHeight(20);
        leftBox.getChildren().add(chxPageSize);
        txtCurPage.textProperty().bindBidirectional(curPage);
        chxPageSize.getItems().addAll("10", "20", "50", "100", "200");
        chxPageSize.valueProperty().bindBidirectional(pageSize);
        chxPageSize.valueProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                goPage();
            }
        });
        HBox rightBox = new HBox();
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.setPrefWidth(200.0);
        this.getChildren().add(rightBox);
        lblDetail = new Label("共有0条数据");
        rightBox.getChildren().add(lblDetail);
    }
    
    private void disableBtn()
    {
        if ("1".equals(curPage.get()))
        {
            btnFirst.setDisable(true);
            btnPrevious.setDisable(true);
        }
        else
        {
            btnFirst.setDisable(false);
            btnPrevious.setDisable(false);
        }
        if ((totlePage.get() + "").equals(curPage.get()))
        {
            btnNext.setDisable(true);
            btnLast.setDisable(true);
        }
        else
        {
            btnNext.setDisable(false);
            btnLast.setDisable(false);
        }
    }
    
    void goFirst(ActionEvent event)
    {
        curPage.set("1");
        disableBtn();
        goPage();
    }
    
    void goPrevious(ActionEvent event)
    {
        curPage.set((Integer.parseInt(curPage.get()) - 1) + "");
        disableBtn();
        goPage();
    }
    
    void goPage(KeyEvent event)
    {
        if (event.getCode() == KeyCode.ENTER)
        {
            int page = 1;
            try
            {
                page = Integer.parseInt(txtCurPage.getText());
            }
            catch (Exception e)
            {
            }
            curPage.set(page + "");
            disableBtn();
            goPage();
        }
    }
    
    void goNext(ActionEvent event)
    {
        curPage.set((Integer.parseInt(curPage.get()) + 1) + "");
        disableBtn();
        goPage();
    }
    
    void goLast(ActionEvent event)
    {
        curPage.set(totlePage.get() + "");
        disableBtn();
        goPage();
    }
    
    public void goPage()
    {
        pageDataTask.setOnSucceeded((e) -> {
            Platform.runLater(() -> {
                try
                {
                    PageData<T> pageData = pageDataTask.getValue();
                    tableView.getItems().clear();
                    tableView.getItems().addAll(pageData.getRecords());
                    totlePage.set(pageData.getTotlePage());
                    lblDetail.setText("共有" + pageData.getTotleRecords() + "条数据");
                    curPage.set(pageData.getPage() + "");
                    pageSize.set(pageData.getPageSize() + "");
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            });
        });
        TaskRunner.run(owner, pageDataTask);
    }
    
    public void forTable(TableView<T> tableView, PageDataFactory<T> pageDataFactory, BasePage owner)
    {
        this.tableView = tableView;
        this.pageDataTask = new Service<PageData<T>>()
        {
            @Override
            protected Task<PageData<T>> createTask()
            {
                return new Task<PageData<T>>()
                {
                    @Override
                    protected PageData<T> call() throws Exception
                    {
                        return pageDataFactory.queryPageData(Integer.parseInt(curPage.get()), Integer.parseInt(pageSize.get()));
                    }
                };
            }
        };
        this.owner = owner;
        goPage();
    }
}
