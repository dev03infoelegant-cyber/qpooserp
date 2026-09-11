package com.qpoos.erp.accounting.accounttype.dto;

import com.qpoos.erp.accounting.entity.NormalBalance;
import com.qpoos.erp.accounting.entity.StatementType;

public record AccountTypeResponse(
        Long id,
        String code,
        String name,
        NormalBalance normalBalance,
        StatementType statementType,
        Integer displayOrder,
        Boolean systemDefined
) {
}
