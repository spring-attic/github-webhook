package com.example;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Marcin Grzejszczak
 */
@ConfigurationProperties("feed")
public class ConfigProps {
	private String url = "http://localhost:8080/feed.xml";

	private String metadataKey = "messages";

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMetadataKey() {
		return metadataKey;
	}

	public void setMetadataKey(String metadataKey) {
		this.metadataKey = metadataKey;
	}
}
