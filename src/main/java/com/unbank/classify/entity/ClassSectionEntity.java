package com.unbank.classify.entity;

import java.util.Set;

public class ClassSectionEntity {
	private Integer id;

	private String classname;

	private Integer strucutureid;

	private Integer templateid;

	private Integer forumid;

	private Integer status;

	private Set<String> websiteList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public Integer getStrucutureid() {
		return strucutureid;
	}

	public void setStrucutureid(Integer strucutureid) {
		this.strucutureid = strucutureid;
	}

	public Integer getTemplateid() {
		return templateid;
	}

	public void setTemplateid(Integer templateid) {
		this.templateid = templateid;
	}

	public Integer getForumid() {
		return forumid;
	}

	public void setForumid(Integer forumid) {
		this.forumid = forumid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Set<String> getWebsiteList() {
		return websiteList;
	}

	public void setWebsiteList(Set<String> websiteList) {
		this.websiteList = websiteList;
	}

}
