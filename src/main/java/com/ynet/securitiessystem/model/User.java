package com.ynet.securitiessystem.model;

import java.util.Set;

public class User {

	private String userId;
	private Set<Group> groups;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Set<Group> getGroups() {
		return groups;
	}
	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}
}
