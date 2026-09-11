package com.qpoos.erp.expenses.bills.dto;

<<<<<<< Updated upstream:src/main/java/com/qpoos/erp/purchase/transaction/dto/TransactionLineResponse.java
import com.qpoos.erp.purchase.transaction.domain.TransactionLineType;
=======
import com.qpoos.erp.expenses.bills.entity.TransactionLineType;
>>>>>>> Stashed changes:src/main/java/com/qpoos/erp/expenses/bills/dto/TransactionLineResponse.java

import java.math.BigDecimal;

public record TransactionLineResponse(
        Long id,
        Integer lineNo,
        TransactionLineType lineType,
        Long itemId,
        Long accountId,
        String description,
        BigDecimal quantity,
        String unit,
        BigDecimal rate,
        BigDecimal discount,
        BigDecimal taxRate,
        BigDecimal taxAmount,
        BigDecimal amount
) {
}
