package com.be.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.be.entity.AllPlan;
import com.be.entity.PageableInfo;
import com.be.repository.AllPlanRepository;
import com.be.service.AllPlanService;
import com.be.utils.QueryUtils;

@Service
public class AllPlanServiceImpl implements AllPlanService {

	@Autowired
	AllPlanRepository allplan;

	@Override
	@Transactional(readOnly = true) // 只读
	public Page<AllPlan> getPage(PageableInfo pageInfo) {
		
		Pageable pageable = QueryUtils.getPageable(pageInfo);
		return allplan.findAll(QueryUtils.getMenuSpecification(pageInfo.getCondition()), pageable);
	}

}
