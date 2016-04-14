package org.springframework.cloud.stream.test.stub;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.config.ChannelBindingAutoConfiguration;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Mark Fisher
 */
@Configuration
@AutoConfigureAfter(ChannelBindingAutoConfiguration.class)
@EnableConfigurationProperties(StubMessageProducerProperties.class)
@PropertySource("classpath:stub.properties")
public class StubMessageProducerAutoConfiguration {

	@Bean
	@ConditionalOnBean(name = "org.springframework.cloud.stream.messaging.Sink")
	public ResourceReadingStubMessageProducer stubMessageProducer(Sink sink) {
		ResourceReadingStubMessageProducer producer = new ResourceReadingStubMessageProducer();
		producer.setOutputChannel(sink.input());
		return producer;
	}
}
