package com.qpoos.erp.controller;

import com.qpoos.erp.dto.auth.MessageResponse;
import com.qpoos.erp.dto.company.CompanyRequest;
import com.qpoos.erp.dto.company.CompanyResponse;
import com.qpoos.erp.security.AuthenticatedUser;
import com.qpoos.erp.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyResponse> create(
            @Valid @RequestBody CompanyRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(companyService.create(AuthenticatedUser.id(authentication), request));
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> list(Authentication authentication) {
        return ResponseEntity.ok(companyService.list(AuthenticatedUser.id(authentication)));
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> get(
            @PathVariable UUID companyId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(companyService.get(AuthenticatedUser.id(authentication), companyId));
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> update(
            @PathVariable UUID companyId,
            @Valid @RequestBody CompanyRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(companyService.update(AuthenticatedUser.id(authentication), companyId, request));
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<MessageResponse> delete(
            @PathVariable UUID companyId,
            Authentication authentication
    ) {
        companyService.delete(AuthenticatedUser.id(authentication), companyId);
        return ResponseEntity.ok(new MessageResponse("Company deleted successfully."));
    }

}
