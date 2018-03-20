package com.fxclient.app.page;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import com.alibaba.fastjson.TypeReference;
import com.fxclient.app.model.NetSettingBo;
import com.fxclient.app.model.UserInfo;
import com.fxclient.app.model.Version;
import com.fxclient.app.util.Apis;
import com.fxclient.app.util.NetSettingUtil;
import com.fxclient.app.util.RunCmd;
import com.fxclient.app.viewmodel.NetSettingVo;
import com.fxclient.framework.appcontext.FxAppContext;
import com.fxclient.framework.controls.dialog.MessageBox;
import com.fxclient.framework.controls.dialog.PageDialog;
import com.fxclient.framework.controls.overlay.TaskRunner;
import com.fxclient.framework.controls.tableview.RadioButtonTableCell;
import com.fxclient.framework.fxbase.BasePage;
import com.fxclient.framework.util.AppSettings;
import com.fxclient.framework.util.HttpUtils;
import com.fxclient.framework.util.MD5Utils;
import com.fxclient.framework.util.UpdateSettings;
import com.fxclient.framework.vo.SelectedVoBase;

public class LoginPage extends BasePage
{
    @FXML
    private TextField                            txtUsername;
    
    @FXML
    private Button                               btnLogin;
    
    @FXML
    private Hyperlink                            hlNetSet;
    
    @FXML
    private Text                                 lblSystemName;
    
    @FXML
    private ImageView                            imgSystemLogo;
    
    @FXML
    private CheckBox                             cbxRemember;
    
    @FXML
    private PasswordField                        txtPassword;
    
    @FXML
    private TableView<NetSettingVo>              tvwNetSettings;
    
    @FXML
    private TableColumn<NetSettingVo, String>    port;
    
    @FXML
    private TableColumn<NetSettingVo, String>    ip;
    
    @FXML
    private TableColumn<SelectedVoBase, Boolean> selected;
    
    @FXML
    private TableColumn<NetSettingVo, String>    colTest;
    
    @Override
    public BasePage loadPage() throws Exception
    {
        return loadPage(File.separator + "static" + File.separator + "page" + File.separator + "login" + File.separator + "login.fxml");
    }
    
