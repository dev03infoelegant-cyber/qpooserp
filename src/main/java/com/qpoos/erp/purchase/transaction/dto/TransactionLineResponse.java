package com.qpoos.erp.purchase.transaction.dto;

import com.qpoos.erp.purchase.transactionline.domain.TransactionLineType;

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
        Boolean taxable,
        BigDecimal taxRate,
        BigDecimal taxAmount,
        BigDecimal amount
) {
}
