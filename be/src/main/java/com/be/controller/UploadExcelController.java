package com.be.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.be.entity.TableType;
import com.be.excelReader.Excel2007Reader;
import com.be.repository.TableTypeRepository;
import com.be.service.UploadExcelService;

@CrossOrigin(origins = {"*", "null"})
@RestController
public class UploadExcelController {

	@Autowired
	private UploadExcelService excelService;
	
	@Autowired
	private TableTypeRepository tableTypeRepository;

	@RequestMapping("/uploadexcel")
	@ResponseBody
	public String pubggupload(@RequestParam("file") MultipartFile file,HttpServletRequest req) throws Exception {
		
		String exceltype = req.getParameter("exceltype").trim();
		String name = file.getOriginalFilename();
		if (name.length() < 6 || !name.substring(name.length() - 5).equals(".xlsx")) {
			return "文件格式错误，请上传.xlsx格式文件";
		}
		Excel2007Reader er07 = new Excel2007Reader();
		List<List<String>> excelList = er07.processOneSheetByIndex(file.getInputStream(),1);
		
		if(excelList != null && excelList.size()>0) {
			TableType tabletype = tableTypeRepository.findByType(exceltype);
			
			String tablename = System.currentTimeMillis()+"L";
			if(tabletype != null) {
				tablename = tabletype.getTablename();
				excelService.updateTableColumn(tablename, excelList.get(0));//第一行为标题
			}else {
				TableType newTabletype = TableType.builder().tablename(tablename).type(exceltype).build();
				tableTypeRepository.saveAndFlush(newTabletype);
				excelService.createTable(tablename, excelList.get(0));
			}
			excelService.insertTable(tablename, excelList);
		
		}
		
		return "上传成功";
	}
}
