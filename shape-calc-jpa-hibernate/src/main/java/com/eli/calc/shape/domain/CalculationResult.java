package com.eli.calc.shape.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.ShapeName;

@Entity
@Table(name="CALCULATION_RESULTS")
@IdClass(value=RequestPk.class)
public final class CalculationResult {

	@Id
	@Enumerated(EnumType.STRING)
	@Column(name="shapename",nullable=false)
	private ShapeName shapeName;
	
	@Id
	@Enumerated(EnumType.STRING)
	@Column(name="calctype",nullable=false)
	private CalcType calcType;
	
	@Id
	@Column(name="dimension",nullable=false)
	private Double dimension;
	
	@Column(name="result",nullable=false)
	private Double result;
	
	private boolean error = false;
	
	public CalculationResult() { }
	
	public CalculationResult(CalculationRequest request) {

		if (null==request){throw new IllegalArgumentException("CalculationRequest is required."); }

		this.shapeName = request.getShapeName();
		this.calcType = request.getCalcType();
		this.dimension = request.getDimension();

	}


	public CalculationResult(CalculationRequest request, Double result) {

		this(request);
		if (null==result) { throw new IllegalArgumentException("result is required"); }
		this.result = result;

	}

	public ShapeName getShapeName() {
		return shapeName;
	}

	public void setShapeName(ShapeName shapeName) {
		if (null==shapeName){throw new IllegalArgumentException("ShapeName is required."); }
		this.shapeName = shapeName;
	}

	public CalcType getCalcType() {
		return calcType;
	}

	public void setCalcType(CalcType calcType) {
		if (null==calcType){throw new IllegalArgumentException("CalcType is required."); }
		this.calcType = calcType;
	}

	public Double getDimension() {
		return dimension;
	}

	public void setDimension(Double dimension) {
		this.dimension = dimension;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		if (null==result) { throw new IllegalArgumentException("result is required"); }
		this.result = result;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public CalculationRequest getRequest() {
		return new CalculationRequest(this.shapeName,this.calcType,this.dimension);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((calcType == null) ? 0 : calcType.hashCode());
		result = prime * result + ((dimension == null) ? 0 : dimension.hashCode());
		result = prime * result + ((shapeName == null) ? 0 : shapeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CalculationResult other = (CalculationResult) obj;
		if (calcType != other.calcType)
			return false;
		if (dimension == null) {
			if (other.dimension != null)
				return false;
		} else if (!dimension.equals(other.dimension))
			return false;
		if (shapeName != other.shapeName)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CalculationResult [shapeName=" + shapeName + ", calcType=" + calcType + ", dimension=" + dimension
				+ ", result=" + result + ", error=" + error + "]";
	}


}