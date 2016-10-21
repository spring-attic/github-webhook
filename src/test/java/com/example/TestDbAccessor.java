package com.example;

import java.util.List;

/**
 * @author Marcin Grzejszczak
 */
public class TestDbAccessor {

	public static List<Pojo> getPojos() {
		return TransformerController.DATABASE;
	}
}
