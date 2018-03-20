package com.fxclient.base.futurekind;

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
import com.uams.rpc.base.model.FutureKindModel;
import com.uams.rpc.base.model.MarketInfoModel;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(value = "futureKindManagerPage")
public class FutureKindPage extends BasePage {
    @Override
    public BasePage loadPage() throws Exception
    {
        return loadPage("/static/page/base/futureKind/futureKindPage.fxml");
    }

/*    @FXML
    private TextField marketName;*/

    @FXML
    private ChoiceBox marketNameList;
    @FXML
    private TextField kindCode;

    @FXML
    private TextField kindName;

    @FXML
    private TextField marketId;

    @FXML
    public TableView<FutureKindModel> tvwUserInfo;

    @FXML
    public TablePager<FutureKindModel> pageFutureKindInfo;

    private final Map<String, String> params = new HashMap<>();

    @FXML
    void query(ActionEvent event)
    {
        pageFutureKindInfo.goPage();
    }

    @FXML
    void add(ActionEvent event)
    {
        FutureKindAddPage fkap=new FutureKindAddPage();
        try
        {
            BasePage bpDialog =PageDialog.showDialog(new FutureKindAddPage().loadPage(), 500, 200, this);
            bpDialog.setLastPage(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void modify(ActionEvent event)
    {
        FutureKindModel selectedItem = tvwUserInfo.getSelectionModel().getSelectedItem();
        if (selectedItem != null)
        {
            try
            {
                FutureKindAddPage FutureKindAddPage = (FutureKindAddPage) new FutureKindAddPage().loadPage();
                PageDialog.showDialog(FutureKindAddPage, 500, 200, this);
                FutureKindAddPage.init(selectedItem);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            MessageBox.showInfo("请先选择一条数据");
            initialize();
        }
    }

    @FXML
    void delete(ActionEvent event)
    {
        FutureKindModel selectedItem = tvwUserInfo.getSelectionModel().getSelectedItem();
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
                            params.put("id",selectedItem.getId().toString());
                            try
                            {
                                params.put("marketId",selectedItem.getMarketId().toString());
                                result = HttpUtils.httpPostToString(Apis.getHttpHost() + Apis.FutureKind_DELETE, params,true);
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
                        pageFutureKindInfo.goPage();
                        initialize();
                    }
                    else
                    {
                        MessageBox.showInfo("删除失败！");
                        initialize();
                    }
                }
            });
            TaskRunner.run(this, task);
        }
        else
        {
            MessageBox.showInfo("请先选择一条数据");
            initialize();
        }
    }

    @FXML
    void active(ActionEvent event)
    {
        Service<List<FutureKindModel>> task = new Service<List<FutureKindModel>>()
        {
            @Override
            protected Task<List<FutureKindModel>> createTask()
            {
                return new Task<List<FutureKindModel>>()
                {
                    @Override
                    protected List<FutureKindModel> call() throws Exception
                    {
                        Map<String, String> params = new HashMap<>();
                        List<FutureKindModel> futureKindModel = null;
                        int page;
                        int pageSize;
                        params.clear();
                        /*params.put("page", page + "");
                        params.put("pageSize", pageSize + "");*/
                        params.put("marketId", marketId.getText());
                        params.put("kindCode", kindCode.getText());
                        params.put("kindName", kindName.getText());
                        try
                        {
                            futureKindModel = HttpUtils.httpGetToObject(Apis.getHttpHost() + Apis.FutureKind_EXPORT, params, new TypeReference<List<FutureKindModel>>()
                            {
                            });
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        return futureKindModel;
                    }
                };
            }
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>()
        {
            @Override
            public void handle(WorkerStateEvent event)
            {
                List<FutureKindModel> futureKindModel = task.getValue();
                if (futureKindModel != null && futureKindModel.size() > 0)
                {
                    String title = "期货品种";
                    String[] rowsName = new String[]{"序号", "交易市场", "期货品种编码", "期货品种名称"};
                    List<Object[]> dataList = new ArrayList<Object[]>();
                    Object[] objs = null;
                    for (int i = 0; i < futureKindModel.size(); i++)
                    {
                        FutureKindModel fkm = futureKindModel.get(i);
                        objs = new Object[rowsName.length];
                        objs[0] = i;
                        objs[1] = fkm.getMarketName();
                        objs[2] = fkm.getKindCode();
                        objs[3] = fkm.getKindName();
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
        try
        {
            List<MarketInfoModel> miList=new ArrayList<MarketInfoModel>();
            String markInfoModelList = HttpUtils.httpPostToString(Apis.getHttpHost() + Apis.FutureKind_all, params, true);
            miList = JSON.parseObject(markInfoModelList, new TypeReference<List<MarketInfoModel>>()
            {
            });
            final String [] name= new String[miList.toArray().length+1];
            name[0]="请选择交易市场";
            final Long [] marketIdHidden= new Long[miList.toArray().length+1];
            marketIdHidden[0]=Long.valueOf(0);
            for (int i=0;i<miList.toArray().length;i++){
                name[i+1]=miList.subList(0,miList.toArray().length).get(i).getMarketName();
            }
            for (int i=0;i<miList.toArray().length;i++){
                marketIdHidden[i+1]=miList.subList(0,miList.toArray().length).get(i).getId();
            }
            if(0==marketNameList.getItems().size()) {
                marketNameList.getItems().addAll(name);
                marketNameList.getSelectionModel().select(0);
                marketNameList.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                    marketId.setText(marketIdHidden[new_val.intValue()].toString());
                });
            }else{
                marketNameList.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                    marketId.setText(marketIdHidden[new_val.intValue()].toString());
                });
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        initFutureKindInfo();
    }

    private void initFutureKindInfo()
    {
        pageFutureKindInfo.forTable(tvwUserInfo, (int page, int pageSize) -> {
            params.clear();
            params.put("page", page + "");
            params.put("pageSize", pageSize + "");
            params.put("marketId", marketId.getText());
            params.put("kindCode", kindCode.getText());
            params.put("kindName", kindName.getText());
            PageData<FutureKindModel> pageData = null;
            try
            {
                String pageDataJSON = HttpUtils.httpPostToString(Apis.getHttpHost() + Apis.FutureKind_QUERY, params, true);
                pageData = JSON.parseObject(pageDataJSON, new TypeReference<PageData<FutureKindModel>>()
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
