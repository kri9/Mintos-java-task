package lv.mintos.demo.DemoApp.service;

import lv.mintos.demo.DemoApp.config.ExchangeApiConfig;
import lv.mintos.demo.DemoApp.err.AppLogicException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExchangeRateService {

    Optional<BigDecimal> getExchangeRate(String fromCurrency, String toCurrency);

    String getServiceId();

    default ExchangeApiConfig.Config getConfig(ExchangeApiConfig config) {
        return config.configs().stream()
                .filter(c -> c.serviceId().equals(getServiceId()))
                .findFirst()
                .orElseThrow(() -> new AppLogicException(HttpStatus.I_AM_A_TEAPOT, "Unable to get config for service: " + getServiceId()));
    }

}
