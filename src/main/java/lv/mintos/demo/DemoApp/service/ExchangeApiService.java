package lv.mintos.demo.DemoApp.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.mintos.demo.DemoApp.config.ExchangeApiConfig;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Order(2)
@Component
@RequiredArgsConstructor
public class ExchangeApiService implements ExchangeRateService {

    private final RestTemplate restTemplate;
    private final ExchangeApiConfig exchangeApiConfig;

    @Override
    public Optional<BigDecimal> getExchangeRate(String fromCurrency, String toCurrency) {
        try {
            log.warn("Using backup exchange API");
            return Optional.ofNullable(restTemplate.getForObject(getUrl(fromCurrency, toCurrency), JsonNode.class))
                    .map(r -> r.get("conversion_rate").asText())
                    .map(BigDecimal::new);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private String getUrl(String fromCurrency, String toCurrency) {
        ExchangeApiConfig.Config config = getConfig(exchangeApiConfig);
        return config.url() + String.format("/pair/%s/%s", fromCurrency, toCurrency);
    }

    @Override
    public String getServiceId() {
        return "exchangerateApi";
    }
}
