package org.url_shortener_mp.analytics_service.config.kafka;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxConfig {
    @Bean
    public InfluxDBClient initiateClient() {
        String token = System.getenv("INFLUX_TOKEN");
        String bucket = "url";
        String org = "mk";
        InfluxDBClient client = InfluxDBClientFactory.create("http://localhost:8086", token.toCharArray(),org,bucket);
        return client;
    }
}
