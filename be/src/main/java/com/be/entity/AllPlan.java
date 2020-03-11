package com.be.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="all_plan")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AllPlan {

	public final static int DISABLE = 0;
	public final static int ENABLE = 1;
	
	@Id
	@GeneratedValue(generator="IDENTITY")
	private Long id;

	@Column(nullable = false)
	private String name;//计划名称

	@Column(nullable = true)
	private String detail;//计划备注
	
	@Column(nullable = false)
	private String userid;//用户id
	
	@Column(nullable = false)
	private int state;// 0 禁用（删除）   1启用

	@OneToOne
    @JoinColumn(name = "parentid")
	private AllPlan parent;//父计划
	
	@Column(nullable = true)
	private int isRepeat;//是否重复执行   0否    1是

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(nullable = true)
	@JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date executeDate;//执行日期
	
	@Column(nullable = true)
	private String executeWeek;//执行周数
	
	public AllPlan(Long id, String name, String detail, String userid, int state, int isRepeat,
			Date executeDate, String executeWeek) {
		this.id = id;
		this.name = name;
		this.detail = detail;
		this.userid = userid;
		this.state = state;
		this.isRepeat = isRepeat;
		this.executeDate = executeDate;
		this.executeWeek = executeWeek;
	}
}
