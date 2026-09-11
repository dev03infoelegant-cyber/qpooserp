package com.qpoos.erp.expenses.bills.dto;

import com.qpoos.erp.expenses.bills.entity.TransactionLineType;

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
