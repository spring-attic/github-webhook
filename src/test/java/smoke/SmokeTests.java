package smoke;

import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
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

	@Value("${stubrunner.url}") String stubRunnerUrl;
	@Value("${application.url}") String applicationUrl;
	@Value("${test.timeout:60}") Long timeout;

	TestRestTemplate testRestTemplate = new TestRestTemplate();

	@Test
	public void shouldWork() {
		Awaitility.await().atMost(this.timeout, TimeUnit.SECONDS).untilAsserted(() -> {
			ResponseEntity<String> entity = this.testRestTemplate
					.getForEntity("http://" + this.applicationUrl + "/health", String.class);

			then(entity.getStatusCode().is2xxSuccessful()).isTrue();
		});
	}

}