    @FXML
    void doLogin(ActionEvent event)
    {
        final String userName = txtUsername.getText();
        final String password = MD5Utils.getMd5(txtPassword.getText());
        if (cbxRemember.isSelected())
        {
            AppSettings.getInstance().setProperty("rememberMe", "true");
            AppSettings.getInstance().setProperty("userName", userName);
            AppSettings.getInstance().setProperty("password", txtPassword.getText());
            AppSettings.getInstance().save();
        }
        Service<UserInfo> task = new Service<UserInfo>()
        {
            @Override
            protected Task<UserInfo> createTask()
            {
                return new Task<UserInfo>()
                {
                    @Override
                    protected UserInfo call() throws Exception
                    {
                        Map<String, String> params = new HashMap<>();
                        params.put("userName", userName);
                        params.put("password", password);
//                        UserInfo u = HttpUtils.httpPostToObject(Apis.getHttpHost() + Apis.DOLOGIN, params, new TypeReference<UserInfo>()
//                        {
//                        }, true);
                        UserInfo u=new  UserInfo();
                        u.setUserName("admin");
                        u.setPassword("123456");
                        return u;
                    }
                };
            }
        };
        task.setOnSucceeded((e) -> {
            if (task.getValue().getUserName() == null)
            {
                MessageBox.showError("用户名密码错误");
            }
            else
            {
                Stage currentStage = FxAppContext.getInstance().getCurrentStage();
                Stage mainStage = new Stage();
                mainStage.setTitle(FxAppContext.getInstance().getLanguage().getString("mainpage.SystemName"));
                try
                {
                    BasePage bp = new MainPage().loadPage();
                    Scene s = new Scene(bp.getBox());
                    mainStage.setScene(s);
                    mainStage.centerOnScreen();
                    mainStage.setMaximized(true);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                currentStage.close();
                FxAppContext.getInstance().setCurrentStage(mainStage);
                mainStage.show();
            }
        });
        TaskRunner.run(this, task);
    }
    
    @FXML
    void setNet(ActionEvent event)
    {
        Stage currentStage = FxAppContext.getInstance().getCurrentStage();
        if (currentStage.getHeight() == 326)
        {
            currentStage.setResizable(true);
            currentStage.setHeight(600);
            hlNetSet.setText(FxAppContext.getInstance().getLanguage().getString("loginStage.netSetting.close"));
            currentStage.setResizable(false);
        }
        else
        {
            currentStage.setResizable(true);
            currentStage.setHeight(326);
            hlNetSet.setText(FxAppContext.getInstance().getLanguage().getString("loginStage.netSetting"));
            currentStage.setResizable(false);
        }
    }
    
    @FXML
    void add(ActionEvent event)
    {
        NetSettingDialogPage netsetting = new NetSettingDialogPage();
        try
        {
            BasePage bpDialog = PageDialog.showDialog(netsetting.loadPage(), 320, 160, this);
            bpDialog.setLastPage(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @FXML
    void del(ActionEvent event)
    {
        tvwNetSettings.getItems().remove(tvwNetSettings.getSelectionModel().getSelectedItem());
        saveNetSettings();
    }
    
    @FXML
    void modify(ActionEvent event)
    {
        NetSettingVo selectedItem = tvwNetSettings.getSelectionModel().getSelectedItem();
        try
        {
            NetSettingDialogPage netsetting = (NetSettingDialogPage) new NetSettingDialogPage().loadPage();
            BasePage bpDialog = PageDialog.showDialog(netsetting, 320, 160, this);
            bpDialog.setLastPage(this);
            netsetting.modify(selectedItem);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @FXML
    void initialize()
    {
        String rememberMe = AppSettings.getInstance().getProperty("rememberMe");
        if ("true".equals(rememberMe))
        {
            String userName = AppSettings.getInstance().getProperty("userName");
            String password = AppSettings.getInstance().getProperty("password");
            txtUsername.setText(userName);
            txtPassword.setText(password);
            cbxRemember.setSelected(true);
        }
        initTable();
        initNetSettings();
        Platform.runLater(() -> {
            Map<String, String> params = new HashMap<String, String>();
            String curVersion = AppSettings.getInstance().getProperty("curVersion");
            params.put("curVersion", curVersion);
            params.put("r", System.currentTimeMillis() + "");
            try
            {
                Version v = HttpUtils.httpGetToObject(Apis.getHttpHost() + Apis.CHECK_VERSION, params, new TypeReference<Version>()
                {
                });
                if (!curVersion.equals(v.getLastVersion()))
                {
                    UpdateSettings.getInstance().setProperty("autoupdate.url", v.getUrl());
                    // UpdateSettings.getInstance().setProperty("autoupdate.url", "http://localhost:8080/fxclient/version/download.do");
                    UpdateSettings.getInstance().setProperty("autoupdate.versioninfo", v.getVersionInfo());
                    UpdateSettings.getInstance().setProperty("autoupdate.version", v.getLastVersion());
                    UpdateSettings.getInstance().save();
                    MessageBox.showConfirm("检测到新版本，是否更新", () -> {
                        RunCmd.runCmd("java -jar autoupdate.jar");
                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        Platform.exit();
                    });
                }
            }
            catch (Exception e1)
            {
                // MessageBox.showError("获取更新信息失败！");
            }
        });
    }
    
    private void initNetSettings()
    {
        List<NetSettingBo> bos = NetSettingUtil.Load();
        if (null == bos) return;
        for (NetSettingBo netSettingBo : bos)
        {
            NetSettingVo vo = new NetSettingVo();
            vo.setIp(netSettingBo.getIp());
            vo.setPort(netSettingBo.getPort());
            vo.setSelected(netSettingBo.isSelect());
            tvwNetSettings.getItems().add(vo);
        }
    }
    
    private void initTable()
    {
        selected.setCellValueFactory(new PropertyValueFactory<SelectedVoBase, Boolean>("selected"));
        final ToggleGroup g = new ToggleGroup();
        g.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue)
            {
                saveNetSettings();
            }
        });
        selected.setCellFactory(RadioButtonTableCell.getCallBack(g));
        ip.setCellValueFactory(new PropertyValueFactory<NetSettingVo, String>("ip"));
        port.setCellValueFactory(new PropertyValueFactory<NetSettingVo, String>("port"));
        colTest.setCellValueFactory(new PropertyValueFactory<NetSettingVo, String>("test"));
        colTest.setCellFactory(new Callback<TableColumn<NetSettingVo, String>, TableCell<NetSettingVo, String>>()
        {
            @Override
            public TableCell<NetSettingVo, String> call(TableColumn<NetSettingVo, String> param)
            {
                return new TableCell<NetSettingVo, String>()
                {
                    @Override
                    protected void updateItem(String item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if (!empty)
                        {
                            Button b = new Button("测试");
                            b.setOnAction((e) -> {
                                NetSettingVo netSettingVo = (NetSettingVo) getTableRow().getItem();
                                Service<String> s = new Service<String>()
                                {
                                    @Override
                                    protected Task<String> createTask()
                                    {
                                        return new Task<String>()
                                        {
                                            @Override
                                            protected String call() throws Exception
                                            {
                                                try
                                                {
                                                    String test = HttpUtils.httpGetToString("http://" + netSettingVo.getIp() + ":" + netSettingVo.getPort() + "/"
                                                            + Apis.TEST, null);
                                                    Platform.runLater(() -> {
                                                        if ("OK".equals(test))
                                                        {
                                                            netSettingVo.setTest("连接成功！");
                                                        }
                                                        else
                                                        {
                                                            netSettingVo.setTest("连接失败");
                                                        }
                                                    });
                                                }
                                                catch (Exception e1)
                                                {
                                                    Platform.runLater(() -> {
                                                        netSettingVo.setTest("连接失败");
                                                    });
                                                }
                                                return null;
                                            }
                                        };
                                    }
                                };
                                TaskRunner.run(LoginPage.this, s);
                            });
                            setGraphic(b);
                            setText(item);
                        }
                    }
                };
            }
        });
    }
    
    public void saveNetSetting(String ip, String port, boolean modify)
    {
        NetSettingVo vo2 = new NetSettingVo();
        vo2.setSelected(false);
        vo2.setIp(ip);
        vo2.setPort(port);
        tvwNetSettings.getItems().add(vo2);
        saveNetSettings();
    }
    
    private void saveNetSettings()
    {
        ObservableList<NetSettingVo> vos = tvwNetSettings.getItems();
        List<NetSettingBo> bos = new ArrayList<>();
        int i = 1;
        for (NetSettingVo netSettingVo : vos)
        {
            NetSettingBo bo = new NetSettingBo();
            bo.setId(i + "");
            bo.setIp(netSettingVo.getIp());
            bo.setPort(netSettingVo.getPort());
            bo.setSelect(netSettingVo.getSelected());
            bos.add(bo);
            i++;
            if (netSettingVo.getSelected())
            {
                AppSettings.getInstance().setProperty("serverip", netSettingVo.getIp());
                AppSettings.getInstance().setProperty("serverPort", netSettingVo.getPort());
                AppSettings.getInstance().save();
            }
        }
        NetSettingUtil.SaveNetSettings(bos);
    }
    
    public void saveNetSetting(NetSettingVo vo)
    {
        ObservableList<NetSettingVo> items = tvwNetSettings.getItems();
        if (!items.contains(vo))
        {
            items.add(vo);
        }
        saveNetSettings();
    }
}
