package com.example;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.PathNotFoundException;
import com.toomuchcoding.jsonassert.JsonPath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Marcin Grzejszczak
 */
@RestController
public class TransformerController {

	@Autowired Source source;
	static final List<Pojo> DATABASE = Collections.synchronizedList(new ArrayList<>());

	@PostConstruct
	public void simulateInitialState() {
		DATABASE.add(new Pojo("test1", "test1/test1", "hook", "updated", "test one"));
		DATABASE.add(new Pojo("test2", "test2/test2", "issue", "created", "test two"));
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public Pojo transform(@RequestBody String message) {
		DocumentContext parsedJson = com.jayway.jsonpath.JsonPath.parse(message);
		String username = parsedJson
				.read(JsonPath.builder().field("sender").field("login").jsonPath());
		String repo;
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

		// @TODO: get this value from Github using ID
		String userfullname = "unknown";
		
		Pojo pojo = new Pojo(username, repo, type, action, userfullname);
		this.source.output().send(MessageBuilder.createMessage(pojo, new MessageHeaders(headers)));
		DATABASE.add(pojo);
		return pojo;
	}

	@GetMapping(value = "/")
	public Pojos pojos() {
		return new Pojos(DATABASE);
	}


}
