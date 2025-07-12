package lv.mintos.demo.DemoApp.dto;

import java.math.BigDecimal;

public record TransactionDTO(Long id,
                             Long sourceAccountId,
                             Long destinationAccountId,
                             Double amount,
                             String sourceCurrency,
                             String destinationCurrency,
                             BigDecimal exchangeRate
) {
}
