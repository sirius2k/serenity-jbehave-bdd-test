package kr.co.redbrush.bdd.test.ws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WSConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
