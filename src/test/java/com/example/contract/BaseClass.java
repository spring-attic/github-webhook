package com.example.contract;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

import com.example.DemoApplication;
import com.example.Pojo;
import com.example.TestDbAccessor;
import com.example.TransformerController;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@AutoConfigureMessageVerifier
public class BaseClass {

	@Autowired TransformerController transformerController;
	@Inject MessageVerifier messaging;

	@Value("classpath:/github-webhook-input/issue-created.json") Resource issueCreatedInput;
	@Value("classpath:/github-webhook-input/hook-created.json") Resource hookCreatedInput;

	@Before
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(transformerController);
		this.messaging.receive("output", 100, TimeUnit.MILLISECONDS);
		TestDbAccessor.getPojos().clear();
		TestDbAccessor.getPojos().addAll(Arrays.asList(
						new Pojo("dsyer", "spring-cloud-samples", "hook", "updated", "d syer"),
						new Pojo("smithapitla", "spring-cloud/spring-cloud-netflix", "issue", "created", "smith apitla"))
		);
	}

	public void createHook() throws IOException  {
		this.transformerController.transform(read(hookCreatedInput));
	}

	public void createHookV2() throws IOException  {
		createHook();
	}

	public void createIssue() throws IOException  {
		this.transformerController.transform(read(issueCreatedInput));
	}

	public void createIssueV2() throws IOException  {
		createIssue();
	}

	private String read(Resource resource) throws IOException {
		return FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream()));
	}
}
