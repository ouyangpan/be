package com.be.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.be.entity.AllPlan;

@Repository
public interface AllPlanRepository extends JpaRepository<AllPlan, Long>,JpaSpecificationExecutor {

	/*
	@Transactional(timeout = 10)
    @Modifying
    @Query("update AllPlan set state = ?1 where id = ?2 and userid = ?3")
	void updateAllPlanState(int state, Long id, String userid);

	//有nativeQuery = true时，是可以执行原生sql语句，所谓原生sql，也就是说这段sql拷贝到数据库中，然后把参数值给一下就能运行了
	@Query(value = "select * from all_plan where userid = ?1 and state = 1",nativeQuery = true)
	List<AllPlan> getAllPlanByUserid(String userid);
	
	@Query(value = "select * from all_plan where parentid is null" ,nativeQuery = true)
	List<AllPlan> getAllParentPlan(String userid);
	
	AllPlan findByUseridAndId(String userid,Long id);
	
	@Query(value = "select new com.example.didi.entity.AllPlan(id,name,detail,userid,state,isRepeat,executeDate,executeWeek) from AllPlan where userid = ?1 and parent is not null")
	List<AllPlan> getAllPlanWithOutParentInfo(String userid);
	*/
}
