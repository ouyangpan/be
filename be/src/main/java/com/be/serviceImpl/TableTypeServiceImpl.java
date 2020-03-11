package com.be.serviceImpl;

import java.util.List;

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
import com.be.service.TableTypeService;
import com.be.service.UploadExcelService;
import com.be.utils.QueryUtils;

@Service
public class TableTypeServiceImpl implements TableTypeService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

}
