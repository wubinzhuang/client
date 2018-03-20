package com.fxclient.base.marktinfo;

import com.fxclient.framework.controls.dialog.MessageBox;
import com.fxclient.framework.controls.dialog.PageDialog;
import com.fxclient.framework.controls.overlay.TaskRunner;
import com.fxclient.framework.controls.tableview.TablePager;
import com.fxclient.framework.fxbase.BasePage;
import com.fxclient.framework.util.HttpUtils;
import com.fxclient.sysmanager.util.Apis;
import com.uams.rpc.base.model.MarketInfoModel;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MarketInfoAddPage extends BasePage
{
    @Override
    public BasePage loadPage() throws Exception
    {
        return loadPage(File.separator + "static" + File.separator + "page" + File.separator + "base" + File.separator + "marketInfo" + File.separator + "marketInfoAddPage.fxml");
    }


    @FXML
    private TextField     marketCode;

    @FXML
    private TextField     marketName;

    @FXML
    private TextField     marketFullName;

    @FXML
    private TextField     isStop;

    @FXML
    private TextField     exchangeCode;

    @FXML
    private TextField     countryNo;

    
    @FXML
    private TextField     txtId;


    private  MarketInfoPage marketInfoPage;


    //增加新记录
    @FXML
    void saveMarketInfo(ActionEvent event)
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

                        if(null!=txtId.getText()&&!"".equals(txtId.getText())){
                            params.put("id", txtId.getText());
                        }
                        params.put("marketCode",marketCode.getText());
                        params.put("marketName",marketName.getText());
                        params.put("marketFullName",marketFullName.getText());
                        params.put("isStop",isStop.getText());
                        params.put("exchangeCode",exchangeCode.getText());
                        params.put("countryNo",countryNo.getText());

                        String result = HttpUtils.httpPostToString(Apis.getHttpHost() + Apis.MARKETINFO_ADD_UPDATE, params, true);
                        return result;
                    }
                };
            }

        };
        task.setOnSucceeded((e) -> {
            if ("OK".equals(task.getValue()))
            {
                getOwner();
                ((PageDialog) getLastPage()).closeDialog();
                marketInfoPage.pageMarketInfo.goPage();
                MessageBox.showInfo("保存成功！");

            }
            else
            {
                MessageBox.showWarning(task.getValue());
            }
        });
        TaskRunner.run(this.getLastPage(), task);
    }

    @Override
    public BasePage getOwner() {
        marketInfoPage=(MarketInfoPage)super.getOwner();
        return marketInfoPage;
    }



    //初始化
    
    public void init(MarketInfoModel selectedItem)
    {
        txtId.setText(selectedItem.getMarketCode() == null ? "" : selectedItem.getMarketCode());
        marketCode.setText(selectedItem.getMarketCode());
        marketName.setText(selectedItem.getMarketName());
        marketFullName.setText(selectedItem.getMarketFullName());
        isStop.setText(selectedItem.getIsStop().toString());
        exchangeCode.setText(selectedItem.getExchangeCode());
        countryNo.setText(selectedItem.getCountryNo());
    }
}
