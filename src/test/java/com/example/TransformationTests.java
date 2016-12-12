package com.example;

import static org.assertj.core.api.BDDAssertions.then;


import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.util.FileCopyUtils;

public class TransformationTests {

	private TransformerController transformer = new TransformerController();
	private Source source = () -> new QueueChannel();

	@Before
	public void init() {
		this.transformer.source = this.source;
	}

	@Test
	public void noRepository() throws InterruptedException, IOException {
		Resource resource = new ClassPathResource("/github-webhook-input/hook-created.json");
		String json = FileCopyUtils
				.copyToString(new InputStreamReader(resource.getInputStream()));
		Pojo pojo = this.transformer.transform(json);
		then(pojo).isNotNull();
	}

	@Test
	public void thisTestWillFail() {
		Assert.assertTrue(false);
	}
}
