<<<<<<< Updated upstream:src/main/java/com/qpoos/erp/purchase/transaction/infrastructure/TransactionLineRepository.java
package com.qpoos.erp.purchase.transaction.infrastructure;

import com.qpoos.erp.purchase.transaction.domain.TransactionLineEntity;
=======
package com.qpoos.erp.expenses.bills.repository;

import com.qpoos.erp.expenses.bills.entity.TransactionLineEntity;
>>>>>>> Stashed changes:src/main/java/com/qpoos/erp/expenses/bills/repository/TransactionLineRepository.java
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionLineRepository extends JpaRepository<TransactionLineEntity, Long> {
    List<TransactionLineEntity> findAllByTransaction_IdOrderByLineNoAsc(Long transactionId);

    List<TransactionLineEntity> findAllByTransaction_IdAndTransaction_Company_IdOrderByLineNoAsc(
            Long transactionId,
            UUID companyId
    );

    @Modifying(flushAutomatically = true)
    @Transactional
    @Query("delete from TransactionLineEntity line where line.transaction.id = :transactionId")
    void deleteAllByTransactionId(@Param("transactionId") Long transactionId);
}
