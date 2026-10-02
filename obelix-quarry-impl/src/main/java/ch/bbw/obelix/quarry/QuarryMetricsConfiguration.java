package ch.bbw.obelix.quarry;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuarryMetricsConfiguration {

	@Bean
	public MeterBinder menhirCountGauge(QuarryRepository menhirRepository) {
		return registry -> Gauge.builder("obelix.menhirs", menhirRepository::count)
			.description("Number of menhirs currently lying around in Obelix's quarry")
			.register(registry);
	}
}
