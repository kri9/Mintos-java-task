package lv.mintos.demo.DemoApp;

import lv.mintos.demo.DemoApp.dto.TransactionRequestDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DtoTest {

    @Test
    void testTransactionRequestDTO() {
        TransactionRequestDTO dto = new TransactionRequestDTO(1L, 2L, new BigDecimal("100.00"), "desc");
        assertEquals(1L, dto.sourceAccount());
    }

}
