package com.qpoos.erp.purchase.transaction.dto;

import com.qpoos.erp.purchase.transaction.domain.PaymentStatus;
import com.qpoos.erp.purchase.transaction.domain.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionListResponse(
        Long id,
        String transactionNo,
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
