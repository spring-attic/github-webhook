package com.example.accurest;

import com.example.DemoApplication;
import com.example.TransformerConfiguration;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
public class BaseClass {

	@Autowired TransformerConfiguration transformerConfiguration;

	@Value("classpath:/github-webhook-input/issue-created.json") Resource issueCreatedInput;
	@Value("classpath:/github-webhook-input/hook-created.json") Resource hookCreatedInput;

	@Before
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(transformerConfiguration);
	}

	public void createHook() throws IOException  {
		transformerConfiguration.transform(read(hookCreatedInput));
	}

	public void createHookV2() throws IOException  {
		createHook();
	}

	public void createIssue() throws IOException  {
		transformerConfiguration.transform(read(issueCreatedInput));
	}

	public void createIssueV2() throws IOException  {
		createIssue();
	}

	private String read(Resource resource) throws IOException {
		return FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream()));
	}
}
