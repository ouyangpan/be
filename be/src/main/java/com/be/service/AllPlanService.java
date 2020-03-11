package com.be.service;

import org.springframework.data.domain.Page;

import com.be.entity.AllPlan;
import com.be.entity.PageableInfo;

public interface AllPlanService {


    // 分页
    Page<AllPlan> getPage(PageableInfo pageInfo);

}
