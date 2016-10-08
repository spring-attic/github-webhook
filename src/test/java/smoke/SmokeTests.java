package smoke;

import java.lang.invoke.MethodHandles;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmokeTests.class,
		webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnableAutoConfiguration
public class SmokeTests {

	private static final Log log = LogFactory.getLog(MethodHandles.lookup().lookupClass());

	@Value("${stubrunner.url}") String stubRunnerUrl;
	@Value("${application.url}") String applicationUrl;

	TestRestTemplate testRestTemplate = new TestRestTemplate();

	@Test
	public void shouldWork() {
		ResponseEntity<String> entity = this.testRestTemplate
				.getForEntity("http://" + this.applicationUrl + "/health", String.class);

		then(entity.getStatusCode().is2xxSuccessful()).isTrue();
	}

}
