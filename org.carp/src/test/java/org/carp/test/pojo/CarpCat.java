package org.carp.test.pojo;

import java.util.Date;

import org.carp.annotation.Column;
import org.carp.annotation.Id;
import org.carp.annotation.Table;
import org.carp.annotation.CarpAnnotation.Generate;

@Table(name="carp_cat")
public class CarpCat {
	@Id(name="cat_id",build=Generate.assigned)
	private Integer catId;
	
	@Column(name="cat_name",Null=false,length=30 )
	private String catName;
	
	@Column(name="cat_age")
	private short catAge;
	
	@Column(name="sts_time")
	private Date stsTime;
	
	@Column(name="cat_weight")
	private float catWeight;

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public Short getCatAge() {
		return catAge;
	}

	public void setCatAge(Short catAge) {
		this.catAge = catAge;
	}

	public Date getStsTime() {
		return stsTime;
	}

	public void setStsTime(Date sts_time) {
		this.stsTime = sts_time;
	}

	public double getCatWeight() {
		return catWeight;
	}

	public void setCatWeight(float catWeight) {
		this.catWeight = catWeight;
	}
	
	public String toString(){
		String s = "";
		s += "catId="+catId;
		s += ",catName="+catName;
		s += ",catAge="+catAge;
		s += ",catWeight="+catWeight;
		return s;
	}
}
