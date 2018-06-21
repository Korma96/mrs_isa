package com.jvm.isa.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Auditorium {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@Column(name="name", unique=false, nullable=false)
	private String name;
	
	@Column(name="num_Of_rows", unique=false, nullable=false)
	private int numOfRows;
	
	@Column(name="num_Of_cols", unique=false, nullable=false)
	private int numOfCols;

	
	public Auditorium() {
		
	}

	public Auditorium(String name, int numOfRows, int numOfCols) {
		super();
		this.name = name;
		this.numOfRows = numOfRows;
		this.numOfCols = numOfCols;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumOfRows() {
		return numOfRows;
	}

	public void setNumOfRows(int numOfRows) {
		this.numOfRows = numOfRows;
	}

	public int getNumOfCols() {
		return numOfCols;
	}

	public void setNumOfCols(int numOfCols) {
		this.numOfCols = numOfCols;
	}
	
}
