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
@Table( name="PENDING_REQUESTS")
@IdClass(value=RequestPk.class)
public final class CalculationRequest {

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
	private Double dimension = new Double(0.0);
	
	public CalculationRequest() { }
	
	public CalculationRequest(ShapeName shapeName, CalcType calcType, Double dimension) {
		
		if (null==shapeName) { throw new IllegalArgumentException("ShapeName required"); }
		if (null==calcType) { throw new IllegalArgumentException("CalcType required"); }
		this.shapeName = shapeName;
		this.calcType = calcType;
		if (null==dimension) { dimension = new Double(0.0); }
		this.dimension = dimension;
	}

	public ShapeName getShapeName() {
		return shapeName;
	}

	public void setShapeName(ShapeName shapeName) {
		this.shapeName = shapeName;
	}

	public CalcType getCalcType() {
		return calcType;
	}

	public void setCalcType(CalcType calcType) {
		this.calcType = calcType;
	}

	public Double getDimension() {
		return dimension;
	}

	public void setDimension(Double dimension) {
		if (null==dimension) { dimension = new Double(0.0); }
		this.dimension = dimension;
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
		CalculationRequest other = (CalculationRequest) obj;
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
		return "CalculationRequest [shapeName=" + shapeName + ", calcType=" + calcType + ", dimension=" + dimension
				+ "]";
	}



}