package lv.mintos.demo.DemoApp.dto;

import java.math.BigDecimal;

public record AccountDTO(Long id, String client, String currency, BigDecimal balance) {
}
