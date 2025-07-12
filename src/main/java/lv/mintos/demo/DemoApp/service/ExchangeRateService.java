package lv.mintos.demo.DemoApp.service;

import lv.mintos.demo.DemoApp.config.ExchangeApiConfig;

import java.math.BigDecimal;

public interface ExchangeRateService {

    BigDecimal getExchangeRate(String fromCurrency, String toCurrency);

    String getServiceId();

    default String getUrl(ExchangeApiConfig config) {
        return config.configs().stream()
                .filter(c -> c.serviceId().equals(getServiceId()))
                .findFirst()
                .map(ExchangeApiConfig.Config::url)
                .orElseThrow(() -> new RuntimeException("Unable to url config for service: " + getServiceId()));
    }

}
