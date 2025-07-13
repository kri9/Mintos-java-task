package lv.mintos.demo.DemoApp.service;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.mintos.demo.DemoApp.dto.TransactionDTO;
import lv.mintos.demo.DemoApp.dto.TransactionRequestDTO;
import lv.mintos.demo.DemoApp.err.AppLogicException;
import lv.mintos.demo.DemoApp.mapper.MainMapper;
import lv.mintos.demo.DemoApp.model.Account;
import lv.mintos.demo.DemoApp.model.Transaction;
import lv.mintos.demo.DemoApp.model.repo.AccountRepository;
import lv.mintos.demo.DemoApp.model.repo.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionService {

    private final MainMapper mainMapper;
    private final AccountRepository accountRepository;
    private final List<ExchangeRateService> serviceList;
    private final Cache<String, BigDecimal> exchangeRateCache;
    private final TransactionRepository transactionRepository;

    @Transactional
    public TransactionDTO executeTransaction(TransactionRequestDTO request) {
        log.info("Executing transaction");
        Account srcAccount = accountRepository.getReferenceById(request.sourceAccount());
        Account destAccount = accountRepository.getReferenceById(request.destinationAccount());
        if (srcAccount.getBalance().compareTo(request.amount()) < 0) {
            throw new AppLogicException(HttpStatus.BAD_REQUEST, "Source account has low balance");
        }
        srcAccount.setBalance(srcAccount.getBalance().subtract(request.amount()));
        BigDecimal rate = getExchangeRate(srcAccount.getCurrency(), destAccount.getCurrency());
        log.info("Rate resolved to be: " + rate);
        destAccount.setBalance(destAccount.getBalance().add(request.amount().multiply(rate)));
        accountRepository.save(srcAccount);
        accountRepository.save(destAccount);
        return mainMapper.transactionToTransactionDTO(createTransaction(request, srcAccount, destAccount, rate));
    }

    private Transaction createTransaction(TransactionRequestDTO request, Account srcAccount, Account destAccount, BigDecimal rate) {
        return transactionRepository.save(Transaction.builder()
                .sourceAccount(srcAccount)
                .destinationAccount(destAccount)
                .amount(request.amount())
                .description(request.description())
                .sourceCurrency(srcAccount.getCurrency())
                .destinationCurrency(destAccount.getCurrency())
                .exchangeRate(rate)
                .timestamp(Timestamp.from(Instant.now()))
                .build());
    }

    private BigDecimal getExchangeRate(String sourceCurrency, String destinationCurrency) {
        String key = sourceCurrency + "-" + destinationCurrency;
        if (exchangeRateCache.getIfPresent(key) != null) {
            log.info("Getting exchange rate from cache");
            return exchangeRateCache.getIfPresent(key);
        }
        BigDecimal rate = getExchangeRateFromApi(sourceCurrency, destinationCurrency);
        exchangeRateCache.put(key, rate);
        return rate;
    }

    private BigDecimal getExchangeRateFromApi(String sourceCurrency, String destinationCurrency) {
        return serviceList.stream()
                .map(s -> s.getExchangeRate(sourceCurrency, destinationCurrency))
                .filter(Optional::isPresent)
                .findFirst()
                .flatMap(Function.identity())
                .orElseThrow(() -> new AppLogicException(HttpStatus.SERVICE_UNAVAILABLE, "Unable to determine exchange rate"));
    }
}
