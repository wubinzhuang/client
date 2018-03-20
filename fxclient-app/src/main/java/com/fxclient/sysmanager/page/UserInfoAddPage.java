package com.fxclient.sysmanager.page;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import com.fxclient.framework.controls.dialog.MessageBox;
import com.fxclient.framework.controls.dialog.PageDialog;
import com.fxclient.framework.controls.overlay.TaskRunner;
import com.fxclient.framework.fxbase.BasePage;
import com.fxclient.framework.util.HttpUtils;
import com.fxclient.sysmanager.util.Apis;
import com.fxclient.sysmanager.vo.UserInfo;

public class UserInfoAddPage extends BasePage
{
    @Override
    public BasePage loadPage() throws Exception
    {
        return loadPage(File.separator + "static" + File.separator + "page" + File.separator + "sysmanager" + File.separator + "userInfoAddPage.fxml");
    }
    
    @FXML
    private TextField     txtEmpName;
    
    @FXML
    private TextField     txtUserName;
    
    @FXML
    private TextField     txtTel;
    
    @FXML
    private TextField     txtEmpCode;
    
    @FXML
    private TextField     txtId;
    
    @FXML
    private ToggleGroup   sexGroup;
    
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private RadioButton   rbMan;
    
    @FXML
    private RadioButton   rbFm;


    //添加
    @FXML
    void saveUserInfo(ActionEvent event)
    {
        Service<String> task = new Service<String>()
        {
            @Override
            protected Task<String> createTask()
            {
                return new Task<String>()
                {
                    @Override
                    protected String call() throws Exception
                    {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", txtId.getText());
                        params.put("empName", txtEmpName.getText());
                        params.put("userName", txtUserName.getText());
                        params.put("tel", txtTel.getText());
                        params.put("empCode", txtEmpCode.getText());
                        params.put("sex", ((RadioButton) sexGroup.getSelectedToggle()).getText());
                        String result = HttpUtils.httpPostToString(Apis.getHttpHost() + Apis.ADD_UPDATE, params, true);
                        return result;
                    }
                };
            }
        };
        task.setOnSucceeded((e) -> {
            if ("OK".equals(task.getValue()))
            {
                ((PageDialog) getLastPage()).closeDialog();
                MessageBox.showInfo("保存成功！");
            }
            else
            {
                MessageBox.showWarning(task.getValue());
            }
        });
        TaskRunner.run(this.getLastPage(), task);
    }
    
    @FXML
    void initialize()
    {

    }

    //初始化方法
    
    public void init(UserInfo selectedItem)
    {
        txtId.setText(selectedItem.getId() == null ? "" : selectedItem.getId());
        txtEmpCode.setText(selectedItem.getEmpCode());
        txtUserName.setText(selectedItem.getUserName());
        txtEmpName.setText(selectedItem.getEmpName());
        txtPassword.setText(selectedItem.getPassword());
        txtTel.setText(selectedItem.getTel());
        sexGroup.selectToggle("男".equals(selectedItem.getSex()) ? rbMan : rbFm);
    }
}
