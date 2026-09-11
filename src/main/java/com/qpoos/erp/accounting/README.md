# Accounting Module

The accounting module currently provides the chart-of-accounts foundation for QPOOS ERP. It manages account types, account type definitions, company-specific account groups, and ledgers.

The module is designed to support a future double-entry accounting engine with vouchers, journal entries, posting, and financial reports.

## Package Structure

```text
accounting/
├── accountcatalog/
│   ├── controller/
│   ├── service/
│   ├── entity/
│   ├── repository/
│   └── dto/
├── accountgroup/
│   ├── controller/
│   ├── service/
│   ├── entity/
│   ├── repository/
│   └── dto/
├── accounttype/
│   ├── controller/
│   ├── service/
│   ├── entity/
│   ├── repository/
│   └── dto/
├── ledger/
│   ├── controller/
│   ├── service/
│   ├── entity/
│   ├── repository/
│   └── dto/
├── entity/
└── setup/
```

## Accounting Hierarchy

Accounting data follows this hierarchy:

```text
Account Type
    |
    v
Account Type Definition
    |
    v
Company Account Group
    |
    v
Ledger
```

Example:

```text
ASSET
└── CASH_EQUIVALENTS
    └── Cash in Hand
```

## Main Components

### Account Type

Account types are the top-level accounting classifications:

- `ASSET`
- `LIABILITY`
- `EQUITY`
- `INCOME`
- `EXPENSE`

Each account type defines a normal balance and the financial statement where it belongs.

Normal balance rules:

| Account Type | Normal Balance | Statement |
|---|---|---|
| Asset | Debit | Balance Sheet |
| Liability | Credit | Balance Sheet |
| Equity | Credit | Balance Sheet |
| Income | Credit | Profit and Loss |
| Expense | Debit | Profit and Loss |

### Account Type Definition

Account type definitions provide standard categories under an account type.

Examples:

- `CASH_EQUIVALENTS`
- `BANK`
- `ACCOUNTS_RECEIVABLE`
- `ACCOUNTS_PAYABLE`
- `GST_PAYABLE`
- `INCOME`
- `COST_OF_GOODS_SOLD`
- `EXPENSES`

Each definition also specifies a default ledger type, such as:

- `GENERAL`
- `CASH`
- `BANK`
- `CUSTOMER`
- `SUPPLIER`
- `TAX`
- `CONTROL`

### Account Group

Account groups belong to a company and organize related ledgers. Groups may have parent groups, but a parent must use the same account type.

Important rules:

- Account group codes are unique within a company.
- A group cannot be its own parent.
- Circular group hierarchies are rejected.
- Groups are deactivated instead of physically deleted.
- Account data is always scoped to the selected company.

### Ledger

A ledger is the posting-level account used by future transactions and journal entries.

Default ledgers created for a company include:

- Cash in Hand
- Bank Account
- Customer Receivable
- Supplier Payable
- Output GST Payable
- Domestic Sales
- Purchase Account
- Rent Expense
- Capital Account

Ledger codes are generated from the ledger name. For example:

```text
Cash in Hand -> CASH_IN_HAND
```

System-defined ledgers cannot be changed or deactivated through normal user operations.

## API Endpoints

All accounting endpoints require authentication.

### Account Type Catalog

```http
GET /api/accounting/account-groups/catalog
```

Returns the system account types ordered for display.

### Account Type Definitions

```http
GET    /api/accounting/account-types
POST   /api/accounting/account-types
PUT    /api/accounting/account-types/{definitionId}
DELETE /api/accounting/account-types/{definitionId}
```

Creating, updating, and deactivating definitions requires the `ADMIN` role.

### Account Groups

```http
POST   /api/accounting/account-groups
GET    /api/accounting/account-groups
GET    /api/accounting/account-groups/{groupId}
PUT    /api/accounting/account-groups/{groupId}
DELETE /api/accounting/account-groups/{groupId}
```

Creating, updating, and deactivating account groups requires the `ADMIN` role.

### Ledgers

```http
POST   /api/accounting/ledgers
GET    /api/accounting/ledgers
GET    /api/accounting/ledgers/{ledgerId}
PUT    /api/accounting/ledgers/{ledgerId}
DELETE /api/accounting/ledgers/{ledgerId}
```

Ledger operations are restricted to the currently selected company. System-defined ledgers cannot be modified or deactivated.

## Company Isolation

Accounting records are company-specific. Services obtain the active company from the authenticated security context:

```java
SecurityUtils.getCompanyId()
```

This company ID is used when creating, listing, loading, updating, and deactivating account groups and ledgers.

## Application Startup

`AccountingBootstrapService` runs during application startup and:

1. Seeds the five system account types.
2. Seeds standard account type definitions.
3. Finds all active companies.
4. Creates missing account groups for each company.
5. Creates missing default ledgers for each company.
6. Sets missing opening balance dates.
7. Migrates legacy account groups to the current definitions.

The bootstrap process is transactional and safe to run repeatedly because it updates or reuses existing records instead of blindly creating duplicates.

## Double-Entry Accounting Concept

The future posting engine will follow the double-entry rule:

```text
Total Debits = Total Credits
```

Example: a customer payment of 10,000 into the bank:

```text
Debit:  Bank Account        10,000
Credit: Customer Receivable 10,000
```

Example: a rent payment of 20,000 from the bank:

```text
Debit:  Rent Expense 20,000
Credit: Bank Account 20,000
```

## Current Scope

Implemented:

- Account types
- Account type definitions
- Company account groups
- Ledger management
- Opening balance fields
- Company-level accounting setup
- System default chart initialization

Not yet implemented:

- Voucher headers and voucher lines
- Journal entry posting
- Debit and credit balancing validation
- Posting engine
- Voucher numbering
- Financial years and accounting periods
- Ledger transaction history
- Trial balance
- Profit and loss reports
- Balance sheet reports
- Cash flow reports
- GST reports
- Audit trail
- Voucher approval, cancellation, and reversal

The future accounting design is documented in:

```text
docs/qpoos-erp-accounting-design-report.md
```

## Development Guidelines

When extending this module:

1. Keep accounting data scoped by company.
2. Use the existing `controller`, `service`, `entity`, `repository`, and `dto` structure.
3. Preserve system-defined records from user modification.
4. Prefer deactivation over physical deletion for accounting master data.
5. Keep future voucher posting separate from chart-of-accounts setup.
6. Enforce double-entry validation before any financial posting.
7. Use immutable posted entries for reporting and auditability.
