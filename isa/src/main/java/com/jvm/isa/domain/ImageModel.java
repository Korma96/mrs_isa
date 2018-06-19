package com.jvm.isa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
 
@Entity
public class ImageModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique=true, nullable=false)
    private Long id;
	
    @Column(name = "name", unique=true, nullable=false)
	private String name;
    
    @Column(name = "type", unique=false, nullable=false)
	private String type;
	
	@Lob
    @Column(name="pic", unique=false, nullable=false)
    private byte[] pic;
	
	public ImageModel(){}
	
	public ImageModel(String name, byte[] pic){
		this.name = name;
		this.type = "png";
		this.pic = pic;
	}
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getType(){
		return this.type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public byte[] getPic(){
		return this.pic;
	}
	
	public void setPic(byte[] pic){
		this.pic = pic;
	}
}