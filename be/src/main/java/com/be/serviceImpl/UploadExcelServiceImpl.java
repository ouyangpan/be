package com.be.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.be.entity.AllPlan;
import com.be.entity.PageableInfo;
import com.be.repository.AllPlanRepository;
import com.be.service.AllPlanService;
import com.be.service.UploadExcelService;
import com.be.utils.ListUtils;

@Service
public class UploadExcelServiceImpl implements UploadExcelService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public boolean createTable(String tablename, List<String> excelList)  throws Exception{
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE "+tablename+" ( ");
		for (int i = 0; i < excelList.size(); i++) {
			sb.append(" "+excelList.get(i)+" varchar(200),");
		}
		sb.setCharAt(sb.length()-1, ')');
		jdbcTemplate.execute(sb.toString());
		return false;
	}

	@Override
	public boolean updateTableColumn(String tablename, List<String> newColumnList)  throws Exception{
		List<String> oldColumnList = this.getTableColumn(tablename);
		List<String> addColumnList = ListUtils.getNewListDiffInOldList(oldColumnList, newColumnList);
		
		String[] sql = new String[addColumnList.size()];
		for (int i = 0; i < addColumnList.size(); i++) {
			sql[i] = "alter table "+tablename+" add "+addColumnList.get(i)+" varchar(200)";
		}
		if(sql != null && sql.length > 0) {
			jdbcTemplate.batchUpdate(sql);
		}
		
		return true;
	}

	@Override
	public boolean insertTable(String tablename, List<List<String>> excelList)  throws Exception{
		String[] sql = new String[excelList.size()-1];
		
		StringBuilder columnname = new StringBuilder();
		for(int j = 0; j < excelList.size(); j++) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < excelList.get(j).size(); i++) {
				if(j==0) {
					//取列名
					columnname.append(excelList.get(j).get(i)+",");
				}else {
					sb.append("'"+excelList.get(j).get(i)+"',");
				}
			}
			if(j==0) {
				columnname.setCharAt(columnname.length()-1, ' ');
			}else {
				sb.setCharAt(sb.length()-1, ' ');
				sql[j-1] = "insert into "+tablename+"("+columnname.toString()+") values("+sb.toString()+")";
			}
		}
		jdbcTemplate.batchUpdate(sql);
		return true;
	}

	@Override
	public List<String> getTableColumn(String tablename) throws Exception {
		List<String> result = new ArrayList<String>();
		String sql = 
		"select column_name from information_schema.columns "+ 
		"where table_name = '"+tablename+"'" ;
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if(list != null) {
			for (int i = 0; i < list.size(); i++) {
				result.add(list.get(i).get("column_name").toString());
			}
		}
		return result;
	}


}
