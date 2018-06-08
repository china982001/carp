package org.carp.test.pojo;

import java.sql.Blob;
import java.util.Date;

import org.carp.annotation.CarpAnnotation.Generate;
import org.carp.annotation.Column;
import org.carp.annotation.Id;
import org.carp.annotation.Table;

@Table(name="t_carp")
public class TCarp {
	@Id(name="pk_id",build=Generate.sequences,sequence="seq_carp")
	private Long pkId;
	
	@Column(name="t_name",Null=false,length=20 )
	private String tName;
	
	@Column(name="t_cname")
	private String tCname;
	
	@Column(name="t_data")
	private java.sql.Blob tData;

	@Column(name="t_date")
	private Date tDate;
	
	@Column(name="t_double")
	private double tDouble;
	
	@Column(name="sts")
	private String sts;

	public Long getPkId() {
		return pkId;
	}

	public void setPkId(Long pkId) {
		this.pkId = pkId;
	}

	public String getTName() {
		return tName;
	}

	public void setTName(String name) {
		tName = name;
	}

	public String getTCname() {
		return tCname;
	}

	public void setTCname(String cname) {
		tCname = cname;
	}

	public java.sql.Blob getTData() {
		return tData;
	}

	public void setTData(java.sql.Blob data) {
		tData = data;
	}

	public Date getTDate() {
		return tDate;
	}

	public void setTDate(Date date) {
		tDate = date;
	}

	public double getTDouble() {
		return tDouble;
	}

	public void setTDouble(double double1) {
		tDouble = double1;
	}

	public String getSts() {
		return sts;
	}

	public void setSts(String sts) {
		this.sts = sts;
	}
	
	
}
