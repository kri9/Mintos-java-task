package lv.mintos.demo.DemoApp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.mintos.demo.DemoApp.dto.AccountDTO;
import lv.mintos.demo.DemoApp.dto.TransactionDTO;
import lv.mintos.demo.DemoApp.dto.TransactionRequestDTO;
import lv.mintos.demo.DemoApp.mapper.MainMapper;
import lv.mintos.demo.DemoApp.model.repo.AccountRepository;
import lv.mintos.demo.DemoApp.model.repo.ClientRepository;
import lv.mintos.demo.DemoApp.model.repo.TransactionRepository;
import lv.mintos.demo.DemoApp.service.TransactionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MainMapper mainMapper;
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    @GetMapping("/client/{id}/accounts")
    public List<AccountDTO> getClientAccounts(@PathVariable("id") Long id) {
        return clientRepository.findById(id).stream()
                .flatMap(c -> c.getAccounts().stream())
                .map(mainMapper::accountToAccountDTO)
                .toList();
    }

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream()
                .map(mainMapper::accountToAccountDTO)
                .toList();
    }

    @GetMapping("/account/{id}/transactions")
    public List<TransactionDTO> getAccountTransactions(@PathVariable("id") Long id,
                                                       @RequestParam("offset") Integer offset,
                                                       @RequestParam("limit") Integer limit) {
        return transactionRepository.findAllBySourceAccountOrDestinationAccount(id, PageRequest.of(offset / limit, limit))
                .stream()
                .map(mainMapper::transactionToTransactionDTO)
                .toList();
    }

    @PostMapping("/transaction")
    public TransactionDTO createTransaction(@RequestBody @Valid TransactionRequestDTO requestDTO) {
        return transactionService.executeTransaction(requestDTO);
    }

}
