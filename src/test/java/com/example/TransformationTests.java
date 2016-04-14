package com.example;

import static org.assertj.core.api.BDDAssertions.then;

import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.util.FileCopyUtils;

public class TransformationTests {

	private TransformerConfiguration transformer = new TransformerConfiguration();
	private Source source = () -> new QueueChannel();

	@Before
	public void init() {
		this.transformer.source = this.source;
	}

	@Test
	public void noRepository() throws InterruptedException, IOException {
		Resource resource = new ClassPathResource("/webhooks/no-repo.json");
		String json = FileCopyUtils
				.copyToString(new InputStreamReader(resource.getInputStream()));
		Pojo pojo = this.transformer.transform(json);
		then(pojo).isNotNull();
	}

}
