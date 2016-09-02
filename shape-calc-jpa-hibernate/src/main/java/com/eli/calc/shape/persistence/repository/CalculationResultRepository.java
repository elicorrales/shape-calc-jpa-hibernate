package com.eli.calc.shape.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.eli.calc.shape.domain.CalculationResult;
import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.ShapeName;

//public interface CalculationResultRepository extends PagingAndSortingRepository<CalculationResult, Long> {
public interface CalculationResultRepository extends JpaRepository<CalculationResult, Long> {

	public CalculationResult findByShapeNameAndCalcTypeAndDimension(
			ShapeName shapeName,
			CalcType calcType,
			double dimension
			);

	List<CalculationResult> findAllByOrderByShapeNameAscCalcTypeAscDimensionAsc();
}
