package com.be.excelReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * 抽象Excel2007读取器，excel2007的底层数据结构是xml文件，采用SAX的事件驱动的方法解析
 * xml，需要继承DefaultHandler，在遇到文件内容时，事件会触发，这种做法可以大大降低
 * 内存的耗费，特别使用于大数据量的文件。
 *
 */
public class Excel2007Reader extends DefaultHandler {
	//共享字符串表
	private SharedStringsTable sst;
	//上一次的内容
	private String lastContents;
	private boolean nextIsString;

	private int sheetIndex = -1;
	private static List<String> rowlist = new ArrayList<String>();
	//当前行
	private int curRow = 0;
	//当前列
	private int curCol = 0;
	
	// 当前遍历的Excel单元格列索引  
    protected int thisColumnIndex = -1;
    
    private String defaultStr = "";  
    private boolean listIsNull = true;  
	
	protected int total = 0;  //总行数
	private int totalCol = 0;//总列数
	
	private String sheetName;
	
	private static StylesTable stylesTable;
	private short formatIndex;   
	private String formatString;  
	private final DataFormatter formatter = new DataFormatter();   
	private CellDataType nextDataType = CellDataType.SSTINDEX;   
	
	private List<List<String>> dataList = new ArrayList();
	
	
	/**
	 * 根据sheetid 解析sheet
	 * @param stream
	 * @param sheetId
	 * @throws Exception
	 */
	public List<List<String>> processOneSheetByIndex(InputStream in,int sheetId) throws Exception {
		OPCPackage pkg = OPCPackage.open(in);
		XSSFReader r = new XSSFReader(pkg);
		stylesTable = r.getStylesTable();   
		SharedStringsTable sst = r.getSharedStringsTable();
		XMLReader parser = fetchSheetParser(sst);
		
		// 根据 rId# 查找sheet
		InputStream sheet2 = r.getSheet("rId"+sheetId);
		sheetIndex++;
		InputSource sheetSource = new InputSource(sheet2);
		parser.parse(sheetSource);
		sheet2.close();
		pkg.close();
		
		return dataList;
	}
	
	
	/**
	 * 根据sheetid 解析sheet
	 * @param stream
	 * @param sheetId
	 * @throws Exception
	 */
	public List<List<String>> processOneSheetByIndex(String filename,int sheetId) throws Exception {
		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader(pkg);
		stylesTable = r.getStylesTable();   
		SharedStringsTable sst = r.getSharedStringsTable();
		XMLReader parser = fetchSheetParser(sst);
		
		// 根据 rId# 查找sheet
		InputStream sheet2 = r.getSheet("rId"+sheetId);
		sheetIndex++;
		InputSource sheetSource = new InputSource(sheet2);
		parser.parse(sheetSource);
		sheet2.close();
		pkg.close();
		
		return dataList;
	}

