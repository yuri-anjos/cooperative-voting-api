package br.com.yurifranca.cooperative_voting_api.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Bean
    RestClient restClient() {

        var factory = new JdkClientHttpRequestFactory();

        factory.setReadTimeout(Duration.ofSeconds(3));
        factory.setReadTimeout(Duration.ofSeconds(2));

        return RestClient.builder()
                .requestFactory(factory)
                .build();
    }

}