package com.unbank.classify.entity;

public class Label {

	private int id;
	private String classname;
	private int state;
	private int strategytype;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getStrategytype() {
		return strategytype;
	}

	public void setStrategytype(int strategytype) {
		this.strategytype = strategytype;
	}

}
