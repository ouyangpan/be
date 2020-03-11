package com.be.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tableType")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TableType {
	
	@Id
	@GeneratedValue(generator="IDENTITY")
	private Long id;

	@Column(nullable = false)
	private String tablename;//表名

	@Column(nullable = true)
	private String type;//类型
}