	/**
	 * 根据sheet名称,解析sheet
	 * @param stream
	 * @param name
	 * @throws Exception
	 */
	public void process(InputStream stream) throws Exception {
		OPCPackage pkg = OPCPackage.open(stream);
		XSSFReader r = new XSSFReader(pkg);
		SharedStringsTable sst = r.getSharedStringsTable();
		XMLReader parser = fetchSheetParser(sst);
		XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) r.getSheetsData();
		while (iter.hasNext()) {
			InputStream ist = iter.next();
            sheetName = iter.getSheetName();
        	InputSource sheetSource = new InputSource(ist);
        	parser.parse(sheetSource);
        	ist.close();
		}
	}

	public XMLReader fetchSheetParser(SharedStringsTable sst)
			throws SAXException {
		XMLReader parser = XMLReaderFactory .createXMLReader("org.apache.xerces.parsers.SAXParser");
		this.sst = sst;
		parser.setContentHandler(this);
		return parser;
	}

	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		
		// c => 单元格
		if ("c".equals(name)) {
			// 如果下一个元素是 SST 的索引，则将nextIsString标记为true
			String cellType = attributes.getValue("t");
			if ("s".equals(cellType)) {
				nextIsString = true;
			} else {
				nextIsString = false;
			}
			this.setNextDataType(attributes);   
			String r = attributes.getValue("r");  
            int firstDigit = -1;  
            for (int c = 0; c < r.length(); ++c) {  
                if (Character.isDigit(r.charAt(c))) {  
                    firstDigit = c;  
                    break;  
                }  
            }  
            thisColumnIndex = nameToColumn(r.substring(0, firstDigit)); ;
		} else if (name.equals("row")) {  
            // 设置行号  
            if(listIsNull){  
            	totalCol = getColumns(attributes.getValue("spans"));  
                listIsNull = false;  
            }  
		}else if (name.equals("dimension")){  
            //获得总计录数  
            String d = attributes.getValue("ref");  
            total = getNumber(d.substring(d.indexOf(":")+1,d.length()));  
        }  
		
		// 置空
		lastContents = "";
	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		// 根据SST的索引值的到单元格的真正要存储的字符串
		// 这时characters()方法可能会被调用多次
		
		if (nextIsString) {
			try {
				int idx = Integer.parseInt(lastContents);
				lastContents = new XSSFRichTextString(sst.getEntryAt(idx))
						.toString();
			} catch (Exception e) {
//				e.printStackTrace();
			}
		} 
		if ("v".equals(name)) {
			paddingNullCell();
			String value = this.getDataValue(lastContents.trim(), ""); //lastContents.trim();
			value = value.equals("")?" ":value;

			rowlist.add(curCol, value);
			curCol++;
		}else {
			//如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
			if (name.equals("row")) {
				//对最后的空列做填充
				for(int i=rowlist.size();i<totalCol;i++){
					rowlist.add(i,defaultStr);
				}
				//optRows(sheetIndex,curRow,rowlist,total);
				dataList.add(rowlist);
				rowlist = new ArrayList<String>();
				curRow++;
				curCol = 0;
				thisColumnIndex = 0;
			}
		}
	}

	private static int getNumber(String column) {  
        String c = column.toUpperCase().replaceAll("[A-Z]", "");  
        return Integer.parseInt(c);  
    }  
	
	
	  /** 
     * 根据element属性设置数据类型 
     * @param attributes 
     */  
    public void setNextDataType(Attributes attributes){   

        nextDataType = CellDataType.NUMBER;   
        formatIndex = -1;   
        formatString = null;   
        String cellType = attributes.getValue("t");   
        String cellStyleStr = attributes.getValue("s");   
        if ("b".equals(cellType)){   
            nextDataType = CellDataType.BOOL;  
        }else if ("e".equals(cellType)){   
            nextDataType = CellDataType.ERROR;   
        }else if ("inlineStr".equals(cellType)){   
            nextDataType = CellDataType.INLINESTR;   
        }else if ("s".equals(cellType)){   
            nextDataType = CellDataType.SSTINDEX;   
        }else if ("str".equals(cellType)){   
            nextDataType = CellDataType.FORMULA;   
        }  
        if (cellStyleStr != null){   
            int styleIndex = Integer.parseInt(cellStyleStr);   
            XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);   
            formatIndex = style.getDataFormat();   
            formatString = style.getDataFormatString();   
            if ("m/d/yy" == formatString){   
                nextDataType = CellDataType.DATE;   
                //full format is "yyyy-MM-dd hh:mm:ss.SSS";  
                formatString = "yyyy-MM-dd";  
            }   
            if (formatString == null){   
                nextDataType = CellDataType.NULL;   
                formatString = BuiltinFormats.getBuiltinFormat(formatIndex);   
            }   
        }   
    }  
	
	 /** 
     * 根据数据类型获取数据 
     * @param value 
     * @param thisStr 
     * @return 
     */  
    public String getDataValue(String value, String thisStr)   

    {   
        switch (nextDataType)   
        {   
            //这几个的顺序不能随便交换，交换了很可能会导致数据错误   
            case BOOL:   
            char first = value.charAt(0);   
            thisStr = first == '0' ? "FALSE" : "TRUE";   
            break;   
            case ERROR:   
            thisStr = "\"ERROR:" + value.toString() + '"';   
            break;   
            case FORMULA:   
            thisStr = '"' + value.toString() + '"';   
            break;   
            case INLINESTR:   
            XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());   
            thisStr = rtsi.toString();   
            rtsi = null;   
            break;   
            case SSTINDEX:   
            String sstIndex = value.toString();   
            thisStr = value.toString();   
            break;   
            case NUMBER:   
            if (formatString != null){   
                thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString).trim();   
            }else{  
                thisStr = value;   
            }   
            thisStr = thisStr.replace("_", "").trim();   
            break;   
            case DATE:   
                try{  
                    thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString);   
                }catch(NumberFormatException ex){  
                    thisStr = value.toString();  
                }  
            thisStr = thisStr.replace(" ", "");  
            break;   
            default:   
            thisStr = "";   
            break;   
        }   
        return thisStr;   
    }   
	
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		//得到单元格内容的值
		lastContents += new String(ch, start, length);
	}

	
	private static int getColumns(String spans) {  
        String number = spans.substring(spans.lastIndexOf(':') + 1,  
                spans.length());  
        return Integer.parseInt(number);  
    }  
	
	/**
	 * 空的单元个填充
	 */
	private void paddingNullCell() {
        int index = curCol;
        if(thisColumnIndex > index){
            for(int i = index; i < thisColumnIndex; i++){
                rowlist.add(curCol, "");
                curCol++;
            }
        }
    }
    /** 
     * 从列名转换为列索引 
     * @param name 
     * @return 
     */ 
    private static int nameToColumn(String name) {  
        int column = -1;  
        for (int i = 0; i < name.length(); ++i) {  
            int c = name.charAt(i);  
            column = (column + 1) * 26 + c - 'A';  
        }  
        return column;  
    } 
	
    @SuppressWarnings("unused")
	private void optRows(int sheetIndex,int curRow, List<String> rowlist, int total2) { 
        for (int i = 0; i < rowlist.size(); i++) {  
            System.out.print("'" + rowlist.get(i) + "',");  
        }  
        System.out.println("");  
    }  
	  
	    public static void main(String[] args) throws Exception {  
	    	Excel2007Reader howto = new Excel2007Reader();  
	    	List<List<String>> dataList = howto.processOneSheetByIndex("C:\\Users\\ouyangpan\\Desktop\\导入模板\\sw-1.xlsx",1);
	    	
	    	System.out.println(dataList.size());
	    }  
}
