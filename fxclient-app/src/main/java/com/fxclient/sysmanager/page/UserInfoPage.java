package com.fxclient.sysmanager.page;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fxclient.framework.appcontext.FxAppContext;
import com.fxclient.framework.controls.dialog.MessageBox;
import com.fxclient.framework.controls.dialog.PageDialog;
import com.fxclient.framework.controls.overlay.TaskRunner;
import com.fxclient.framework.controls.tableview.PageData;
import com.fxclient.framework.controls.tableview.TablePager;
import com.fxclient.framework.fxbase.BasePage;
import com.fxclient.framework.util.ExportExcel;
import com.fxclient.framework.util.HttpUtils;
import com.fxclient.sysmanager.util.Apis;
import com.fxclient.sysmanager.vo.UserInfo;

@Component(value = "userManagerPage")
public class UserInfoPage extends BasePage
{
    @Override
    public BasePage loadPage() throws Exception
    {
        return loadPage("/static/page/sysmanager/userInfoPage.fxml");
    }
    
    @FXML
    private TextField                 txtEmpName;
    
    @FXML
    private TextField                 txtUserName;
    
    @FXML
    private TextField                 txtUserCode;
    
    @FXML
    private TableView<UserInfo>       tvwUserInfo;
    
    @FXML
    private TablePager<UserInfo>      pageUserInfo;
    
    private final Map<String, String> params = new HashMap<>();
    
    @FXML
    void query(ActionEvent event)
    {
        pageUserInfo.goPage();
    }
    
    @FXML
    void add(ActionEvent event)
    {
        try
        {
            PageDialog.showDialog(new UserInfoAddPage().loadPage(), 500, 200, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @FXML
    void modify(ActionEvent event)
    {
        UserInfo selectedItem = tvwUserInfo.getSelectionModel().getSelectedItem();
        if (selectedItem != null)
        {
            try
            {
                UserInfoAddPage userInfoAddPage = (UserInfoAddPage) new UserInfoAddPage().loadPage();
                PageDialog.showDialog(userInfoAddPage, 500, 200, this);
                userInfoAddPage.init(selectedItem);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            MessageBox.showInfo("请先选择一条数据");
        }
    }
    
    @FXML
    void delete(ActionEvent event)
    {
        UserInfo selectedItem = tvwUserInfo.getSelectionModel().getSelectedItem();
        if (selectedItem != null)
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
                            String result = "";
                            try
                            {
                                result = HttpUtils.httpGetToString(Apis.getHttpHost() + Apis.DELETE, params);
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            return result;
                        }
                    };
                }
            };
            task.setOnSucceeded(new EventHandler<WorkerStateEvent>()
            {
                @Override
                public void handle(WorkerStateEvent event)
                {
                    if ("OK".equals(task.getValue()))
                    {
                        MessageBox.showInfo("删除成功！");
                        pageUserInfo.goPage();
                    }
                    else
                    {
                        MessageBox.showInfo("删除失败！");
                    }
                }
            });
            TaskRunner.run(this, task);
        }
        else
        {
            MessageBox.showInfo("请先选择一条数据");
        }
    }
    
    @FXML
    void active(ActionEvent event)
    {
        Service<List<UserInfo>> task = new Service<List<UserInfo>>()
        {
            @Override
            protected Task<List<UserInfo>> createTask()
            {
                return new Task<List<UserInfo>>()
                {
                    @Override
                    protected List<UserInfo> call() throws Exception
                    {
                        Map<String, String> params = new HashMap<>();
                        List<UserInfo> users = null;
                        try
                        {
                            users = HttpUtils.httpGetToObject(Apis.getHttpHost() + Apis.EXPORT, params, new TypeReference<List<UserInfo>>()
                            {
                            });
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        return users;
                    }
                };
            }
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>()
        {
            @Override
            public void handle(WorkerStateEvent event)
            {
                List<UserInfo> users = task.getValue();
                if (users != null && users.size() > 0)
                {
                    String title = "用户信息";
                    String[] rowsName = new String[]{"序号", "用户名", "用户姓名", "工号", "性别", "电话"};
                    List<Object[]> dataList = new ArrayList<Object[]>();
                    Object[] objs = null;
                    for (int i = 0; i < users.size(); i++)
                    {
                        UserInfo man = users.get(i);
                        objs = new Object[rowsName.length];
                        objs[0] = i;
                        objs[1] = man.getUserName();
                        objs[2] = man.getEmpName();
                        objs[3] = man.getEmpCode();
                        objs[4] = man.getSex();
                        objs[5] = man.getTel();
                        dataList.add(objs);
                    }
                    ExportExcel ex = new ExportExcel(title, rowsName, dataList);
                    FileChooser fileChooser = new FileChooser();
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xls)", "*.xls");
                    fileChooser.getExtensionFilters().add(extFilter);
                    File file = fileChooser.showSaveDialog(FxAppContext.getInstance().getCurrentStage());
                    if (file == null) return;
                    if (file.exists())
                    {
                        file.delete();
                    }
                    String exportFilePath = file.getAbsolutePath();
                    try
                    {
                        ex.export(exportFilePath);
                        MessageBox.showInfo("导出成功！");
                    }
                    catch (Exception e)
                    {
                        MessageBox.showError("导出失败！");
                    }
                }
            }
        });
        TaskRunner.run(this, task);
    }
    
    @FXML
    void initialize()
    {
        pageUserInfo.forTable(tvwUserInfo, (int page, int pageSize) -> {
            params.clear();
            params.put("page", page + "");
            params.put("pageSize", pageSize + "");
            params.put("userName", txtUserName.getText());
            params.put("empName", txtEmpName.getText());
            params.put("empCode", txtUserCode.getText());
            PageData<UserInfo> pageData = null;
            try
            {
                String pageDataJSON = HttpUtils.httpPostToString(Apis.getHttpHost() + Apis.QUERY, params, true);
                pageData = JSON.parseObject(pageDataJSON, new TypeReference<PageData<UserInfo>>()
                {
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return pageData;
        }, this);
    }
}
