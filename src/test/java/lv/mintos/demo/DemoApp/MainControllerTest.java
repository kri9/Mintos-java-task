package lv.mintos.demo.DemoApp;

import lv.mintos.demo.DemoApp.controller.MainController;
import lv.mintos.demo.DemoApp.dto.AccountDTO;
import lv.mintos.demo.DemoApp.dto.TransactionDTO;
import lv.mintos.demo.DemoApp.dto.TransactionRequestDTO;
import lv.mintos.demo.DemoApp.mapper.MainMapper;
import lv.mintos.demo.DemoApp.model.Account;
import lv.mintos.demo.DemoApp.model.Client;
import lv.mintos.demo.DemoApp.model.Transaction;
import lv.mintos.demo.DemoApp.model.repo.AccountRepository;
import lv.mintos.demo.DemoApp.model.repo.ClientRepository;
import lv.mintos.demo.DemoApp.model.repo.TransactionRepository;
import lv.mintos.demo.DemoApp.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class MainControllerTest {

    @InjectMocks
    private MainController controller;

    @Mock
    private MainMapper mapper;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    void getClientAccountsWhenClientExists() {
        Client client = new Client();
        Account acc = new Account();
        acc.setCurrency("USD");
        acc.setBalance(BigDecimal.TEN);
        client.setAccounts(List.of(acc));

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(mapper.accountToAccountDTO(acc)).thenReturn(new AccountDTO(1L, "Test Client", "USD", BigDecimal.TEN));

        List<AccountDTO> result = controller.getClientAccounts(1L);
        assertEquals(1, result.size());
    }

    @Test
    void getClientAccountsWhenClientDoesNotExist() {
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());
        List<AccountDTO> result = controller.getClientAccounts(999L);
        assertEquals(0, result.size());
    }

    @Test
    void getAccountTransactionsReturnsMappedDTOs() {
        Transaction tx = new Transaction();
        when(transactionRepository.findAllBySourceAccountOrDestinationAccount(1L, PageRequest.of(0, 10)))
                .thenReturn(List.of(tx));
        when(mapper.transactionToTransactionDTO(tx)).thenReturn(
                new TransactionDTO(1L, 1L, 2L, 100.0, "USD", "USD", BigDecimal.ONE)
        );

        List<TransactionDTO> result = controller.getAccountTransactions(1L, 0, 10);
        assertEquals(1, result.size());
    }

    @Test
    void createTransactionCallsService() {
        TransactionRequestDTO request = new TransactionRequestDTO(1L, 2L, BigDecimal.TEN, "desc");
        TransactionDTO response = new TransactionDTO(1L, 1L, 2L, 10.0, "USD", "USD", BigDecimal.ONE);

        when(transactionService.executeTransaction(request)).thenReturn(response);

        TransactionDTO result = controller.createTransaction(request);
        assertEquals(response, result);
    }

}
