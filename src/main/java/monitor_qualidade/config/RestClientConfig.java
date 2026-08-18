package monitor_qualidade.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient webContinentalRestClient(RestClient.Builder builder) {

        // Configuração de Timeouts para evitar travamento em cascata (Circuit Breaker passivo)
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(3)); // Máximo de 3 segundos para estabelecer conexão
        factory.setReadTimeout(Duration.ofSeconds(5));    // Máximo de 5 segundos esperando a resposta do servidor

        return builder
                .requestFactory(factory)
                // .baseUrl("https://api.sandbox.webcontinental.com.br") // URL base centralizada (descomentar quando tiver o endpoint oficial)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}