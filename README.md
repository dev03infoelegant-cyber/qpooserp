# QPOOS ERP

QPOOS ERP is a modular Spring Boot backend for an ERP system focused on accounting, company management, customer operations, inventory, and expense workflows. The project is designed as a modular monolith so it can later evolve into separate services without changing the core domain structure.

## Project Goal

This project aims to provide a clean base for business operations with:

- modular domain separation
- company-aware business logic
- accounting and ledger support
- role-based authentication and authorization
- future-ready structure for service extraction

## Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Data JPA
- Spring Security
- PostgreSQL / H2 (for tests)
- Maven Wrapper
- Lombok
- OpenAPI / Springdoc

## Project Structure

```text
src/
  main/
    java/
      com/
        qpoos/
          erp/
            accounting/
            auth/
            common/
            company/
            customerhub/
            expenses/
            inventory/
            user/
            vendor/
            ErpApplication.java
    resources/
      application.properties
  test/
    java/
      com/
        qpoos/
          erp/
```

## Domain Layout Pattern

Each business module follows a practical structure:

```text
module/
  controller/
  service/
  dto/
  entity/
  repository/
  mapper/
  config/
```

This keeps the code easier to understand and supports future modular growth.

## Accounting Model Overview

The accounting layer is designed around double-entry bookkeeping:

- every business event creates a balanced journal entry
- each journal entry has debit and credit lines
- accounts are grouped by type and category
- ledger balances are maintained from journal postings
- financial reports are generated from the ledger and account balances

The accounting module contains the core bookkeeping logic and should be treated as the source of truth for financial postings.

## Business Flow

The general flow is:

1. A business action occurs in a domain module such as sales, expenses, customer, or vendor.
2. The domain module triggers the accounting service.
3. The accounting service validates the accounts and period.
4. A journal entry is generated with equal debit and credit totals.
5. The ledger and balances are updated.
6. Reports are generated from the stored accounting data.

## Authentication and Authorization

The project includes:

- user management
- company scoping
- refresh-token handling
- email verification flow
- password reset flow
- JWT-based access control

## Run the Project

From the project root:

### CMD

```cmd
cd /d "D:\New folder (5)\qpooserp"
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.12.1
set PATH=%JAVA_HOME%\bin;%PATH%
mvnw.cmd clean spring-boot:run
```

### PowerShell

```powershell
Set-Location "D:\New folder (5)\qpooserp"
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.1"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
.\mvnw.cmd clean spring-boot:run
```

## Important Notes

- Use Java 21 for build and runtime.
- The application should run with the Maven wrapper from the project root.
- Avoid creating duplicate repository/service bean definitions across the same package tree.
- Keep business modules and accounting logic separated to preserve a clean accounting model.

## Future Roadmap

This project is currently structured as a modular monolith, but it is ready for future extraction into independent service domains such as:

- accounting service
- customer service
- inventory service
- billing/expenses service
- auth service

## Related Documentation

- [src/main/java/com/qpoos/erp/accounting/README.md](src/main/java/com/qpoos/erp/accounting/README.md)

## License

This project is for internal ERP development and learning purposes unless a specific license is added later.
