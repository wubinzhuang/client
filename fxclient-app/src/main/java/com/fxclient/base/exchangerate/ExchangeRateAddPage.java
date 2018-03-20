package com.fxclient.base.exchangerate;

import com.fxclient.app.page.LoginPage;
import com.fxclient.framework.controls.dialog.MessageBox;
import com.fxclient.framework.controls.dialog.PageDialog;
import com.fxclient.framework.controls.overlay.TaskRunner;
import com.fxclient.framework.controls.tableview.TablePager;
import com.fxclient.framework.fxbase.BasePage;
import com.fxclient.framework.util.HttpUtils;
import com.fxclient.sysmanager.util.Apis;
import com.uams.rpc.base.model.ExchangeRateModel;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ExchangeRateAddPage extends BasePage {

    @Override
    public BasePage loadPage() throws Exception {
        return loadPage(File.separator + "static" + File.separator + "page" + File.separator + "base" + File.separator + "exchangeRate" + File.separator + "exchangeRateAddPage.fxml");
    }

    @FXML
    private ComboBox currencySourceList;//源币种

    @FXML
    private ComboBox currencyTargetList;//目标币种

    @FXML
    private TextField exchangDate;//汇率日期

    @FXML
    private TextField unitAmount;//最小单位

    @FXML
    private TextField buyPrice;//买入价

    @FXML
    private TextField sellPrice;//卖出价

    @FXML
    private TextField midPrice;//中间价

    @FXML
    private TextField txtId;

    @FXML
    private TextField currencySources;

    @FXML
    private TextField currencyTargets;


    private ExchangeRatePage exchangeRatePage;

    @Override
    public BasePage getOwner() {
        exchangeRatePage = (ExchangeRatePage) super.getOwner();
        return exchangeRatePage;
    }

    //增加新记录
    @FXML
    void saveExchangeRate(ActionEvent event) {
        Service<String> task = new Service<String>() {
            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() throws Exception {
                        Map<String, String> params = new HashMap<>();

                        if (null != txtId.getText() && !"".equals(txtId.getText())) {
                            params.put("id", txtId.getText());
                        }
                        params.put("exchangDate", exchangDate.getText());
                        params.put("unitAmount", unitAmount.getText());
                        params.put("buyPrice", buyPrice.getText());
                        params.put("sellPrice", sellPrice.getText());
                        params.put("midPrice", midPrice.getText());
                        params.put("currencySourceList", currencySources.getText());
                        params.put("currencyTargetList", currencyTargets.getText());
                        String result = HttpUtils.httpPostToString(Apis.getHttpHost() + Apis.EXCHANGE_ADD_UPDATE, params, true);
                        return result;
                    }
                };
            }

        };
        task.setOnSucceeded((e) -> {
            if ("OK".equals(task.getValue())) {
                getOwner();
                ((PageDialog) getLastPage()).closeDialog();
                exchangeRatePage.pageExchangeRate.goPage();
                MessageBox.showInfo("保存成功！");
            } else {
                MessageBox.showWarning(task.getValue());
            }
        });
        TaskRunner.run(this.getLastPage(), task);
    }


    //初始化加载
    @FXML
    void initialize() {
        getOwner();
        String[] name = new String[]{"请选择","人民币", "美元", "欧元", "港币", "英镑", "日元"};
        String[] currency = new String[]{"","CNY", "USD", "EUR", "HKD", "BP", "JPY"};
        currencySourceList.getItems().addAll(name);
        currencySourceList.getSelectionModel().select(0);
        currencySourceList.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            currencySources.setText(currency[new_val.intValue()].toString());
        });
        currencyTargetList.getItems().addAll(name);
        currencyTargetList.getSelectionModel().select(0);
        currencyTargetList.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            currencyTargets.setText(currency[new_val.intValue()].toString());
        });
    }

    public void init(ExchangeRateModel selectedItem) {
        txtId.setText(selectedItem.getId() == null ? "" : String.valueOf(selectedItem.getId()));
        currencySourceList.setValue(selectedItem.getCurrencySource());
        currencyTargetList.setValue(selectedItem.getCurrencyTarget());
        exchangDate.setText(String.valueOf(selectedItem.getExchangDate()));
        unitAmount.setText(String.valueOf(selectedItem.getUnitAmount()));
        buyPrice.setText(String.valueOf(selectedItem.getBuyPrice()));
        sellPrice.setText(String.valueOf(selectedItem.getSellPrice()));
        midPrice.setText(String.valueOf(selectedItem.getMidPrice()));

    }


}
