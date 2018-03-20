package com.fxclient.app.page;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import com.fxclient.app.job.MessageJob;
import com.fxclient.app.job.NetTestJob;
import com.fxclient.app.model.MenuItemBo;
import com.fxclient.framework.appcontext.FxAppContext;
import com.fxclient.framework.controls.dialog.MessageBox;
import com.fxclient.framework.controls.overlay.TaskRunner;
import com.fxclient.framework.fxbase.BasePage;

public class MainPage extends BasePage
{
    @FXML
    private ResourceBundle       resources;
    
    @FXML
    private StackPane            root;
    
    @FXML
    private URL                  location;
    
    @FXML
    private TabPane              navTab;
    
    @FXML
    private ImageView            imgLogo;
    
    @FXML
    private Label                lblNotice;
    
    @FXML
    private Button               btnAlert;
    
    @FXML
    private TreeView<MenuItemBo> treeMenu;
    
    @FXML
    private Label                lblNetSetting;
    
    @FXML
    private TextField            txtSearchMenu;
    
    @FXML
    private TextField            txtUserName;
    
    @FXML
    private PasswordField        txtPassWord;
    
    @FXML
    private HBox                 hbLock;
    
    @FXML
    private BorderPane           bpMain;
    
    private List<MenuItemBo>     menus;
    
    private List<MenuItemBo>     curmenus   = new ArrayList<>();
    
    private NetTestJob           netTestjob = new NetTestJob();
    
    @FXML
    void showNetSettings(MouseEvent event)
    {
        TaskRunner.run(this, new Service<String>()
        {
            @Override
            protected Task<String> createTask()
            {
                return new Task<String>()
                {
                    @Override
                    protected String call() throws Exception
                    {
                        Thread.sleep(6000);
                        Platform.runLater(() -> {
                            MessageBox.showError("这个功能没有开发");
                        });
                        return null;
                    }
                };
            }
        });
    }
    
    @FXML
    void searchMenu(KeyEvent event)
    {
        curmenus.clear();
        String filter = txtSearchMenu.getText();
        TreeItem<MenuItemBo> item = treeMenu.getRoot();
        item.getChildren().clear();
        if ("".equals(filter))
        {
            addTreeItem(item, menus);
        }
        else
        {
            filterMenu(menus, filter);
            for (MenuItemBo menuItemBo : curmenus)
            {
                ImageView iv = new ImageView("file:///" + FxAppContext.getInstance().getBaseDir() + "/static/image/" + menuItemBo.getIcon());
                iv.setFitHeight(16);
                iv.setFitWidth(16);
                TreeItem<MenuItemBo> i1 = new TreeItem<>(menuItemBo, iv);
                item.getChildren().add(i1);
            }
        }
    }
    
    private void filterMenu(List<MenuItemBo> menus, String filter)
    {
        for (MenuItemBo menuItemBo : menus)
        {
            if (menuItemBo.getMenuName().contains(filter) && menuItemBo.getMenuType() != MenuItemBo.MENU_TYPE_DIR)
            {
                curmenus.add(menuItemBo);
            }
            else if (menuItemBo.getMenuType() == MenuItemBo.MENU_TYPE_DIR && menuItemBo.getChildMenus() != null)
            {
                filterMenu(menuItemBo.getChildMenus(), filter);
            }
        }
    }
    
    @FXML
    void goAlertMsg(ActionEvent event)
    {
        MessageBox.showInfo("当前有" + netTestjob.alertNumPorperty.get() + "条推送");
    }
    
    @FXML
    void lockClient(ActionEvent event)
    {
        bpMain.setDisable(true);
        hbLock.setVisible(true);
    }
    
    @FXML
    void modifySysSettings(ActionEvent event)
    {
        MessageBox.showConfirm("确定要修改系统设置？", () -> {
            MessageBox.showWarning("这功能压根就没做");
            try
            {
                Integer.parseInt("asd");
            }
            catch (Exception e)
            {
                MessageBox.showException(e, null);
            }
        });
    }
    
    @FXML
    void unlock(ActionEvent event)
    {
        bpMain.setDisable(false);
        hbLock.setVisible(false);
    }
    
    @FXML
    void exit(ActionEvent event)
    {
        Platform.exit();
        System.exit(0);
    }
    
