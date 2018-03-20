package com.fxclient.base.marktinfo;

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
import com.uams.rpc.base.model.MarketInfoModel;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
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

@Component(value = "marketInfoPage")
public class MarketInfoPage extends BasePage
{
    @Override
    public BasePage loadPage() throws Exception
    {
        return loadPage("/static/page/base/marketInfo/marketInfoPage.fxml");
    }

    @FXML
    private TextField marketCode;

    @FXML
    private TextField marketName;

    @FXML
    private TextField currencyCountryList;

    @FXML
    private ComboBox countryNo;

    @FXML
    public TableView<MarketInfoModel> tvwMarketInfo;

    @FXML
    public TablePager<MarketInfoModel> pageMarketInfo;
    
    private final Map<String, String> params = new HashMap<>();


    //查询
    @FXML
    void query(ActionEvent event) {
        pageMarketInfo.goPage();
    }


    //增加
    @FXML
    void add(ActionEvent event) {
        try {
            BasePage erp=PageDialog.showDialog(new MarketInfoAddPage().loadPage(), 500, 300, this);
            erp.setLastPage(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 修改
    @FXML
    void modify(ActionEvent event) {
        MarketInfoModel selectedItem = tvwMarketInfo.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                MarketInfoAddPage marketInfoAddPage = (MarketInfoAddPage) new MarketInfoAddPage().loadPage();
                PageDialog.showDialog(marketInfoAddPage, 500, 300, this);
                marketInfoAddPage.init(selectedItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MessageBox.showInfo("请先选择一条数据");
        }
    }

    //删除
    @FXML
    void delete(ActionEvent event) {
        MarketInfoModel selectedItem = tvwMarketInfo.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Service<String> task = new Service<String>() {
                @Override
                protected Task<String> createTask() {
                    return new Task<String>() {
                        @Override
                        protected String call() throws Exception {
                            Map<String, String> params = new HashMap<>();
                            params.put("id",selectedItem.getId().toString());
                            String result = "";
                            try {
                                result = HttpUtils.httpPostToString(Apis.getHttpHost() + Apis.MARKETINFO_DELETE, params,
                                        true);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return result;
                        }
                    };
                }
            };
            task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    if ("OK".equals(task.getValue())) {
                        MessageBox.showInfo("删除成功！");
                        pageMarketInfo.goPage();
                    } else {
                        MessageBox.showInfo("删除失败！");
                    }
                }
            });
            TaskRunner.run(this, task);
        } else {
            MessageBox.showInfo("请先选择一条数据");
        }
    }

    //初始化加载
    @FXML
    void initialize()
    {

        String [] country= new String[]{"中国","香港","美国","英国","新加坡"};
        String [] currency= new String[]{"China","HongKong","UnitedStates","UnitedKingdom","Singapore"};
        countryNo.getItems().addAll(country);


        countryNo.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) ->{
            currencyCountryList.setText(currency[new_val.intValue()].toString());
        });
        pageMarketInfo.forTable(tvwMarketInfo, (int page, int pageSize) -> {
            params.clear();
            params.put("page", page + "");
            params.put("pageSize", pageSize + "");


            params.put("marketCode",marketCode.getText());
            params.put("marketName",marketName.getText());

            params.put("countryNo",currencyCountryList.getText());
            PageData<MarketInfoModel> pageData = null;
            try
            {
                String pageDataJSON = HttpUtils.httpPostToString(Apis.getHttpHost() + Apis.MARKETINFO_QUERY, params, true);
                pageData = JSON.parseObject(pageDataJSON, new TypeReference<PageData<MarketInfoModel>>()
                {
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            if (pageData.getRecords()!=null) {
                for (MarketInfoModel marketInfoModel : pageData.getRecords())
                    switch (marketInfoModel.getCountryNo()) {
                        case "China":
                            marketInfoModel.setCountryNo("中国");
                            break;
                        case "HongKong":
                            marketInfoModel.setCountryNo("香港");
                            break;
                        case "UnitedStates":
                            marketInfoModel.setCountryNo("美国");
                            break;
                        case "UnitedKingdom":
                            marketInfoModel.setCountryNo("英国");
                            break;
                        case "Singapore":
                            marketInfoModel.setCountryNo("新加坡");
                            break;
                    }
            }

            return pageData;


        }, this);
    }


    @FXML
    void active(ActionEvent event)
    {
        Service<List<MarketInfoModel>> task = new Service<List<MarketInfoModel>>()
        {
            @Override
            protected Task<List<MarketInfoModel>> createTask()
            {
                return new Task<List<MarketInfoModel>>()
                {
                    @Override
                    protected List<MarketInfoModel> call() throws Exception
                    {
                        Map<String, String> params = new HashMap<>();
                        List<MarketInfoModel> marketInfoModels = null;
                        try
                        {
                            marketInfoModels = HttpUtils.httpGetToObject(Apis.getHttpHost() + Apis.MARKETINFO_EXPORT, params, new TypeReference<List<MarketInfoModel>>()
                            {
                            });
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        return marketInfoModels;
                    }
                };
            }
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>()
        {
            @Override
            public void handle(WorkerStateEvent event)
            {
                List<MarketInfoModel> users = task.getValue();
                if (users != null && users.size() > 0)
                {
                    String title = "交易市场信息";
                    String[] rowsName = new String[]{"交易市场编码", "交易市场简称", "交易市场全称", "停市标志", "市场代码", "国家或地区"};
                    List<Object[]> dataList = new ArrayList<Object[]>();
                    Object[] objs = null;
                    for (int i = 0; i < users.size(); i++)
                    {
                        MarketInfoModel man = users.get(i);
                        objs = new Object[rowsName.length];
                        objs[0] = i;
                        objs[1] = man.getMarketCode();
                        objs[2] = man.getMarketName();
                        objs[3] = man.getIsStop();
                        objs[4] = man.getExchangeCode();
                        objs[5] = man.getCountryNo();
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



}
