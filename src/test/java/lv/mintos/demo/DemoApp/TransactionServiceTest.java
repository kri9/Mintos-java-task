package lv.mintos.demo.DemoApp;

import com.github.benmanes.caffeine.cache.Cache;
import lv.mintos.demo.DemoApp.dto.TransactionRequestDTO;
import lv.mintos.demo.DemoApp.err.AppLogicException;
import lv.mintos.demo.DemoApp.mapper.MainMapper;
import lv.mintos.demo.DemoApp.model.Account;
import lv.mintos.demo.DemoApp.model.repo.AccountRepository;
import lv.mintos.demo.DemoApp.model.repo.TransactionRepository;
import lv.mintos.demo.DemoApp.service.ExchangeRateService;
import lv.mintos.demo.DemoApp.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private MainMapper mainMapper;

    @Mock
    private Cache<String, BigDecimal> exchangeRateCache;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Test
    void throwsWhenInsufficientBalance() {
        Account source = new Account();
        source.setCurrency("USD");
        source.setBalance(new BigDecimal("10.00"));

        Account dest = new Account();
        dest.setCurrency("USD");

        when(accountRepository.getReferenceById(1L)).thenReturn(source);
        when(accountRepository.getReferenceById(2L)).thenReturn(dest);

        TransactionRequestDTO request = new TransactionRequestDTO(1L, 2L, new BigDecimal("100.00"), "desc");

        AppLogicException ex = assertThrows(AppLogicException.class, () ->
                transactionService.executeTransaction(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    void executesTransactionWithSameCurrency() {
        Account source = new Account();
        source.setCurrency("USD");
        source.setBalance(new BigDecimal("200.00"));

        Account dest = new Account();
        dest.setCurrency("USD");
        dest.setBalance(new BigDecimal("100.00"));

        when(accountRepository.getReferenceById(1L)).thenReturn(source);
        when(accountRepository.getReferenceById(2L)).thenReturn(dest);
        when(transactionRepository.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));
        when(mainMapper.transactionToTransactionDTO(Mockito.any())).thenReturn(null);

        Cache<String, BigDecimal> cacheMock = Mockito.mock(Cache.class);
        when(cacheMock.getIfPresent("USD-USD")).thenReturn(BigDecimal.ONE);
        transactionService = new TransactionService(mainMapper, accountRepository, List.of(), cacheMock, transactionRepository);

        TransactionRequestDTO request = new TransactionRequestDTO(1L, 2L, new BigDecimal("50.00"), "test");
        transactionService.executeTransaction(request);

        assertEquals(new BigDecimal("150.00"), dest.getBalance());
    }


}
