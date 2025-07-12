package lv.mintos.demo.DemoApp.model.repo;

import lv.mintos.demo.DemoApp.model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select t from Transaction t where t.destinationAccount.id=?1 or t.sourceAccount.id=?1")
    List<Transaction> findAllBySourceAccountOrDestinationAccount(Long accountId, Pageable pageable);
}
