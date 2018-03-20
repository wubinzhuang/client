package com.fxclient.server.base.controller;


import com.uams.rpc.base.model.ExchangeRateModel;
import com.uams.rpc.base.service.ExchangeRateService;
import com.fxclient.server.app.model.PageData;
import com.uams.tools.enums.CommonEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping(value = "fxclient/exchangeRateManager")
public class ExchangeRateController {


    @Autowired(required = false)
    private ExchangeRateService exchangeRateService;

    @ResponseBody
    @RequestMapping(value = "query.do", method = RequestMethod.POST)
    public PageData<ExchangeRateModel> doLogin(HttpServletRequest request) {

        ExchangeRateModel exchangeRateModel = new ExchangeRateModel();
        int page = Integer.parseInt(request.getParameter("page"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));

        String currencySourceList = request.getParameter("currencySourceList");
        String currencyTargetList = request.getParameter("currencyTargetList");
        String exchangDate = request.getParameter("exchangDate");
        Date exc = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            exc = sdf.parse(exchangDate);
        } catch (ParseException e) {

        }

        exchangeRateModel.setCurrencySource(currencySourceList);
        exchangeRateModel.setCurrencyTarget(currencyTargetList);
        exchangeRateModel.setExchangDate(exc);

        List<ExchangeRateModel> list = exchangeRateService.findList(exchangeRateModel);


        int TotalPage=(list.size()%pageSize==0)?(list.size()/pageSize):(list.size()/pageSize+1);
        PageData<ExchangeRateModel> pageData = new PageData<>();
        pageData.setPage(page);
        pageData.setPageSize(pageSize);

        pageData.setTotlePage(TotalPage);
        pageData.setTotleRecords(list.size());
        if (page != TotalPage) {
            pageData.setRecords(list.subList((page - 1) * pageSize, page * pageSize));
        } else {
            pageData.setRecords(list.subList((page - 1) * pageSize, list.size()));
        }

        return pageData;
    }
    /*public List<ExchangeRateModel> list(ExchangeRateModel exchangeRateModel) throws BusinessException
    {

        List<ExchangeRateModel> exchangeRateModels=exchangeRateService.findList(exchangeRateModel);

        return exchangeRateModels;
    }*/

    @ResponseBody
    @RequestMapping(value = "addOrUpdate", method = RequestMethod.POST)
    public String addOrUpdate(HttpServletRequest request) {

        String id = request.getParameter("id");
        String currencySourceList = request.getParameter("currencySourceList");
        String currencyTargetList = request.getParameter("currencyTargetList");
        String exchangDate = request.getParameter("exchangDate");
        String unitAmount = request.getParameter("unitAmount");
        String buyPrice = request.getParameter("buyPrice");

        String sellPrice = request.getParameter("sellPrice");
        String midPrice = request.getParameter("midPrice");

        Date exc = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            exc = sdf.parse(exchangDate);
        } catch (ParseException e) {

        }

        ExchangeRateModel model = new ExchangeRateModel();
        if (null != id && !"".equals(id)) {
            model.setId(Long.valueOf(id));
        }
        model.setCurrencySource(currencySourceList);
        model.setCurrencyTarget(currencyTargetList);
        model.setExchangDate(exc);
        model.setUnitAmount(Long.valueOf(unitAmount));
        model.setBuyPrice(new BigDecimal(buyPrice));
        model.setSellPrice(new BigDecimal(sellPrice));
        model.setMidPrice(new BigDecimal(midPrice));
        model.setUpdateDate(new Date());

        exchangeRateService.save(model);

        return "OK";
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String delete(HttpServletRequest request) throws InterruptedException {
        String id = request.getParameter("id");
        exchangeRateService.removeBatch(id.split(","));
        return "OK";
    }


    @ResponseBody
    @RequestMapping(value = "export.do", method = RequestMethod.GET)
    public List<ExchangeRateModel> export(HttpServletRequest request) {

        ExchangeRateModel exchangeRateModel = new ExchangeRateModel();
        String currencySourceList = request.getParameter("currencySourceList");
        String currencyTargetList = request.getParameter("currencyTargetList");
        String exchangDate = request.getParameter("exchangDate");
        Date exc = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            exc = sdf.parse(exchangDate);
        } catch (ParseException e) {

        }

        exchangeRateModel.setCurrencySource(currencySourceList);
        exchangeRateModel.setCurrencyTarget(currencyTargetList);
        exchangeRateModel.setExchangDate(exc);

        List<ExchangeRateModel> list = exchangeRateService.findList(exchangeRateModel);

        list.add(exchangeRateModel);

        return list;
    }


}
