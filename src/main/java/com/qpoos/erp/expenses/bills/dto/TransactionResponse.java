package com.qpoos.erp.expenses.bills.dto;

import com.qpoos.erp.expenses.bills.entity.PaymentStatus;
import com.qpoos.erp.expenses.bills.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record TransactionResponse(
        Long id,
        UUID companyId,
        LocalDate transactionDate,
        TransactionType transactionType,
        String billNo,
        Long vendorId,
        LocalDate dueDate,
        String referenceNo,
        BigDecimal subtotal,
        BigDecimal discountAmount,
        BigDecimal taxAmount,
        BigDecimal roundOff,
        BigDecimal totalAmount,
        LocalDate paymentDate,
        PaymentStatus paymentStatus,
        String notes,
        String attachments,
        UUID createdBy,
        UUID updatedBy,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        List<TransactionLineResponse> lines
) {
}
