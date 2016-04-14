package org.springframework.cloud.stream.test.stub;

import org.springframework.util.MimeType;

/**
 * @author Mark Fisher
 */
public interface StubMessageProducer {

	void produce(String resourceName, MimeType contentType);

}
