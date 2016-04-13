package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.BDDAssertions.then;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
@WebIntegrationTest
public class DemoApplicationTests {

	@Autowired MessageCollector messageCollector;
	@Autowired Source source;

	@Test
	public void contextLoads() throws InterruptedException {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<String> httpEntity = new HttpEntity<>("{\"foo\":\"bar\"}", httpHeaders);
		new TestRestTemplate().exchange("http://localhost:8080/", HttpMethod.POST, httpEntity, String.class);

		Message<?> message = messageCollector.forChannel(source.output()).poll(5, TimeUnit.SECONDS);

		then(message).isNotNull();
		then(message.getHeaders()).containsEntry(MessageHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
	}

}
