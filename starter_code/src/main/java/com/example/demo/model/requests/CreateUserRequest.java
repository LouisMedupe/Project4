package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserRequest {

	@JsonProperty
	private String username;
	@JsonProperty
	private String password;
	@JsonProperty
	private String ConfirmPassword;

	public CreateUserRequest(String username, String password, String confirmPassword) {
		this.username = username;
		this.password = password;
		this.ConfirmPassword = confirmPassword;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setConfirmPassword(String confirmPassword) {
		ConfirmPassword = confirmPassword;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getConfirmPassword() {
		return ConfirmPassword;
	}
}
