package com.ynet.securitiessystem.model;

import java.util.Set;

public class Group {

	private String groupKey;
	private String groupName;
	private User user;
	private Set<Bond> productList;
	public String getGroupKey() {
		return groupKey;
	}
	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Set<Bond> getProductList() {
		return productList;
	}
	public void setProductList(Set<Bond> productList) {
		this.productList = productList;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
