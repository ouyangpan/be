package com.be.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class POI07Util {


    /** 
     * 1.创建 workbook 
     * @return 
     */  
    public XSSFWorkbook getXSSFWorkbook(){  
        return new XSSFWorkbook();  
    }  
  
    /** 
     * 2.创建 sheet 
     * @param hssfWorkbook 
     * @param sheetName sheet 名称 
     * @return 
     */  
    public XSSFSheet getXSSFSheet(XSSFWorkbook xssfWorkbook, String sheetName){  
        return xssfWorkbook.createSheet(sheetName);  
    }  
    
    /**
     * 2.5 写入标题信息
     * @param hssfWorkbook
     * @param hssfSheet
     * @param headInfoList
     */
    public void writeTitle(XSSFWorkbook xssfWorkbook,XSSFSheet xssfSheet,List<Map<String, Object>> headInfoList,String title){  
    	  XSSFCellStyle cs = xssfWorkbook.createCellStyle();  
          XSSFFont font = xssfWorkbook.createFont();  
          font.setFontHeightInPoints((short)12);  
          font.setBoldweight(font.BOLDWEIGHT_BOLD);  
          cs.setFont(font);  
          cs.setAlignment(cs.ALIGN_CENTER);  
          CellRangeAddress callRangeAddress = new CellRangeAddress(0,0,0,headInfoList.size()-1);
          
          xssfSheet.addMergedRegion(callRangeAddress); 
          XSSFRow r = xssfSheet.createRow(0);  
          r.setHeight((short) 380);  
          XSSFCell c = r.createCell(0);
          c.setCellValue(title);  
          c.setCellStyle(cs);  
    }
  
    /** 
     * 3.写入表头信息 
     * @param hssfWorkbook 
     * @param hssfSheet 
     * @param headInfoList List<Map<String, Object>> 
     *              key: title         列标题 
     *                   columnWidth   列宽 
     *                   dataKey       列对应的 dataList item key 
     */  
    public void writeHeader(XSSFWorkbook xssfWorkbook,XSSFSheet xssfSheet ,int startIndex, List<Map<String, Object>> headInfoList){  
        XSSFCellStyle cs = xssfWorkbook.createCellStyle();  
        XSSFFont font = xssfWorkbook.createFont();  
        font.setFontHeightInPoints((short)12);  
        font.setBoldweight(font.BOLDWEIGHT_BOLD);  
        cs.setFont(font);  
        cs.setAlignment(cs.ALIGN_CENTER);  
  
        XSSFRow r = xssfSheet.createRow(startIndex);  
        r.setHeight((short) 380);  
        XSSFCell c = null;  
        Map<String, Object> headInfo = null;  
        //处理excel表头  
        for(int i=0, len = headInfoList.size(); i < len; i++){  
            headInfo = headInfoList.get(i);  
            c = r.createCell(i);  
            c.setCellValue(headInfo.get("title").toString());  
            c.setCellStyle(cs);  
            if(headInfo.containsKey("columnWidth")){  
                xssfSheet.setColumnWidth(i, (short)(((Integer)headInfo.get("columnWidth") * 8) / ((double) 1 / 20)));  
            }  
        }  
    }  
  
    /** 
     * 4.写入内容部分 
     * @param hssfWorkbook 
     * @param hssfSheet 
     * @param startIndex 从1开始，多次调用需要加上前一次的dataList.size() 
     * @param headInfoList List<Map<String, Object>> 
     *              key: title         列标题 
     *                   columnWidth   列宽 
     *                   dataKey       列对应的 dataList item key 
     * @param dataList 
     */  
    public void writeContent(XSSFWorkbook xssfWorkbook,XSSFSheet xssfSheet ,int startIndex,  
                                     List<Map<String, Object>> headInfoList, List<Map<String, Object>> dataList){  
        Map<String, Object> headInfo = null;  
        XSSFRow r = null;  
        XSSFCell c = null;  
        //处理数据  
        Map<String, Object> dataItem = null;  
        Object v = null;  
        for (int i=0, rownum = startIndex, len = (startIndex + dataList.size()); rownum < len; i++,rownum++){  
            r = xssfSheet.createRow(rownum);  
            r.setHeightInPoints(16);  
            dataItem = dataList.get(i);  
            for(int j=0, jlen = headInfoList.size(); j < jlen; j++){  
                headInfo = headInfoList.get(j);  
                c = r.createCell(j);  
                v = dataItem.get(headInfo.get("dataKey").toString());  
  
                if (v instanceof String) {  
                    c.setCellValue((String)v);  
                }else if (v instanceof Boolean) {  
                    c.setCellValue((Boolean)v);  
                }else if (v instanceof Calendar) {  
                    c.setCellValue((Calendar)v);  
                }else if (v instanceof Double) {  
                    c.setCellValue((Double)v);  
                }else if (v instanceof Integer  
                        || v instanceof Long  
                        || v instanceof Short  
                        || v instanceof Float) {  
                    c.setCellValue(Double.parseDouble(v.toString()));  
                }else if (v instanceof XSSFRichTextString) {  
                    c.setCellValue((XSSFRichTextString)v);  
                }else {  
                    c.setCellValue(v == null?"":v.toString());  
                }  
            }  
        }  
    }  
  
    public void write2FilePath(XSSFWorkbook xssfWorkbook, String filePath) throws IOException{  
        FileOutputStream fileOut = null;  
        try{  
            fileOut = new FileOutputStream(filePath);  
            xssfWorkbook.write(fileOut);  
        }finally{  
            if(fileOut != null){  
                fileOut.close();  
            }  
        }  
    }  
  
  
    /** 
     * 导出excel 
     * code example: 
         List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>(); 
         Map<String, Object> itemMap = new HashMap<String, Object>(); 
         itemMap.put("title", "序号1"); 
         itemMap.put("columnWidth", 25); 
         itemMap.put("dataKey", "XH1"); 
         headInfoList.add(itemMap); 
 
         itemMap = new HashMap<String, Object>(); 
         itemMap.put("title", "序号2"); 
         itemMap.put("columnWidth", 50); 
         itemMap.put("dataKey", "XH2"); 
         headInfoList.add(itemMap); 
 
         itemMap = new HashMap<String, Object>(); 
         itemMap.put("title", "序号3"); 
         itemMap.put("columnWidth", 25); 
         itemMap.put("dataKey", "XH3"); 
         headInfoList.add(itemMap); 
 
         List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>(); 
         Map<String, Object> dataItem = null; 
         for(int i=0; i < 100; i++){ 
         dataItem = new HashMap<String, Object>(); 
         dataItem.put("XH1", "data" + i); 
         dataItem.put("XH2", 88888888f); 
         dataItem.put("XH3", "脉兜V5.."); 
         dataList.add(dataItem); 
         } 
         POIUtil.exportExcel2FilePath("test sheet 1","F:\\temp\\customer2.xls", headInfoList, dataList); 
 
     * @param sheetName   sheet名称 
     * @param filePath   文件存储路径， 如：f:/a.xls 
     * @param headInfoList List<Map<String, Object>> 
     *                           key: title         列标题 
     *                                columnWidth   列宽 
     *                                dataKey       列对应的 dataList item key 
     * @param dataList  List<Map<String, Object>> 导出的数据 
     * @throws java.io.IOException 
     * 
     */  
    public static void exportExcel2FilePath(String sheetName, String filePath,  
                                   List<Map<String, Object>> headInfoList,  
                                   List<Map<String, Object>> dataList, String titlename) throws IOException {  
        POI07Util poiUtil = new POI07Util();  
        //1.创建 Workbook  
        XSSFWorkbook xssfWorkbook = poiUtil.getXSSFWorkbook();  
        //2.创建 Sheet  
        XSSFSheet xssfSheet = poiUtil.getXSSFSheet(xssfWorkbook, sheetName);  
        //2.5 写入标题
        if(!titlename.equals("")){
        	poiUtil.writeTitle(xssfWorkbook, xssfSheet,headInfoList, titlename);
        }
        //3.写入 head  
        poiUtil.writeHeader(xssfWorkbook, xssfSheet, titlename.equals("")?0:1, headInfoList);  
        //4.写入内容  
        poiUtil.writeContent(xssfWorkbook, xssfSheet, titlename.equals("")?1:2, headInfoList, dataList);  
        //5.保存文件到filePath中  
        poiUtil.write2FilePath(xssfWorkbook, filePath);  
    }  
    
    public static void main(String[] args) {
    	
    	List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>(); 
        Map<String, Object> itemMap = new HashMap<String, Object>(); 
        itemMap.put("title", "序号1"); 
        itemMap.put("columnWidth", 25); 
        itemMap.put("dataKey", "XH1"); 
        headInfoList.add(itemMap); 

        itemMap = new HashMap<String, Object>(); 
        itemMap.put("title", "序号2"); 
        itemMap.put("columnWidth", 50); 
        itemMap.put("dataKey", "XH2"); 
        headInfoList.add(itemMap); 

        itemMap = new HashMap<String, Object>(); 
        itemMap.put("title", "序号3"); 
        itemMap.put("columnWidth", 25); 
        itemMap.put("dataKey", "XH3"); 
        headInfoList.add(itemMap); 

        List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>(); 
        Map<String, Object> dataItem = null; 
        for(int i=0; i < 100; i++){ 
        dataItem = new HashMap<String, Object>(); 
        dataItem.put("XH1", "data" + i); 
        dataItem.put("XH2", 88888888f); 
        dataItem.put("XH3", "脉兜V5.."); 
        dataList.add(dataItem); 
        } 
    	try {
			exportExcel2FilePath("shet1","D:\\customer2.xlsx",headInfoList,dataList,"第二次全国污染源普查规模化畜禽养殖场清查汇总表");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
