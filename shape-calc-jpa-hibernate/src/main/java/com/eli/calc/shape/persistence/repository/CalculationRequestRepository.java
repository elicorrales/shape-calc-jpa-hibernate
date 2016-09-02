package com.eli.calc.shape.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.eli.calc.shape.domain.CalculationRequest;

//public interface CalculationRequestRepository extends PagingAndSortingRepository<CalculationRequest, Long> {
public interface CalculationRequestRepository extends JpaRepository<CalculationRequest, Long> {

	List<CalculationRequest> findAllByOrderByShapeNameAscCalcTypeAscDimensionAsc();
	
	
}
