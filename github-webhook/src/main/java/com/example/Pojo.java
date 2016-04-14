package com.example;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author Marcin Grzejszczak
 */
public class Pojo {
	private final String username;
	private final String repository;

	@JsonCreator
	public Pojo(String username, String repository) {
		this.username = username;
		this.repository = repository;
	}

	public String getUsername() {
		return username;
	}

	public String getRepository() {
		return repository;
	}
}
