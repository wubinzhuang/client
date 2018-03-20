package com.fxclient.framework.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;

public class ExportExcel
{
    private String         title;
    
    private String[]       rowName;
    
    private List<Object[]> dataList = new ArrayList<Object[]>();
    
    public ExportExcel(String title, String[] rowName, List<Object[]> dataList)
    {
        this.dataList = dataList;
        this.rowName = rowName;
        this.title = title;
    }
    
    public void export(String fileName) throws Exception
    {
        try
        {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(title);
            HSSFRow rowm = sheet.createRow(0);
            HSSFCell cellTiltle = rowm.createCell(0);
            HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);
            HSSFCellStyle style = this.getStyle(workbook);
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowName.length - 1)));
            cellTiltle.setCellStyle(columnTopStyle);
            cellTiltle.setCellValue(title);
            int columnNum = rowName.length;
            HSSFRow rowRowName = sheet.createRow(2);
            for (int n = 0; n < columnNum; n++)
            {
                HSSFCell cellRowName = rowRowName.createCell(n);
                cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);
                HSSFRichTextString text = new HSSFRichTextString(rowName[n]);
                cellRowName.setCellValue(text);
                cellRowName.setCellStyle(columnTopStyle);
            }
            for (int i = 0; i < dataList.size(); i++)
            {
                Object[] obj = dataList.get(i);
                HSSFRow row = sheet.createRow(i + 3);
                for (int j = 0; j < obj.length; j++)
                {
                    HSSFCell cell = null;
                    if (j == 0)
                    {
                        cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(i + 1);
                    }
                    else
                    {
                        cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                        if (!"".equals(obj[j]) && obj[j] != null)
                        {
                            cell.setCellValue(obj[j].toString());
                        }
                    }
                    cell.setCellStyle(style);
                }
            }
            for (int colNum = 0; colNum < columnNum; colNum++)
            {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++)
                {
                    HSSFRow currentRow;
                    if (sheet.getRow(rowNum) == null)
                    {
                        currentRow = sheet.createRow(rowNum);
                    }
                    else
                    {
                        currentRow = sheet.getRow(rowNum);
                    }
                    if (currentRow.getCell(colNum) != null)
                    {
                        HSSFCell currentCell = currentRow.getCell(colNum);
                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING)
                        {
                            int length = currentCell.getStringCellValue().getBytes().length;
                            if (columnWidth < length)
                            {
                                columnWidth = length;
                            }
                        }
                    }
                }
                if (colNum == 0)
                {
                    sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
                }
                else
                {
                    sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
                }
            }
            if (workbook != null)
            {
                try
                {
                    OutputStream out = new FileOutputStream(new File(fileName));
                    workbook.write(out);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook)
    {
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("Courier New");
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setRightBorderColor(HSSFColor.BLACK.index);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setTopBorderColor(HSSFColor.BLACK.index);
        style.setFont(font);
        style.setWrapText(false);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return style;
    }
    
    public HSSFCellStyle getStyle(HSSFWorkbook workbook)
    {
        HSSFFont font = workbook.createFont();
        font.setFontName("Courier New");
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setRightBorderColor(HSSFColor.BLACK.index);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setTopBorderColor(HSSFColor.BLACK.index);
        style.setFont(font);
        style.setWrapText(false);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return style;
    }
}