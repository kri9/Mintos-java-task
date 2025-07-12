package lv.mintos.demo.DemoApp.mapper;

import lv.mintos.demo.DemoApp.dto.AccountDTO;
import lv.mintos.demo.DemoApp.dto.TransactionDTO;
import lv.mintos.demo.DemoApp.model.Account;
import lv.mintos.demo.DemoApp.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MainMapper {

    @Mapping(source = "client.name", target = "client")
    AccountDTO accountToAccountDTO(Account account);

    @Mapping(target = "sourceAccountId", source = "sourceAccount.id")
    @Mapping(target = "destinationAccountId", source = "destinationAccount.id")
    TransactionDTO transactionToTransactionDTO(Transaction transaction);
}
