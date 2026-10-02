package ch.bbw.obelix.webshop;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("obelix.quarry")
public record QuarryProperties(String baseUrl) {}
