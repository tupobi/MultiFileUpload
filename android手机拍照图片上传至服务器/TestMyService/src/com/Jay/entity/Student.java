package com.Jay.entity;

public class Student {
	private String id;
	private String name;
	private float score;
	
	public Student(){
		
	}
	
	public Student(String id, String name, float score) {
		super();
		this.id = id;
		this.name = name;
		this.score = score;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

}
