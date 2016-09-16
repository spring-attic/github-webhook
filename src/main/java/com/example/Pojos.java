package com.example;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author Marcin Grzejszczak
 */
public class Pojos {
	private final List<Pojo> data;

	@JsonCreator
	public Pojos(List<Pojo> data) {
		this.data = data;
	}

	public List<Pojo> getData() {
		return data;
	}
}