    @SuppressWarnings("rawtypes")
    @FXML
    void menuClicked(MouseEvent event)
    {
        Node node = event.getPickResult().getIntersectedNode();
        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null))
        {
            MenuItemBo bo = ((TreeItem<MenuItemBo>) treeMenu.getSelectionModel().getSelectedItem()).getValue();
            if (MenuItemBo.MENU_TYPE_FXPAGE == bo.getMenuType())
            {
                openNavTab(bo);
            }
            else if (MenuItemBo.MENU_TYPE_NETPAGE == bo.getMenuType())
            {
                openNavTab(bo);
            }
        }
    }
    
    private void openNavTab(MenuItemBo bo)
    {
        boolean isOpened = false;
        for (Tab t : navTab.getTabs())
        {
            if (bo.getMenuId().equals(t.getId()))
            {
                navTab.selectionModelProperty().getValue().select(t);
                isOpened = true;
            }
        }
        if (!isOpened && bo.getMenuType() == MenuItemBo.MENU_TYPE_FXPAGE)
        {
            Parent p;
            try
            {
                p = ((BasePage) FxAppContext.getInstance().getApplicationContext().getBean(bo.getBean())).loadPage().getBox();
                Tab tab = new Tab();
                tab.setText(bo.getMenuName());
                tab.setContent(p);
                tab.setId(bo.getMenuId());
                navTab.getTabs().add(tab);
                navTab.selectionModelProperty().getValue().select(tab);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (!isOpened && bo.getMenuType() == MenuItemBo.MENU_TYPE_NETPAGE)
        {
            Tab tab = new Tab();
            tab.setText(bo.getMenuName());
            WebView webView = new WebView();
            final WebEngine engine = webView.getEngine();
            engine.load(bo.getUrl());
            tab.setContent(webView);
            tab.setId(bo.getMenuId());
            navTab.getTabs().add(tab);
            navTab.selectionModelProperty().getValue().select(tab);
        }
    }
    
    @FXML
    void initialize()
    {
        initMenu();
        initAlert();
        initNetTestJob();
        MessageJob messageJob = new MessageJob();
        messageJob.setMessageStr("系统公告:明天上班，后天上班，星期六上班，星期天上班，星期一上班，星期二上班，星期三上班，星期四上班，星期五上班，国庆上班，元旦上班，过年还是上班");
        messageJob.start();
        lblNotice.textProperty().bind(messageJob.lastValueProperty());
    }
    
    private void initNetTestJob()
    {
        lblNetSetting.textProperty().bind(netTestjob.netStatusPorperty);
        lblNetSetting.textProperty().addListener((observable, oldValue, newValue) -> {
            Circle c = new Circle();
            c.setRadius(10);
            if ("连接成功".equals(newValue))
            {
                c.setFill(Color.GREEN);
            }
            else
            {
                c.setFill(Color.RED);
            }
            lblNetSetting.setGraphic(c);
        });
        netTestjob.start();
    }
    
    private void initAlert()
    {
        AnchorPane root = new AnchorPane();
        Circle c = new Circle();
        c.setRadius(10);
        c.setCenterX(15);
        c.setCenterY(20);
        c.setFill(Color.RED);
        Label l = new Label();
        l.setLayoutX(5);
        l.setLayoutY(12.5);
        l.setPrefWidth(20);
        l.setAlignment(Pos.CENTER);
        l.setTextFill(Color.WHITE);
        l.textProperty().bind(netTestjob.alertNumPorperty);
        root.getChildren().addAll(c, l);
        btnAlert.setGraphic(root);
    }
    
    private void initMenu()
    {
        menus = new ArrayList<>();
        MenuItemBo bo1 = new MenuItemBo("1", "用户维护", "1", 2, 1, "userManagerPage", "", null, "user.png");
        MenuItemBo bo8 = new MenuItemBo("8", "汇率管理", "8", 2, 1, "exchangeRateManagerPage", "", null, "user.png");
        MenuItemBo bo11 = new MenuItemBo("11", "交易市场管理", "1", 2, 1, "marketInfoPage", "", null, "user.png");
        MenuItemBo bo2 = new MenuItemBo("2", "网易", "2", 2, 2, "", "http://www.163.com", null, "org.png");
        List<MenuItemBo> menus1 = new ArrayList<>();
        menus1.add(bo1);
        menus1.add(bo11);
        menus1.add(bo2);
        menus1.add(bo8);
        MenuItemBo bo3 = new MenuItemBo("3", "系统管理", "3", 1, 0, "", "", menus1, "sys.png");
        MenuItemBo bo4 = new MenuItemBo("4", "百度", "4", 2, 2, "", "http://www.baidu.com", null, "baidu.png");
        List<MenuItemBo> menus2 = new ArrayList<>();
        menus2.add(bo4);
        MenuItemBo bo5 = new MenuItemBo("5", "外部页面", "5", 1, 0, "", "", menus2, "other.png");
        String url = "";
        try
        {
            url = new File(FxAppContext.getInstance().getBaseDir() + File.separator + "static" + File.separator + "html" + File.separator + "kline.html").toURI()
                    .toURL().toExternalForm();
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        MenuItemBo bo6 = new MenuItemBo("6", "上证指数", "6", 2, 2, "", url, null, "user.png");
        MenuItemBo bo7 = new MenuItemBo("7", "期货品种", "7", 2, 1, "futureKindManagerPage", url, null, "user.png");
        menus1.add(bo6);
        menus1.add(bo7);
        menus.add(bo3);
        menus.add(bo5);
        TreeItem<MenuItemBo> item = new TreeItem<MenuItemBo>();
        treeMenu.setRoot(item);
        addTreeItem(item, menus);
    }
    
    private void addTreeItem(TreeItem<MenuItemBo> item, List<MenuItemBo> menus)
    {
        for (MenuItemBo menuItemBo : menus)
        {
            ImageView iv = new ImageView("file:///" + FxAppContext.getInstance().getBaseDir() + "/static/image/" + menuItemBo.getIcon());
            iv.setFitHeight(16);
            iv.setFitWidth(16);
            TreeItem<MenuItemBo> i1 = new TreeItem<>(menuItemBo, iv);
            item.getChildren().add(i1);
            if (menuItemBo.getChildMenus() != null)
            {
                addTreeItem(i1, menuItemBo.getChildMenus());
            }
        }
        item.setExpanded(true);
    }
    
    @Override
    public BasePage loadPage() throws Exception
    {
        return loadPage(File.separator + "static" + File.separator + "page" + File.separator + "main" + File.separator + "mainPage.fxml");
    }
}
