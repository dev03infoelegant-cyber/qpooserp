package com.qpoos.erp.expenses.bills.dto;

import com.qpoos.erp.expenses.bills.entity.PaymentStatus;
import com.qpoos.erp.expenses.bills.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionListResponse(
        Long id,
        LocalDate transactionDate,
        TransactionType transactionType,
        String billNo,
        Long vendorId,
        String vendorName,
        LocalDate dueDate,
        BigDecimal totalAmount,
        PaymentStatus paymentStatus
) {
}
