package ch.bbw.obelix.webshop;

import ch.bbw.obelix.quarry.api.QuarryApi;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@EnableConfigurationProperties(QuarryProperties.class)
public class QuarryClientConfiguration {

	@Bean
	public QuarryApi quarryApi(WebClient.Builder webClientBuilder, QuarryProperties quarryProperties) {
		var webClient = webClientBuilder.baseUrl(quarryProperties.baseUrl()).build();
		var factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();
		return factory.createClient(QuarryApi.class);
	}
}
