package com.qpoos.erp.accounting.accounttype;

import com.qpoos.erp.accounting.NormalBalance;
import com.qpoos.erp.accounting.StatementType;

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
