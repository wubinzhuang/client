package com.fxclient.framework.controls.tableview;

import java.util.List;

public class PageData<T>
{
    private int     page;
    
    private int     pageSize;
    
    private int     totleRecords;
    
    private int     totlePage;
    
    private List<T> records;
    
    public int getPage()
    {
        return page;
    }
    
    public void setPage(int page)
    {
        this.page = page;
    }
    
    public int getPageSize()
    {
        return pageSize;
    }
    
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }
    
    public int getTotleRecords()
    {
        return totleRecords;
    }
    
    public void setTotleRecords(int totleRecords)
    {
        this.totleRecords = totleRecords;
    }
    
    public int getTotlePage()
    {
        return totlePage;
    }
    
    public void setTotlePage(int totlePage)
    {
        this.totlePage = totlePage;
    }
    
    public List<T> getRecords()
    {
        return records;
    }
    
    public void setRecords(List<T> records)
    {
        this.records = records;
    }
}
