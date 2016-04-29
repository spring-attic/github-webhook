package com.example;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.PathNotFoundException;
import com.toomuchcoding.jsonassert.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Marcin Grzejszczak
 */
@RestController
public class TransformerConfiguration {

	@Autowired
	Source source;

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public Pojo transform(@RequestBody String message) {
		DocumentContext parsedJson = com.jayway.jsonpath.JsonPath.parse(message);
		String username = parsedJson
				.read(JsonPath.builder().field("sender").field("login").jsonPath());
		String repo = null;
		try {
			repo = parsedJson.read(
					JsonPath.builder().field("repository").field("full_name").jsonPath());
		}
		catch (PathNotFoundException e) {
			repo = parsedJson.read(
					JsonPath.builder().field("organization").field("login").jsonPath());
		}
		String type;
		Map<String, Object> headers = new HashMap<>();
		headers.put("version", "v2");
		try {
			System.err.println(JsonPath.builder().field("issue").jsonPath());
			type = parsedJson.read("$.issue") != null ? "issue" : "unknown";
		}
		catch (PathNotFoundException e) {
			try {
				type = parsedJson.read("$.hook") != null ? "hook" : "unknown";
			}
			catch (PathNotFoundException ex) {
				type = "unknown";
			}
		}
		String action;
		try {
			action = parsedJson.read("$.action", String.class);
		}
		catch (Exception e) {
			action = "updated";
		}
		Pojo pojo = new Pojo(username, repo, type, action);
		this.source.output().send(MessageBuilder.createMessage(pojo, new MessageHeaders(headers)));
		return pojo;
	}

}
