package org.carp.test.pojo;

import java.sql.Blob;
import java.sql.Clob;

import org.carp.annotation.Column;
import org.carp.annotation.Id;
import org.carp.annotation.Table;
import org.carp.annotation.CarpAnnotation.Generate;

@Table(name="carp_blob")
public class CarpBlob {
	@Id(name="cat_id",build=Generate.assigned)
	private Integer catId;
	@Column(name="cat_image")
	private byte[] catImage;
	@Column(name="cat_spec")
	private String catSpec;
	public Integer getCatId() {
		return catId;
	}
	public void setCatId(Integer catId) {
		this.catId = catId;
	}
	public byte[] getCatImage() {
		return catImage;
	}
	public void setCatImage(byte[] catImage) {
		this.catImage = catImage;
	}
	public String getCatSpec() {
		return catSpec;
	}
	public void setCatSpec(String catSpec) {
		this.catSpec = catSpec;
	}
}
