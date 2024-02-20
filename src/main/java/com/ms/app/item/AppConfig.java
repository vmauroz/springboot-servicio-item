package com.ms.app.item;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class AppConfig {

	@Bean
	public RestTemplate registrarRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> {
			return new Resilience4JConfigBuilder(id)
					// Maneja el proceso de resiliencia tolerancia a fallos.
					.circuitBreakerConfig(CircuitBreakerConfig.custom()
							// Tamaño de la ventana deslizante por defecto es 100
							.slidingWindowSize(10)
							// Taza umbral de fallas por defecto es un 50%
							.failureRateThreshold(50)
							// por defecto 60 segundos
							.waitDurationInOpenState(Duration.ofSeconds(10L))
							// número de llamados en estado semi abierto
							.permittedNumberOfCallsInHalfOpenState(5)
							// configura en porcentale las llamadas lentas
							.slowCallRateThreshold(50)
							// configura el tiempo máximo en ejecutar una llama, 
							// si pasa el 50% es una llamada lenta
							.slowCallDurationThreshold(Duration.ofSeconds(2L))
							.build())
					//controla el timeout,custom() personaliza
//					.timeLimiterConfig(TimeLimiterConfig.ofDefaults())
					.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(6L)).build()) 

					.build();
			
		});
	}
}
