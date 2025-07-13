package lv.mintos.demo.DemoApp.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionRequestDTO(@NotNull Long sourceAccount,
                                    @NotNull Long destinationAccount,
                                    @NotNull BigDecimal amount,
                                    String description) {
}
