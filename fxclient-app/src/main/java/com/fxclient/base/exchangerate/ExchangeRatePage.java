package com.fxclient.base.exchangerate;

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
import com.uams.rpc.base.model.ExchangeRateModel;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.springframework.stereotype.Component;


import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component(value = "exchangeRateManagerPage")
public class ExchangeRatePage extends BasePage {
    @Override
    public BasePage loadPage() throws Exception {
        return loadPage("/static/page/base/exchangeRate/exchangeRatePage.fxml");

    }

    @FXML
    private ComboBox txtCurrencySource;

    @FXML
    private ComboBox txtCurrencyTarget;


    @FXML
    private TextField txtExchangDate;

    @FXML
    private TextField currencySourceList;

    @FXML
    private TextField currencyTargetList;

    @FXML
    public TableView<ExchangeRateModel> tvwExchangeRate;

    @FXML
    public TablePager<ExchangeRateModel> pageExchangeRate;

    private final Map<String, String> params = new HashMap<>();

    @FXML
    void query(ActionEvent event) {
        pageExchangeRate.goPage();
    }

    @FXML
    void add(ActionEvent event) {
        try {
            BasePage erp = PageDialog.showDialog(new ExchangeRateAddPage().loadPage(), 500, 300, this);
            erp.setLastPage(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 修改
    @FXML
    void modify(ActionEvent event) {
        ExchangeRateModel selectedItem = tvwExchangeRate.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                ExchangeRateAddPage exchangeRateAddPage = (ExchangeRateAddPage) new ExchangeRateAddPage().loadPage();
                PageDialog.showDialog(exchangeRateAddPage, 500, 300, this);
                exchangeRateAddPage.init(selectedItem);
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
        ExchangeRateModel selectedItem = tvwExchangeRate.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Service<String> task = new Service<String>() {
                @Override
                protected Task<String> createTask() {
                    return new Task<String>() {
                        @Override
                        protected String call() throws Exception {
                            Map<String, String> params = new HashMap<>();
                            params.put("id", selectedItem.getId().toString());
                            String result = "";
                            try {
                                result = HttpUtils.httpPostToString(Apis.getHttpHost() + Apis.EXCHANGE_DELETE, params, true);
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
                        pageExchangeRate.goPage();
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

    @FXML
    void active(ActionEvent event) {
        Service<List<ExchangeRateModel>> task = new Service<List<ExchangeRateModel>>() {
            @Override
            protected Task<List<ExchangeRateModel>> createTask() {
                return new Task<List<ExchangeRateModel>>() {
                    @Override
                    protected List<ExchangeRateModel> call() throws Exception {
                        Map<String, String> params = new HashMap<>();
                        List<ExchangeRateModel> exchangeRateModels = null;
                        params.put("currencySourceList", currencySourceList.getText());
                        params.put("currencyTargetList", currencyTargetList.getText());
                        params.put("exchangDate", txtExchangDate.getText());
                        try {
                            exchangeRateModels = HttpUtils.httpGetToObject(Apis.getHttpHost() + Apis.EXCHANGE_EXPORT, params, new TypeReference<List<ExchangeRateModel>>() {
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return exchangeRateModels;
                    }
                };
            }
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                List<ExchangeRateModel> exchangeRateModels = task.getValue();
                if (exchangeRateModels != null && exchangeRateModels.size() > 0) {
                    String title = "汇率管理";
                    String[] rowsName = new String[]{"序号", "汇率日期", "源币种", "目标币种", "买入价", "卖出价", "中间价", "最小单位"};
                    List<Object[]> dataList = new ArrayList<Object[]>();
                    Object[] objs = null;
                    for (int i = 0; i < exchangeRateModels.size() - 1; i++) {
                        ExchangeRateModel man = exchangeRateModels.get(i);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Country(man);
                        objs = new Object[rowsName.length];
                        objs[0] = man.getId();
                        objs[1] = sdf.format(man.getExchangDate());
                        objs[2] = man.getCurrencySource();
                        objs[3] = man.getCurrencyTarget();
                        objs[4] = man.getBuyPrice();
                        objs[5] = man.getSellPrice();
                        objs[6] = man.getMidPrice();
                        objs[7] = man.getUnitAmount();

                        dataList.add(objs);
                    }
                    ExportExcel ex = new ExportExcel(title, rowsName, dataList);
                    FileChooser fileChooser = new FileChooser();
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xls)", "*.xls");
                    fileChooser.getExtensionFilters().add(extFilter);
                    File file = fileChooser.showSaveDialog(FxAppContext.getInstance().getCurrentStage());
                    if (file == null) return;
                    if (file.exists()) {
                        file.delete();
                    }
                    String exportFilePath = file.getAbsolutePath();
                    try {
                        ex.export(exportFilePath);
                        MessageBox.showInfo("导出成功！");
                    } catch (Exception e) {
                        MessageBox.showError("导出失败！");
                    }
                }
            }
        });
        TaskRunner.run(this, task);
    }

    public void Country(ExchangeRateModel exchangeRateModel) {
        switch (exchangeRateModel.getCurrencySource()) {
            case "CNY":
                exchangeRateModel.setCurrencySource("人民币");
                break;
            case "USD":
                exchangeRateModel.setCurrencySource("美元");
                break;
            case "EUR":
                exchangeRateModel.setCurrencySource("欧元");
                break;
            case "HKD":
                exchangeRateModel.setCurrencySource("港币");
                break;
            case "BP":
                exchangeRateModel.setCurrencySource("英镑");
                break;
            case "JPY":
                exchangeRateModel.setCurrencySource("日元");
                break;
        }

        switch (exchangeRateModel.getCurrencyTarget()) {
            case "CNY":
                exchangeRateModel.setCurrencyTarget("人民币");
                break;
            case "USD":
                exchangeRateModel.setCurrencyTarget("美元");
                break;
            case "EUR":
                exchangeRateModel.setCurrencyTarget("欧元");
                break;
            case "HKD":
                exchangeRateModel.setCurrencyTarget("港币");
                break;
            case "BP":
                exchangeRateModel.setCurrencyTarget("英镑");
                break;
            case "JPY":
                exchangeRateModel.setCurrencyTarget("日元");
                break;
        }
    }

    //初始化加载
    @FXML
    void initialize() {
        String[] name = new String[]{"请选择", "人民币", "美元", "欧元", "港币", "英镑", "日元"};
        String[] currency = new String[]{"", "CNY", "USD", "EUR", "HKD", "BP", "JPY"};

        txtCurrencySource.getItems().addAll(name);
        txtCurrencySource.getSelectionModel().select(0);
        txtCurrencySource.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            currencySourceList.setText(currency[new_val.intValue()].toString());
        });

        txtCurrencyTarget.getItems().addAll(name);
        txtCurrencyTarget.getSelectionModel().select(0);
        txtCurrencyTarget.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            currencyTargetList.setText(currency[new_val.intValue()].toString());
        });

        pageExchangeRate.forTable(tvwExchangeRate, (int page, int pageSize) -> {
            params.clear();
            params.put("page", page + "");
            params.put("pageSize", pageSize + "");
            params.put("currencySourceList", currencySourceList.getText());
            params.put("currencyTargetList", currencyTargetList.getText());
            params.put("exchangDate", txtExchangDate.getText());
            PageData<ExchangeRateModel> pageData = null;
            try {
                String pageDataJSON = HttpUtils.httpPostToString(Apis.getHttpHost() + Apis.EXCHANGE_QUERY, params, true);
                pageData = JSON.parseObject(pageDataJSON, new TypeReference<PageData<ExchangeRateModel>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (pageData.getRecords() != null) {
                for (ExchangeRateModel exchangeRateModel : pageData.getRecords()) {
                    Country(exchangeRateModel);
                }
            }
            return pageData;
        }, this);
    }
}
