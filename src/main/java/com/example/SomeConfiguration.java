package com.example;

import com.rometools.rome.feed.synd.SyndEntry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.feed.inbound.FeedEntryMessageSource;
import org.springframework.messaging.Message;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Marcin Grzejszczak
 */
@Configuration
@EnableConfigurationProperties(ConfigProps.class)
public class SomeConfiguration {

	@Bean
	@InboundChannelAdapter(value = Source.OUTPUT)
	FeedEntryMessageSource feedEntryMessageSource(ConfigProps configProps) throws MalformedURLException {
		return new FeedEntryMessageSource(new URL(configProps.getUrl()), configProps.getMetadataKey()) {
			@Override
			public Message<SyndEntry> receive() {
				try {
					return super.receive();
				} catch (Exception e) {
					return null;
				}
			}
		};
	}

}
