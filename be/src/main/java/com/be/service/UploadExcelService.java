package com.be.service;

import java.util.List;
import java.util.Map;

public interface UploadExcelService {


	List<String> getTableColumn(String tablename) throws Exception;
	
	boolean createTable(String tablename,List<String> excelList) throws Exception;
	
	boolean updateTableColumn(String tablename,List<String> newColumnList) throws Exception;
	
	boolean insertTable(String tablename,List<List<String>> excelList) throws Exception;
	
}
