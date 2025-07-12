package lv.mintos.demo.DemoApp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "exchanges.apis")
public record ExchangeApiConfig(List<Config> configs) {

    public record Config(String url, String serviceId) {
    }
}
