package com.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.be.entity.TableType;

@Repository
public interface TableTypeRepository extends JpaRepository<TableType, Long>,JpaSpecificationExecutor {

	TableType findByType(String type);
}
