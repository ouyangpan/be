package com.be.entity;

import java.util.Map;

import org.springframework.data.domain.Sort.Direction;

public class PageableInfo {

	private Integer pageNum = 1; 
	private Integer pageLimit = 10;
	private Map<String,Map<String,Object>> condition;
	private Map<String,Direction> sortColumn;
	
	public PageableInfo(Integer pageNum, Integer pageLimit, Map<String,Map<String,Object>> condition,
			Map<String, Direction> sortColumn) {
		this.pageNum = pageNum;
		this.pageLimit = pageLimit;
		this.condition = condition;
		this.sortColumn = sortColumn;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageLimit() {
		return pageLimit;
	}
	public void setPageLimit(Integer pageLimit) {
		this.pageLimit = pageLimit;
	}
	public Map<String,Map<String,Object>> getCondition() {
		return condition;
	}
	public void setCondition(Map<String,Map<String,Object>> condition) {
		this.condition = condition;
	}
	public Map<String, Direction> getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(Map<String, Direction> sortColumn) {
		this.sortColumn = sortColumn;
	}
	
}
