package com.be.utils;

import java.awt.Menu;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;

import com.be.entity.PageableInfo;

public class QueryUtils {

	public static final String EQUAL = "equal";
	public static final String NOTEQUAL = "notequal";
	public static final String DATABETWEEN = "databetween";
	public static final String LIKE = "like";
	public static final String IN = "in";
	public static final String NOTNULL = "notnull";
	
	 /**
     * 带条件的分页查询
     * @param Map<String,Map<String,Object>> paraMap
     * key:比较符
     * 
     * 		key:字段名
     * 		value:实际值
     */
    public static Specification<Menu> getMenuSpecification(Map<String,Map<String,Object>> paraMap) {
    	
    	if(paraMap.isEmpty()) {
    		return null;
    	}
        return (Specification<Menu>) (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            for(Entry<String, Map<String,Object>>entry : paraMap.entrySet()) {
            	String key = entry.getKey();
            	Map<String,Object> value = entry.getValue();
            	 for(Entry<String,Object>entry1 : value.entrySet()) {
            		 String key1 = entry1.getKey();
            		 Object value1 = entry1.getValue();
            		 if(value1 != null && !value1.equals("")) {
            			 if(key.equals(EQUAL)) {
            				 predicate.getExpressions().add(criteriaBuilder.equal(root.get(key1), value1));
            			 }else if(key.equals(DATABETWEEN)) {
            				 Date[] date = (Date[]) value1;
            				 predicate.getExpressions().add(criteriaBuilder.between(root.get(key1), date[0], date[1]));
            			 }else if(key.equals(LIKE)) {
            				 predicate.getExpressions().add(criteriaBuilder.like(root.get(key1),"%"+value1+"%"));
            			 }else if(key.equals(IN)) {
            				 List<Object> list = (List<Object>) value1;
            				 Path<Object> path = root.get(key1);//定义查询的字段
            				 CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
            				 for (int i = 0; i <list.size() ; i++) {
            				     in.value(list.get(i));//存入值
            				 }
            				 predicate.getExpressions().add(criteriaBuilder.and(in));
            			 }else if(key.equals(NOTEQUAL)) {
            				 predicate.getExpressions().add(criteriaBuilder.notEqual(root.get(key1), value1));
            			 }else if(key.equals(NOTNULL)) {
            				 predicate.getExpressions().add(criteriaBuilder.isNotNull(root.get(key1)));
            			 }
            			 
                	}
            	 }
            	
            	
            }
            return predicate;
        };
    }
    
    public static Pageable getPageable(PageableInfo pageInfo) {
    	
    	Pageable pageable = null;
		if(pageInfo.getSortColumn() != null) {
			List<Order> orders=new ArrayList<Order>();
			for(Entry<String, Direction> entry : pageInfo.getSortColumn().entrySet()) {
				orders.add( new Order(entry.getValue(), entry.getKey()));
			}
			pageable = new PageRequest(pageInfo.getPageNum() - 1, pageInfo.getPageLimit(), new Sort(orders));
		}else {
			pageable = new PageRequest(pageInfo.getPageNum() - 1, pageInfo.getPageLimit());
		}
		
		return pageable;
    }
}
