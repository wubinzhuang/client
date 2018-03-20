package com.fxclient.server.base.controller;

import com.fxclient.server.app.model.PageData;
import com.uams.rpc.base.model.FutureKindModel;
import com.uams.rpc.base.model.MarketInfoModel;
import com.uams.rpc.base.service.FutureKindService;
import com.uams.rpc.base.service.MarketInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "fxclient/futureKindManager")
public class FutureKindController {
    @Autowired(required = false)
    private FutureKindService futureKindService;

    @Autowired(required = false)
    private MarketInfoService marketInfoService;

    @ResponseBody
    @RequestMapping(value = "query.do", method = RequestMethod.POST)
    public PageData<FutureKindModel> query(HttpServletRequest request) {
        FutureKindModel futureKindModel=new FutureKindModel();
        int page = Integer.parseInt(request.getParameter("page"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String kindCode = request.getParameter("kindCode");
        String kindName = request.getParameter("kindName");
        String marketIdStr=request.getParameter("marketId");
        if(null!=marketIdStr&&!"".equals(marketIdStr)&&!"0".equals(marketIdStr)){
            Long marketId = Long.valueOf(marketIdStr);
            futureKindModel.setMarketId(marketId);
        }
        futureKindModel.setKindCode(kindCode);
        futureKindModel.setKindName(kindName);

        List<FutureKindModel> list = futureKindService.findList(futureKindModel);
        for (int i=0;i<list.size()-1;i++){
            Long id=list.get(i).getMarketId();
            MarketInfoModel mim=marketInfoService.selectByPrimaryKey(id);
            if(mim!=null){
                list.get(i).setMarketName(mim.getMarketName());
            }
        }
        PageData<FutureKindModel> pageData = new PageData<>();
        //总页数
        int TotalPage=(list.size()%pageSize==0)?(list.size()/pageSize):(list.size()/pageSize+1);
        pageData.setPage(page);
        pageData.setPageSize(pageSize);
        pageData.setTotlePage(TotalPage);
        pageData.setTotleRecords(list.size());
        if(page!=TotalPage){
            pageData.setRecords(list.subList((page-1)*pageSize,page*pageSize));
        }else {
            pageData.setRecords(list.subList((page - 1) * pageSize, list.size()));
        }
        return pageData;
    }

    @ResponseBody
    @RequestMapping(value = "addOrUpdate", method = RequestMethod.POST)
    public String addOrUpdate(HttpServletRequest request) {
        FutureKindModel fkm=new FutureKindModel();
        String Id = request.getParameter("Id");
        if(null!=Id&&!"".equals(Id)){
            fkm.setId(Long.valueOf(Id));
        }
        String marketId = request.getParameter("marketId");
        String kindCode = request.getParameter("kindCode");
        String kindName = request.getParameter("kindName");
        if ("".equals(marketId)||"0".equals(marketId)){
            return "请选择交易市场";
        }else if("".equals(kindCode)){
            return "期货品种编码不能为空";
        }else if("".equals(kindName)){
            return "期货品种名称不能为空";
        }
        fkm.setMarketId(Long.valueOf(marketId));
        fkm.setKindCode(kindCode);
        fkm.setKindName(kindName);

        fkm.setUpdateDate(new Date());
        fkm.setMarketName(marketInfoService.selectByPrimaryKey(fkm.getMarketId()).getMarketName());
        futureKindService.save(fkm);
        return "OK";
    }

    @ResponseBody
    @RequestMapping(value = "delete.do", method = RequestMethod.POST)
    public String delete(HttpServletRequest request) throws InterruptedException {
        String id = request.getParameter("id");
        try {
        futureKindService.removeBatch(id.split(","));
        return "OK";
        }catch(Exception e){
            return "删除失败";
        }
    }

    @ResponseBody
    @RequestMapping(value = "all.do", method = RequestMethod.POST)
    public List<MarketInfoModel> all(HttpServletRequest request) {
        List<MarketInfoModel> list = marketInfoService.selectAll();
        return list;
    }

    @ResponseBody
    @RequestMapping(value = "export.do", method = RequestMethod.GET)
    public List<FutureKindModel> export(HttpServletRequest request)
    {
        FutureKindModel futureKindModel=new FutureKindModel();
        /*int page = Integer.parseInt(request.getParameter("page"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));*/
        String kindCode = request.getParameter("kindCode");
        String kindName = request.getParameter("kindName");
        String marketIdStr=request.getParameter("marketId");
        if(null!=marketIdStr&&!"".equals(marketIdStr)&&!"0".equals(marketIdStr)){
            Long marketId = Long.valueOf(marketIdStr);
            futureKindModel.setMarketId(marketId);
        }
        futureKindModel.setKindCode(kindCode);
        futureKindModel.setKindName(kindName);

        List<FutureKindModel> list = futureKindService.findList(futureKindModel);
        for (int i=0;i<list.size();i++){
            Long id=list.get(i).getMarketId();
            MarketInfoModel mim=marketInfoService.selectByPrimaryKey(id);
            list.get(i).setMarketName(mim.getMarketName());
        }
        return list;
    }
}