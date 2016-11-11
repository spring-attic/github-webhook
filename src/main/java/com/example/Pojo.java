package com.example;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author Marcin Grzejszczak
 */
public class Pojo {
	private final String username;
	private final String repository;
	private final String type;
	private final String action;
	private final String userfullname;

	@JsonCreator
	public Pojo(String username, String repository, String type, String action, String userfullname) {
		this.username = username;
		this.repository = repository;
		this.type = type;
		this.action = action;
		this.userfullname = userfullname;
	}

	public String getUsername() {
		return this.username;
	}

	public String getRepository() {
		return this.repository;
	}

	public String getType() {
		return this.type;
	}

	public String getAction() {
		return this.action;
	}

	public String getUserfullname() {
		return this.userfullname;
	}
}
