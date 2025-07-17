package lv.mintos.demo.DemoApp;

import lv.mintos.demo.DemoApp.config.ExchangeApiConfig;
import lv.mintos.demo.DemoApp.service.ExchangeApiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ExchangeApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Test
    void returnsEmptyWhenApiFails() {
        ExchangeApiService service = new ExchangeApiService(restTemplate,
                new ExchangeApiConfig(List.of(new ExchangeApiConfig.Config("http://dummy", "exchangerateApi"))));

        Optional<BigDecimal> rate = service.getExchangeRate("USD", "EUR");
        assertEquals(Optional.empty(), rate);
    }
}
