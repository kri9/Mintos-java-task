package lv.mintos.demo.DemoApp;

import lv.mintos.demo.DemoApp.config.ExchangeApiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ExchangeApiConfig.class)
public class DemoAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoAppApplication.class, args);
    }

}
