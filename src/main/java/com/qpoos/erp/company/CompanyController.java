package com.qpoos.erp.company;

import com.qpoos.erp.auth.dto.MessageResponse;
import com.qpoos.erp.common.security.SecurityUtils;
import com.qpoos.erp.company.dto.CompanyRequest;
import com.qpoos.erp.company.dto.CompanyResponse;
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
                .body(companyService.create(SecurityUtils.getUserId(authentication), request));
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> list(Authentication authentication) {
        return ResponseEntity.ok(companyService.list(SecurityUtils.getUserId(authentication)));
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> get(
            @PathVariable UUID companyId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(companyService.get(SecurityUtils.getUserId(authentication), companyId));
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> update(
            @PathVariable UUID companyId,
            @Valid @RequestBody CompanyRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(companyService.update(SecurityUtils.getUserId(authentication), companyId, request));
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<MessageResponse> delete(
            @PathVariable UUID companyId,
            Authentication authentication
    ) {
        companyService.delete(SecurityUtils.getUserId(authentication), companyId);
        return ResponseEntity.ok(new MessageResponse("Company deleted successfully."));
    }
}
