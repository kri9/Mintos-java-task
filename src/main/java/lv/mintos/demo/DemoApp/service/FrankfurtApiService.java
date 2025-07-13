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
@Order(1)
@Component
@RequiredArgsConstructor
public class FrankfurtApiService implements ExchangeRateService {

    private final RestTemplate restTemplate;
    private final ExchangeApiConfig exchangeApiConfig;

    @Override
    public Optional<BigDecimal> getExchangeRate(String fromCurrency, String toCurrency) {
        try {
            log.info("Using frankfurt API");
            if (fromCurrency.equals(toCurrency)) {
                log.trace("Source and target currency are same. Returning 1 as rate immediately to avoid API error");
                return Optional.of(BigDecimal.ONE);
            }
            return Optional.ofNullable(restTemplate.getForEntity(getUrl(fromCurrency, toCurrency), JsonNode.class).getBody())
                    .map(r -> r.get("rates"))
                    .map(r -> r.get(toCurrency).asText())
                    .map(BigDecimal::new);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private String getUrl(String fromCurrency, String toCurrency) {
        ExchangeApiConfig.Config config = getConfig(exchangeApiConfig);
        return config.url() + String.format("?base=%s&symbols=%s", fromCurrency, toCurrency);
    }

    @Override
    public String getServiceId() {
        return "frankfurtApi";
    }
}
