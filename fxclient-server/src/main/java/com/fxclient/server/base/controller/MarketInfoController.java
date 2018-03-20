package com.fxclient.server.base.controller;



import com.fxclient.server.app.model.PageData;
import com.uams.rpc.base.model.MarketInfoModel;
import com.uams.rpc.base.service.MarketInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "fxclient/marketInfoManager")
public class MarketInfoController {


    @Autowired(required = false)
    private MarketInfoService marketInfoService;

    @ResponseBody
    @RequestMapping(value = "query.do", method = RequestMethod.POST)
    public PageData<MarketInfoModel> doLogin(HttpServletRequest request)
    {

        MarketInfoModel marketInfoModel = new MarketInfoModel();
        int page = Integer.parseInt(request.getParameter("page"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String countryNo = request.getParameter("countryNo");
        String marketName = request.getParameter("marketName");
        String marketCode = request.getParameter("marketCode");
        marketInfoModel.setCountryNo(countryNo);
        marketInfoModel.setMarketName(marketName);
        marketInfoModel.setMarketCode(marketCode);
        List<MarketInfoModel> list = marketInfoService.findList(marketInfoModel);
        PageData<MarketInfoModel> pageData = new PageData<>();
        pageData.setPage(page);
        pageData.setPageSize(pageSize);
        pageData.setTotlePage(10);
        pageData.setTotleRecords(90);
        pageData.setRecords(list);

        pageData.setTotlePage((list.size() + 1) / pageSize + 1);
        pageData.setTotleRecords(list.size());
        if (page != (list.size() + 1) / pageSize + 1) {
            pageData.setRecords(list.subList((page - 1) * pageSize, page * pageSize));
        } else {
            pageData.setRecords(list.subList((page - 1) * pageSize, list.size()));
        }
        return pageData;
    }

    @ResponseBody
    @RequestMapping(value = "addOrUpdate.do", method = RequestMethod.POST)
    public String addOrUpdate(HttpServletRequest request)
    {
        String id = request.getParameter("id");
        String marketCode = request.getParameter("marketCode");
        String marketName = request.getParameter("marketName");
        String marketFullName = request.getParameter("marketFullName");
        String isStop = request.getParameter("isStop");
        Boolean stop =true;
        if ("false".equals(isStop)){
            stop=false;
        }

        String exchangeCode = request.getParameter("exchangeCode");

        String countryNo = request.getParameter("countryNo");



        MarketInfoModel model = new MarketInfoModel();

        if (null != id && !"".equals(id)) {
            model.setId(Long.valueOf(id));
        }
        if ("".equals(marketCode)||"0".equals(marketCode)){
            return "交易市场编码不能为空";
        }else if("".equals(marketName)){
            return "交易市场简称不能为空";
        }else if("".equals(marketFullName)){
            return "交易市场全称不能为空";
        }else if("".equals(isStop)) {
            return "停市标志不能为空";
        }else if("".equals(exchangeCode)) {
            return "市场代码不能为空";
        }else if("".equals(countryNo)) {
            return "国家或地区不能为空";
        }

        model.setMarketCode(marketCode);
        model.setMarketName(marketName);
        model.setMarketFullName(marketFullName);
        model.setIsStop(stop);
        model.setExchangeCode(exchangeCode);
        model.setCountryNo(countryNo);
        model.setUpdateDate(new Date());

        marketInfoService.save(model);

        return "OK";

    }

    @ResponseBody
    @RequestMapping(value = "delete.do", method = RequestMethod.POST)
    public String delete(HttpServletRequest request) throws InterruptedException
    {
        String id = request.getParameter("id");
        marketInfoService.removeBatch(id.split(","));
        return "OK";
    }


    @ResponseBody
    @RequestMapping(value = "export.do", method = RequestMethod.GET)
    public List<MarketInfoModel> export(HttpServletRequest request) {

        MarketInfoModel marketInfoModel = new MarketInfoModel();
        String marketCode = request.getParameter("marketCode");
        String marketName = request.getParameter("marketName");
        String exchangeCode = request.getParameter("exchangeCode");
        marketInfoModel.setMarketCode(marketCode);
        marketInfoModel.setMarketName(marketName);

        marketInfoModel.setExchangeCode(exchangeCode);

        List<MarketInfoModel> list = marketInfoService.findList(marketInfoModel);

        list.add(marketInfoModel);

        return list;
    }


}
