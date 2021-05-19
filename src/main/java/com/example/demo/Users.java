package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class Users {
	@Id
	@Column(name="user_id")
	private Integer userId;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="user_pass")
	private String userPass;

	public Users() {
	}

	public Users(Integer userId, String userName, String userPass) {
		this.userId = userId;
		this.userName = userName;
		this.userPass = userPass;
	}
	public Users(String userName, String userPass) {
		this.userName = userName;
		this.userPass = userPass;
	}

	public Integer getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserPass() {
		return userPass;
	}
	

}
