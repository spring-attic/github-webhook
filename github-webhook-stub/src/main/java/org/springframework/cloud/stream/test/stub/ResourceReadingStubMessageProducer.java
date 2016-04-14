package org.springframework.cloud.stream.test.stub;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MimeType;

/**
 * @author Mark Fisher
 */
public class ResourceReadingStubMessageProducer extends MessageProducerSupport implements StubMessageProducer {

	@Autowired
	private StubMessageProducerProperties properties;

	@Override
	public void produce(String location, MimeType contentType) {
		Resource resource = new ClassPathResource(String.format("%s%s%s",
				this.properties.getBaseDirectory(), File.separator, location));
		this.sendMessage(createMessage(resource, contentType));
	}

	private Message<?> createMessage(Resource resource, MimeType contentType) {
		try {
			String json = FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream()));
			return MessageBuilder.withPayload(json)
					.setHeader(MessageHeaders.CONTENT_TYPE, contentType)
					.build();
		}
		catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
