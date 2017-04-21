package com.example;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author Marcin Grzejszczak
 */
public class Pojo {
	private final String user;
	private final String repo;
	private final String type;
	private final String action;

	@JsonCreator
	public Pojo(String user, String repo, String type, String action) {
		this.user = user;
		this.repo = repo;
		this.type = type;
		this.action = action;
	}

	public String getUser() {
		return this.user;
	}

	public String getRepo() {
		return this.repo;
	}

	public String getType() {
		return this.type;
	}

	public String getAction() {
		return this.action;
	}
}
