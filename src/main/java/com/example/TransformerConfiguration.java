package com.example;

import com.jayway.jsonpath.DocumentContext;
import com.toomuchcoding.jsonassert.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Marcin Grzejszczak
 */
@RestController
public class TransformerConfiguration {

	@Autowired Source source;

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public Pojo transform(@RequestBody String message) {
		DocumentContext parsedJson = com.jayway.jsonpath.JsonPath.parse(message);
		String username = parsedJson.read(JsonPath.builder().field("sender").field("login").jsonPath());
		String repo = parsedJson.read(JsonPath.builder().field("repository").field("full_name").jsonPath());
		Pojo pojo = new Pojo(username, repo);
		source.output().send(MessageBuilder.withPayload(pojo).build());
		return pojo;
	}

}
