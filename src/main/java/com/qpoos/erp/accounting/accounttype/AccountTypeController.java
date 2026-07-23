package com.qpoos.erp.accounting.accounttype;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounting/account-groups/catalog")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class AccountTypeController {

    private final AccountTypeService accountTypeService;

    @GetMapping
    public ResponseEntity<List<AccountTypeResponse>> list() {
        return ResponseEntity.ok(accountTypeService.list());
    }
}
