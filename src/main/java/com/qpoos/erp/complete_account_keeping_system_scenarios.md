# Complete Account Keeping & Double-Entry Accounting System

## Purpose

This document defines a practical accounting/account-keeping system for
an ERP. It covers the accounting engine, debit/credit rules, day-to-day
business scenarios, database structure, voucher types, validation, audit
trail, inventory, tax, receivables, payables, loans, assets, and
financial reports.

The central accounting rule is:

> **Total Debit = Total Credit for every posted accounting
> transaction.**

------------------------------------------------------------------------

# 1. Core Accounting Principle

Every financial transaction must have at least one debit and one credit.

### Example: Credit Sale

A customer purchases goods for ₹10,000 on credit:

``` text
Customer A/C       Dr   ₹10,000
    To Sales A/C          ₹10,000
```

### Example: Customer Payment

The customer later pays ₹10,000:

``` text
Bank A/C           Dr   ₹10,000
    To Customer A/C       ₹10,000
```

The important design principle is:

> **Customers, suppliers, cash, bank, sales, purchases, expenses, taxes,
> loans, assets, etc. are all represented through ledger accounts.**

------------------------------------------------------------------------

# 2. Main Modules

A complete accounting system should contain:

``` text
Accounting System
│
├── Company
├── Financial Year
├── Chart of Accounts
├── Ledger
├── Journal
│
├── Sales
│   ├── Quotation
│   ├── Sales Order
│   ├── Sales Invoice
│   ├── Receipt
│   ├── Credit Note
│   └── Sales Return
│
├── Purchase
│   ├── Purchase Order
│   ├── Purchase Bill
│   ├── Payment
│   ├── Debit Note
│   └── Purchase Return
│
├── Cash & Bank
│   ├── Receipt
│   ├── Payment
│   ├── Contra
│   ├── Bank Transfer
│   └── Bank Reconciliation
│
├── Expenses
├── Income
├── Tax
├── Inventory
├── Fixed Assets
├── Loans
├── Payroll
├── Accounts Receivable
├── Accounts Payable
│
├── Reports
│   ├── Day Book
│   ├── Cash Book
│   ├── Bank Book
│   ├── General Ledger
│   ├── Trial Balance
│   ├── Profit & Loss
│   ├── Balance Sheet
│   ├── Receivable Outstanding
│   ├── Payable Outstanding
│   └── Tax Reports
│
└── Year End / Closing
```

------------------------------------------------------------------------

# 3. Chart of Accounts

The Chart of Accounts is the foundation of the accounting system.

## 3.1 Assets

``` text
Assets
│
├── Current Assets
│   ├── Cash
│   ├── Bank
│   ├── Accounts Receivable
│   ├── Inventory
│   ├── Input Tax
│   ├── Prepaid Expenses
│   └── Advances
│
└── Fixed Assets
    ├── Building
    ├── Furniture
    ├── Computer
    ├── Vehicle
    └── Machinery
```

## 3.2 Liabilities

``` text
Liabilities
│
├── Current Liabilities
│   ├── Accounts Payable
│   ├── Tax Payable
│   ├── Salary Payable
│   ├── Outstanding Expenses
│   └── Customer Advances
│
└── Long-Term Liabilities
    ├── Bank Loan
    └── Other Loans
```

## 3.3 Equity

``` text
Equity
│
├── Capital
├── Retained Earnings
└── Drawings
```

## 3.4 Income

``` text
Income
│
├── Sales
├── Service Income
├── Interest Income
└── Other Income
```

## 3.5 Expenses

``` text
Expenses
│
├── Purchase / COGS
├── Salary
├── Rent
├── Electricity
├── Telephone
├── Internet
├── Transportation
├── Advertisement
├── Bank Charges
├── Depreciation
└── Other Expenses
```

------------------------------------------------------------------------

# 4. Account Types

A basic database can use:

``` text
ASSET
LIABILITY
EQUITY
INCOME
EXPENSE
```

Additional classifications are useful:

``` text
CASH
BANK
CUSTOMER
SUPPLIER
TAX
INVENTORY
FIXED_ASSET
LOAN
```

Example account codes:

``` text
1000  Cash
1100  Bank
1200  Accounts Receivable
1300  Inventory

2000  Accounts Payable
2100  Tax Payable
2200  Bank Loan

3000  Capital

4000  Sales
4100  Service Income

5000  Purchase
5100  Salary
5200  Rent
5300  Electricity
5400  Bank Charges
```

------------------------------------------------------------------------

# 5. Double-Entry Rules

A useful general rule:

  Account Type   Increase   Decrease
  -------------- ---------- ----------
  Asset          Debit      Credit
  Expense        Debit      Credit
  Liability      Credit     Debit
  Equity         Credit     Debit
  Income         Credit     Debit

Examples:

-   Bank receives money → Debit Bank.
-   Bank pays money → Credit Bank.
-   Expense occurs → Debit Expense.
-   Income occurs → Credit Income.
-   Customer owes more → Debit Customer.
-   Customer pays → Credit Customer.
-   Supplier payable increases → Credit Supplier.
-   Supplier is paid → Debit Supplier.

------------------------------------------------------------------------

# 6. Journal Architecture

Do not design the system around only one debit account and one credit
account.

A real transaction can have multiple debit and credit lines.

## 6.1 Journal

``` text
Journal
-------
Id
CompanyId
FinancialYearId
VoucherType
VoucherNumber
TransactionDate
ReferenceNumber
Description
TotalDebit
TotalCredit
Status
CreatedBy
CreatedAt
PostedBy
PostedAt
```

## 6.2 JournalLine

``` text
JournalLine
-----------
Id
JournalId
AccountId
Debit
Credit
Description
PartyType
PartyId
TaxId
CostCenterId
```

Example:

``` text
Journal #1001

Date: 15-09-2026
Voucher: SALE-001

Customer A/C       Debit   ₹11,800
Sales A/C          Credit  ₹10,000
Output GST A/C     Credit   ₹1,800

Total Debit  = ₹11,800
Total Credit = ₹11,800
```

------------------------------------------------------------------------

# 7. Voucher Types

Recommended voucher types:

``` text
JV      Journal Voucher
SV      Sales Voucher
PV      Purchase Voucher
RV      Receipt Voucher
PM      Payment Voucher
CV      Contra Voucher
CN      Credit Note
DN      Debit Note
OP      Opening Balance
ADJ     Adjustment
```

------------------------------------------------------------------------

# 8. Complete Day-to-Day Accounting Scenarios

## 8.1 Owner Introduces Capital

Owner puts ₹5,00,000 into the business.

``` text
Cash/Bank A/C       Dr   ₹5,00,000
    To Capital A/C       ₹5,00,000
```

------------------------------------------------------------------------

## 8.2 Cash Deposited Into Bank

₹50,000 cash is deposited into the bank.

``` text
Bank A/C            Dr   ₹50,000
    To Cash A/C          ₹50,000
```

This is a Contra transaction.

------------------------------------------------------------------------

## 8.3 Cash Withdrawn From Bank

₹20,000 is withdrawn from the bank.

``` text
Cash A/C            Dr   ₹20,000
    To Bank A/C          ₹20,000
```

------------------------------------------------------------------------

## 8.4 Bank-to-Bank Transfer

₹1,00,000 is transferred from Bank A to Bank B.

``` text
Bank B A/C          Dr   ₹1,00,000
    To Bank A A/C        ₹1,00,000
```

------------------------------------------------------------------------

# 9. Sales Scenarios

## 9.1 Cash Sale

Goods are sold for ₹10,000 cash.

``` text
Cash A/C            Dr   ₹10,000
    To Sales A/C         ₹10,000
```

------------------------------------------------------------------------

## 9.2 Credit Sale

Goods are sold to a customer for ₹10,000.

``` text
Customer A/C        Dr   ₹10,000
    To Sales A/C         ₹10,000
```

------------------------------------------------------------------------

## 9.3 Credit Sale With Tax

Sale = ₹10,000 and tax = ₹1,800.

``` text
Customer A/C        Dr   ₹11,800
    To Sales A/C         ₹10,000
    To Output Tax A/C     ₹1,800
```

------------------------------------------------------------------------

## 9.4 Customer Full Payment

Customer pays ₹11,800.

``` text
Bank A/C            Dr   ₹11,800
    To Customer A/C      ₹11,800
```

------------------------------------------------------------------------

## 9.5 Customer Partial Payment

Invoice = ₹50,000.

Customer pays ₹20,000.

``` text
Bank A/C            Dr   ₹20,000
    To Customer A/C       ₹20,000
```

Outstanding:

``` text
₹50,000 - ₹20,000 = ₹30,000
```

------------------------------------------------------------------------

## 9.6 Customer Advance

Customer pays ₹25,000 before an invoice is created.

``` text
Bank A/C                  Dr   ₹25,000
    To Customer Advance A/C    ₹25,000
```

When the invoice is created:

``` text
Customer A/C              Dr   ₹50,000
    To Sales A/C                ₹50,000
```

Advance adjustment:

``` text
Customer Advance A/C      Dr   ₹25,000
    To Customer A/C            ₹25,000
```

Remaining customer balance:

``` text
₹25,000
```

------------------------------------------------------------------------

## 9.7 Sales Return

Customer returns goods worth ₹10,000.

``` text
Sales Return A/C          Dr   ₹10,000
    To Customer A/C            ₹10,000
```

If tax also needs adjustment:

``` text
Sales Return A/C          Dr   ₹10,000
Output Tax Adjustment     Dr    ₹1,800
    To Customer A/C            ₹11,800
```

------------------------------------------------------------------------

## 9.8 Credit Note

A credit note reduces the customer's balance.

Example: ₹5,000 adjustment.

``` text
Sales Adjustment A/C      Dr   ₹5,000
    To Customer A/C            ₹5,000
```

The exact tax treatment depends on the reason and applicable tax rules.

------------------------------------------------------------------------

## 9.9 Discount Allowed

Customer owes ₹10,000 but is allowed ₹500 discount and pays ₹9,500.

``` text
Bank A/C                  Dr   ₹9,500
Discount Allowed A/C      Dr     ₹500
    To Customer A/C           ₹10,000
```

------------------------------------------------------------------------

## 9.10 Bad Debt / Customer Write-Off

Customer balance of ₹10,000 is determined to be unrecoverable.

``` text
Bad Debt Expense A/C      Dr   ₹10,000
    To Customer A/C           ₹10,000
```

A real system should record the reason, approval, date, and audit trail.

------------------------------------------------------------------------

## 9.11 Customer Refund

Customer has overpaid and business refunds ₹5,000.

``` text
Customer A/C              Dr   ₹5,000
    To Bank A/C                ₹5,000
```

------------------------------------------------------------------------

## 9.12 Customer Payment Against Multiple Invoices

Customer pays ₹50,000.

``` text
Payment = ₹50,000

Invoice 101 = ₹30,000
Invoice 102 = ₹15,000
Invoice 103 = ₹5,000
```

The accounting entry credits the customer for ₹50,000, while the payment
allocation table distributes the payment among invoices.

------------------------------------------------------------------------

## 9.13 One Invoice Paid by Multiple Payments

Invoice = ₹1,00,000.

Payments:

``` text
Payment 1 = ₹40,000
Payment 2 = ₹30,000
Payment 3 = ₹30,000
```

The invoice becomes fully paid after the third allocation.

------------------------------------------------------------------------

# 10. Purchase Scenarios

## 10.1 Cash Purchase

Goods purchased for ₹15,000 cash.

``` text
Purchase A/C             Dr   ₹15,000
    To Cash A/C               ₹15,000
```

For a perpetual inventory system, use Inventory instead of Purchase
where appropriate:

``` text
Inventory A/C            Dr   ₹15,000
    To Cash A/C               ₹15,000
```

------------------------------------------------------------------------

## 10.2 Credit Purchase

Goods purchased from supplier for ₹50,000.

``` text
Purchase A/C             Dr   ₹50,000
    To Supplier A/C           ₹50,000
```

------------------------------------------------------------------------

## 10.3 Credit Purchase With Tax

Purchase = ₹50,000, input tax = ₹9,000.

``` text
Purchase / Inventory A/C Dr   ₹50,000
Input Tax A/C            Dr    ₹9,000
    To Supplier A/C           ₹59,000
```

------------------------------------------------------------------------

## 10.4 Supplier Payment

Supplier is paid ₹59,000.

``` text
Supplier A/C             Dr   ₹59,000
    To Bank A/C                ₹59,000
```

------------------------------------------------------------------------

## 10.5 Partial Supplier Payment

Supplier bill = ₹1,00,000.

Payment = ₹40,000.

``` text
Supplier A/C             Dr   ₹40,000
    To Bank A/C                ₹40,000
```

Outstanding = ₹60,000.

------------------------------------------------------------------------

## 10.6 Supplier Advance

₹20,000 is paid before goods are received.

``` text
Supplier Advance A/C     Dr   ₹20,000
    To Bank A/C                ₹20,000
```

When the supplier bill is created:

``` text
Purchase / Inventory A/C Dr   ₹50,000
Input Tax A/C            Dr    ₹9,000
    To Supplier A/C           ₹59,000
```

Advance adjustment:

``` text
Supplier A/C             Dr   ₹20,000
    To Supplier Advance A/C   ₹20,000
```

------------------------------------------------------------------------

## 10.7 Purchase Return

Goods worth ₹10,000 are returned to supplier.

``` text
Supplier A/C             Dr   ₹10,000
    To Purchase Return A/C    ₹10,000
```

With tax adjustment:

``` text
Supplier A/C             Dr   ₹11,800
    To Purchase Return A/C    ₹10,000
    To Input Tax A/C           ₹1,800
```

------------------------------------------------------------------------

## 10.8 Debit Note

A debit note may increase or otherwise adjust an amount
recoverable/payable depending on the business reason.

Example where the amount payable to a supplier is increased:

``` text
Purchase Adjustment A/C  Dr   ₹5,000
    To Supplier A/C           ₹5,000
```

The exact entry should be determined from the underlying transaction and
applicable tax rules.

------------------------------------------------------------------------

## 10.9 Discount Received

Supplier balance = ₹10,000.

Business pays ₹9,500.

``` text
Supplier A/C             Dr   ₹10,000
    To Bank A/C                ₹9,500
    To Discount Received A/C     ₹500
```

------------------------------------------------------------------------

## 10.10 Supplier Refund

Supplier refunds ₹5,000.

``` text
Bank A/C                 Dr   ₹5,000
    To Supplier A/C           ₹5,000
```

------------------------------------------------------------------------

# 11. Expense Scenarios

## 11.1 Expense Paid Immediately

Electricity bill = ₹5,000.

``` text
Electricity Expense A/C  Dr   ₹5,000
    To Bank A/C                ₹5,000
```

------------------------------------------------------------------------

## 11.2 Outstanding Expense

Salary of ₹1,00,000 is incurred but not yet paid.

``` text
Salary Expense A/C       Dr   ₹1,00,000
    To Salary Payable A/C      ₹1,00,000
```

Later:

``` text
Salary Payable A/C       Dr   ₹1,00,000
    To Bank A/C                ₹1,00,000
```

------------------------------------------------------------------------

## 11.3 Prepaid Expense

₹60,000 rent is paid for six months.

Initial entry:

``` text
Prepaid Rent A/C         Dr   ₹60,000
    To Bank A/C                ₹60,000
```

Monthly expense:

``` text
Rent Expense A/C         Dr   ₹10,000
    To Prepaid Rent A/C       ₹10,000
```

------------------------------------------------------------------------

## 11.4 Accrued Expense

Internet expense of ₹3,000 is incurred but the bill has not yet arrived.

``` text
Internet Expense A/C     Dr   ₹3,000
    To Accrued Expense A/C     ₹3,000
```

------------------------------------------------------------------------

## 11.5 Expense Reversal

If an accrual of ₹3,000 is no longer required:

``` text
Accrued Expense A/C      Dr   ₹3,000
    To Internet Expense A/C    ₹3,000
```

The exact reversal depends on the original accounting treatment.

------------------------------------------------------------------------

## 11.6 Employee Reimbursement

Employee submits an approved business expense of ₹5,000.

When liability is recognized:

``` text
Business Expense A/C     Dr   ₹5,000
    To Employee Payable A/C    ₹5,000
```

When paid:

``` text
Employee Payable A/C     Dr   ₹5,000
    To Bank A/C                ₹5,000
```

------------------------------------------------------------------------

# 12. Income Scenarios

## 12.1 Service Income

Service provided for ₹25,000 on credit.

``` text
Customer A/C             Dr   ₹25,000
    To Service Income A/C     ₹25,000
```

------------------------------------------------------------------------

## 12.2 Interest Received

Bank interest = ₹1,000.

``` text
Bank A/C                 Dr   ₹1,000
    To Interest Income A/C     ₹1,000
```

------------------------------------------------------------------------

## 12.3 Other Income Received

Other income of ₹5,000 received in bank.

``` text
Bank A/C                 Dr   ₹5,000
    To Other Income A/C       ₹5,000
```

------------------------------------------------------------------------

# 13. Cash & Bank Scenarios

## 13.1 Cash Receipt

``` text
Cash A/C                 Dr
    To Customer / Income A/C
```

## 13.2 Cash Payment

``` text
Expense / Supplier A/C   Dr
    To Cash A/C
```

## 13.3 Bank Receipt

``` text
Bank A/C                 Dr
    To Customer / Income A/C
```

## 13.4 Bank Payment

``` text
Supplier / Expense A/C  Dr
    To Bank A/C
```

## 13.5 Bank Charges

Bank deducts ₹500.

``` text
Bank Charges Expense A/C Dr   ₹500
    To Bank A/C               ₹500
```

## 13.6 Bank Interest

``` text
Bank A/C                 Dr
    To Interest Income A/C
```

------------------------------------------------------------------------

# 14. Cheque Scenarios

A complete system should support:

``` text
Cheque Received
Cheque Deposited
Cheque Cleared
Cheque Bounced
Cheque Issued
Cheque Presented
Cheque Cleared
Cheque Cancelled
```

Depending on the accounting policy, cheque transactions may use an
intermediate account such as:

``` text
Cheques in Hand
Cheques Deposited
Unpresented Cheques
```

Example for a cheque received and immediately recognized as cleared:

``` text
Bank A/C                 Dr
    To Customer A/C
```

For workflows where clearing is tracked separately:

``` text
Cheques in Hand A/C      Dr
    To Customer A/C
```

Then on clearance:

``` text
Bank A/C                 Dr
    To Cheques in Hand A/C
```

A bounced cheque reverses the relevant settlement and may add a bank
charge.

------------------------------------------------------------------------

# 15. Fixed Asset Scenarios

## 15.1 Asset Purchase

Computer purchased for ₹80,000.

``` text
Computer Asset A/C       Dr   ₹80,000
    To Bank A/C               ₹80,000
```

------------------------------------------------------------------------

## 15.2 Asset Purchased on Credit

``` text
Computer Asset A/C       Dr   ₹80,000
    To Supplier A/C           ₹80,000
```

------------------------------------------------------------------------

## 15.3 Depreciation

Monthly depreciation = ₹5,000.

``` text
Depreciation Expense A/C Dr   ₹5,000
    To Accumulated Depreciation A/C ₹5,000
```

------------------------------------------------------------------------

## 15.4 Asset Disposal

Example:

``` text
Original cost       ₹1,00,000
Accumulated dep.    ₹60,000
Book value          ₹40,000
Sale proceeds       ₹50,000
Profit              ₹10,000
```

The asset disposal module should:

1.  Remove original asset cost.
2.  Remove accumulated depreciation.
3.  Record sale proceeds.
4.  Calculate gain/loss.
5.  Post the gain/loss automatically.
6.  Preserve an audit trail.

------------------------------------------------------------------------

## 15.5 Asset Write-Off

If an asset is destroyed or no longer usable, the system should remove
its carrying amount and record the appropriate loss.

------------------------------------------------------------------------

# 16. Loan Scenarios

## 16.1 Loan Received

Bank loan = ₹10,00,000.

``` text
Bank A/C                 Dr   ₹10,00,000
    To Bank Loan A/C          ₹10,00,000
```

------------------------------------------------------------------------

## 16.2 Principal Repayment

Principal repayment = ₹50,000.

``` text
Bank Loan A/C            Dr   ₹50,000
    To Bank A/C               ₹50,000
```

------------------------------------------------------------------------

## 16.3 Interest Payment

Interest = ₹10,000.

``` text
Interest Expense A/C     Dr   ₹10,000
    To Bank A/C               ₹10,000
```

------------------------------------------------------------------------

## 16.4 EMI Payment

If an EMI contains both principal and interest:

``` text
Loan Principal A/C       Dr   ₹50,000
Interest Expense A/C     Dr   ₹10,000
    To Bank A/C               ₹60,000
```

The loan module should maintain:

``` text
Loan Amount
Interest Rate
Start Date
End Date
Installment Amount
Principal
Interest
Outstanding Principal
Payment Schedule
```

------------------------------------------------------------------------

# 17. Owner Drawings

Owner withdraws ₹20,000 for personal use.

``` text
Drawings A/C             Dr   ₹20,000
    To Cash/Bank A/C          ₹20,000
```

This is not normally a business operating expense.

------------------------------------------------------------------------

# 18. Inventory Scenarios

A serious ERP should support perpetual or periodic inventory according
to the business requirements.

## 18.1 Opening Stock

Opening inventory is established as part of opening balances.

------------------------------------------------------------------------

## 18.2 Inventory Purchase

``` text
Inventory A/C            Dr
Input Tax A/C            Dr
    To Supplier / Bank A/C
```

------------------------------------------------------------------------

## 18.3 Inventory Sale

The sales side:

``` text
Customer / Bank A/C      Dr
    To Sales A/C
    To Output Tax A/C
```

The cost side under perpetual inventory:

``` text
COGS A/C                 Dr
    To Inventory A/C
```

------------------------------------------------------------------------

## 18.4 Sales Return

Reverse the relevant revenue/tax and, where applicable, return inventory
to stock and reverse the corresponding COGS.

------------------------------------------------------------------------

## 18.5 Purchase Return

Reverse the relevant inventory/purchase and input tax and reduce the
supplier liability.

------------------------------------------------------------------------

## 18.6 Stock Shortage

``` text
Inventory Adjustment Expense A/C   Dr
    To Inventory A/C
```

------------------------------------------------------------------------

## 18.7 Stock Excess

``` text
Inventory A/C                     Dr
    To Inventory Adjustment Income A/C
```

------------------------------------------------------------------------

## 18.8 Damaged Stock

Damaged stock should be removed from available inventory and recognized
according to the business's accounting policy.

------------------------------------------------------------------------

## 18.9 Stock Transfer

Transfer stock between warehouses without creating revenue:

``` text
Warehouse A Stock
        ↓
Stock Transfer
        ↓
Warehouse B Stock
```

Accounting may not require a financial journal if both locations belong
to the same accounting entity, but inventory movement must be recorded.

------------------------------------------------------------------------

# 19. Tax Scenarios

For an India-focused system, tax configuration should support applicable
indirect-tax structures rather than hard-coding a single rate.

Common GST-related accounts include:

``` text
Input CGST
Input SGST
Input IGST

Output CGST
Output SGST
Output IGST

Tax Payable
Tax Receivable / Refund
```

## 19.1 Local Sale Example

Taxable value = ₹1,00,000.

CGST = ₹9,000.

SGST = ₹9,000.

``` text
Customer A/C             Dr   ₹1,18,000
    To Sales A/C              ₹1,00,000
    To Output CGST A/C           ₹9,000
    To Output SGST A/C           ₹9,000
```

## 19.2 Purchase Example

Taxable value = ₹1,00,000.

Input CGST = ₹9,000.

Input SGST = ₹9,000.

``` text
Purchase / Inventory A/C Dr   ₹1,00,000
Input CGST A/C            Dr       ₹9,000
Input SGST A/C            Dr       ₹9,000
    To Supplier A/C            ₹1,18,000
```

## 19.3 Tax Payment

When a tax liability is settled:

``` text
Tax Payable A/C          Dr
    To Bank A/C
```

Actual tax settlement and adjustment entries should follow the
applicable tax rules and filing process.

------------------------------------------------------------------------

# 20. Accounts Receivable

Every customer should have a ledger.

Example:

``` text
Customer: Rahul Traders

Date       Description          Debit     Credit    Balance

01 Sep     Opening Balance     20,000               20,000
05 Sep     Invoice             10,000               30,000
10 Sep     Payment                        15,000    15,000
15 Sep     Credit Note                     2,000    13,000
```

Outstanding:

``` text
₹13,000
```

The system should support:

``` text
Invoice
Payment
Partial Payment
Advance
Credit Note
Discount
Write-Off
Refund
Payment Allocation
Aging
Credit Limit
Due Date
```

------------------------------------------------------------------------

# 21. Accounts Payable

Example:

``` text
Supplier: ABC Suppliers

Date       Description          Debit     Credit    Balance

01 Sep     Opening Balance                 30,000   30,000
05 Sep     Purchase                        20,000   50,000
10 Sep     Payment              25,000               25,000
```

Outstanding:

``` text
₹25,000
```

The system should support:

``` text
Bill
Payment
Partial Payment
Advance
Debit Note
Purchase Return
Discount
Refund
Payment Allocation
Aging
Due Date
```

------------------------------------------------------------------------

# 22. Payment Allocation Design

Do not store only:

``` text
Payment.CustomerId
Payment.Amount
```

Use allocation.

## Payment

``` text
Payment
-------
Id
CustomerId
Date
Amount
BankOrCashAccountId
ReferenceNumber
```

## PaymentAllocation

``` text
PaymentAllocation
-----------------
Id
PaymentId
InvoiceId
AllocatedAmount
```

Example:

``` text
Payment = ₹50,000

Invoice 101 = ₹30,000
Invoice 102 = ₹15,000
Invoice 103 = ₹5,000
```

This allows one payment to settle multiple invoices.

The same architecture should be used for supplier payments.

------------------------------------------------------------------------

# 23. Opening Balance

At the beginning of a financial year, opening balances must be imported
or entered.

Example:

``` text
Cash                    Dr   ₹50,000
Bank                    Dr   ₹2,00,000
Customer                Dr   ₹80,000

Supplier                     ₹60,000
Capital                      ₹2,70,000
```

The system must validate:

``` text
Total Opening Debit
=
Total Opening Credit
```

Customer and supplier opening balances should preferably also preserve
invoice-level or reference-level details when required for aging.

------------------------------------------------------------------------

# 24. Manual Journal Entry

The system should allow authorized users to enter manual journals.

Example:

``` text
Journal Entry

Date: 15/09/2026

Account                       Debit       Credit

Depreciation Expense          10,000
Accumulated Depreciation                  10,000

Total                         10,000       10,000
```

Validation:

``` text
Debit == Credit
```

If not:

``` text
Journal cannot be posted.
Debit and Credit totals do not match.
```

------------------------------------------------------------------------

# 25. Reversal

A posted transaction should not normally be silently edited.

Example original:

``` text
Rent Expense A/C          Dr   ₹10,000
    To Bank A/C                ₹10,000
```

Reversal:

``` text
Bank A/C                  Dr   ₹10,000
    To Rent Expense A/C        ₹10,000
```

Then a corrected transaction can be posted.

------------------------------------------------------------------------

# 26. Transaction Status

Recommended statuses:

``` text
DRAFT
POSTED
CANCELLED
REVERSED
```

Typical lifecycle:

``` text
DRAFT
  ↓
VALIDATE
  ↓
POST
  ↓
POSTED
```

Correction:

``` text
POSTED
  ↓
REVERSE
  ↓
REVERSED
  ↓
NEW CORRECT TRANSACTION
```

------------------------------------------------------------------------

# 27. Financial Period Control

The system should maintain:

``` text
FinancialYear
-------------
Id
CompanyId
StartDate
EndDate
Status
```

Periods can be:

``` text
OPEN
LOCKED
CLOSED
```

A posted transaction should not be allowed in a closed period.

------------------------------------------------------------------------

# 28. Audit Trail

Every accounting action should preserve:

``` text
CreatedBy
CreatedAt
ModifiedBy
ModifiedAt
PostedBy
PostedAt
CancelledBy
CancelledAt
```

An AuditLog can contain:

``` text
AuditLog
--------
Id
UserId
Action
EntityName
EntityId
OldValue
NewValue
Timestamp
```

Important actions:

``` text
Created
Edited
Posted
Cancelled
Reversed
Deleted
Payment Allocated
Payment Unallocated
Period Closed
Period Reopened
```

For posted financial records, hard deletion should normally be
prohibited.

------------------------------------------------------------------------

# 29. Recommended Database Structure

## Company

``` text
Company
-------
Id
Name
TaxNumber
Address
Currency
FinancialYearStart
FinancialYearEnd
```

## Account

``` text
Account
-------
Id
CompanyId
Code
Name
AccountType
ParentAccountId
IsActive
```

## Customer

``` text
Customer
--------
Id
CompanyId
AccountId
Name
TaxNumber
Phone
Email
Address
CreditLimit
```

## Supplier

``` text
Supplier
--------
Id
CompanyId
AccountId
Name
TaxNumber
Phone
Email
Address
```

## Journal

``` text
Journal
-------
Id
CompanyId
FinancialYearId
VoucherType
VoucherNumber
Date
Reference
Description
Status
```

## JournalLine

``` text
JournalLine
-----------
Id
JournalId
AccountId
Debit
Credit
Description
PartyType
PartyId
```

------------------------------------------------------------------------

# 30. Sales Database

## SalesInvoice

``` text
SalesInvoice
------------
Id
CompanyId
CustomerId
InvoiceNumber
InvoiceDate
DueDate
Subtotal
Discount
Tax
Total
PaidAmount
Balance
Status
```

## SalesInvoiceLine

``` text
SalesInvoiceLine
----------------
Id
InvoiceId
ProductId
Quantity
Rate
Discount
TaxRate
TaxAmount
Amount
```

------------------------------------------------------------------------

# 31. Purchase Database

## PurchaseInvoice

``` text
PurchaseInvoice
---------------
Id
CompanyId
SupplierId
InvoiceNumber
InvoiceDate
DueDate
Subtotal
Discount
Tax
Total
PaidAmount
Balance
Status
```

## PurchaseInvoiceLine

``` text
PurchaseInvoiceLine
-------------------
Id
PurchaseInvoiceId
ProductId
Quantity
Rate
Discount
TaxRate
TaxAmount
Amount
```

------------------------------------------------------------------------

# 32. Payment Database

## CustomerPayment

``` text
CustomerPayment
---------------
Id
CompanyId
CustomerId
PaymentDate
Amount
AccountId
ReferenceNumber
PaymentMethod
Status
```

## CustomerPaymentAllocation

``` text
CustomerPaymentAllocation
-------------------------
Id
PaymentId
InvoiceId
AllocatedAmount
```

Equivalent supplier structures should exist:

``` text
SupplierPayment
SupplierPaymentAllocation
```

------------------------------------------------------------------------

# 33. Inventory Database

Recommended tables:

``` text
Product
Warehouse
Stock
StockMovement
StockAdjustment
StockTransfer
```

Example:

``` text
Stock
-----
Id
CompanyId
ProductId
WarehouseId
Quantity
AverageCost
```

StockMovement:

``` text
StockMovement
-------------
Id
ProductId
WarehouseId
MovementType
Quantity
UnitCost
ReferenceType
ReferenceId
Date
```

Movement types:

``` text
OPENING
PURCHASE
SALE
SALES_RETURN
PURCHASE_RETURN
TRANSFER_OUT
TRANSFER_IN
ADJUSTMENT_IN
ADJUSTMENT_OUT
DAMAGE
```

------------------------------------------------------------------------

# 34. Fixed Asset Database

``` text
FixedAsset
----------
Id
CompanyId
AssetAccountId
Name
AssetCode
PurchaseDate
PurchaseValue
UsefulLife
DepreciationMethod
AccumulatedDepreciation
CurrentBookValue
Status
```

Statuses:

``` text
ACTIVE
DISPOSED
WRITTEN_OFF
```

------------------------------------------------------------------------

# 35. Loan Database

``` text
Loan
----
Id
CompanyId
Lender
LoanAccountId
PrincipalAmount
InterestRate
StartDate
EndDate
InstallmentAmount
OutstandingPrincipal
Status
```

LoanSchedule:

``` text
LoanSchedule
------------
Id
LoanId
DueDate
Principal
Interest
Installment
PaidAmount
Status
```

------------------------------------------------------------------------

# 36. Accounting Engine

The most important architectural decision:

> **Do not implement independent accounting logic inside every screen.**

Create one central accounting engine.

Example interface:

``` csharp
public interface IAccountingService
{
    Task PostJournalAsync(JournalEntry entry);

    Task PostSalesInvoiceAsync(...);

    Task PostPurchaseInvoiceAsync(...);

    Task PostReceiptAsync(...);

    Task PostPaymentAsync(...);

    Task PostCreditNoteAsync(...);

    Task PostDebitNoteAsync(...);

    Task PostContraAsync(...);

    Task ReverseJournalAsync(...);
}
```

All specialized operations should eventually call:

``` text
PostJournal()
```

------------------------------------------------------------------------

# 37. Accounting Engine Flow

``` text
                    ┌───────────────────┐
                    │ Accounting Engine │
                    └─────────┬─────────┘
                              │
                         ┌────▼────┐
                         │ Journal │
                         └────┬────┘
                              │
                       ┌──────▼──────┐
                       │ JournalLine │
                       └──────┬──────┘
                              │
             ┌────────────────┼────────────────┐
             ↓                ↓                ↓
         Customer            Bank            Revenue
          Ledger             Ledger            Ledger
             ↓                ↓                ↓
        Receivable           Cash              P&L
```

Business modules:

``` text
Sales Invoice ────────┐
Purchase Bill ────────┤
Receipt ──────────────┤
Payment ──────────────┤
Credit Note ──────────┤
Debit Note ───────────┤
Expense ──────────────┤
Loan ─────────────────┤
Asset ────────────────┤
Contra ───────────────┤
Manual Journal ───────┘
          │
          ↓
   Accounting Engine
          │
          ↓
       Journal
```

------------------------------------------------------------------------

# 38. Important Posting Rules

Before posting any journal:

## Rule 1 --- Debit/Credit Balance

``` text
Total Debit == Total Credit
```

## Rule 2 --- Positive Amounts

``` text
Debit >= 0
Credit >= 0
```

## Rule 3 --- One Side Per Line

A normal journal line should not contain:

``` text
Debit > 0
AND
Credit > 0
```

## Rule 4 --- Minimum Lines

A journal should contain at least two lines.

## Rule 5 --- Valid Account

Account must exist and belong to the company.

## Rule 6 --- Active Account

Inactive accounts cannot receive new postings.

## Rule 7 --- Open Period

Transaction date must belong to an open financial period.

## Rule 8 --- Valid Party

Customer/supplier references must be valid where required.

## Rule 9 --- Tax Validation

Tax calculations and tax accounts must be consistent with the
transaction.

## Rule 10 --- Duplicate Protection

The system should prevent accidental duplicate posting of the same
business transaction.

------------------------------------------------------------------------

# 39. Idempotency and Duplicate Prevention

This is especially important for APIs and ERP integrations.

For example:

``` text
ExternalReference
SourceSystem
SourceTransactionId
```

should be stored where appropriate.

Before posting:

``` text
Does this external transaction already exist?
        ↓
YES → Do not post again
NO  → Create and post
```

This prevents duplicate invoices/payments when an API retries.

------------------------------------------------------------------------

# 40. General Ledger

Example:

``` text
Account: HDFC Bank

Date       Voucher       Description       Debit    Credit    Balance

01 Sep     OPEN          Opening           2,00,000            2,00,000
05 Sep     RV-001        Customer Payment    50,000             2,50,000
07 Sep     PM-001        Supplier Payment             40,000    2,10,000
10 Sep     PM-002        Electricity                    5,000    2,05,000
```

------------------------------------------------------------------------

# 41. Customer Ledger

``` text
Customer: Rahul Traders

Date       Voucher       Debit      Credit     Balance

01 Sep     Opening       20,000                20,000
05 Sep     INV-001       10,000                30,000
10 Sep     REC-001                  15,000     15,000
15 Sep     CN-001                    2,000     13,000
```

------------------------------------------------------------------------

# 42. Supplier Ledger

``` text
Supplier: ABC Suppliers

Date       Voucher       Debit      Credit     Balance

01 Sep     Opening                  30,000     30,000
05 Sep     BILL-001                 20,000     50,000
10 Sep     PAY-001       25,000                25,000
```

------------------------------------------------------------------------

# 43. Day Book

The Day Book should show all accounting transactions:

``` text
Date
Voucher Number
Voucher Type
Description
Debit
Credit
User
Status
```

Useful filters:

``` text
Date Range
Voucher Type
Account
Customer
Supplier
User
Status
```

------------------------------------------------------------------------

# 44. Cash Book

Should show:

``` text
Opening Cash
Cash Receipts
Cash Payments
Closing Cash
```

Example:

``` text
Opening Cash       ₹50,000
Receipts           ₹30,000
Payments           ₹10,000
---------------------------
Closing Cash       ₹70,000
```

------------------------------------------------------------------------

# 45. Bank Book

Should show:

``` text
Opening Bank
Receipts
Payments
Transfers
Bank Charges
Interest
Closing Bank
```

------------------------------------------------------------------------

# 46. Trial Balance

Example:

``` text
Account                    Debit       Credit

Cash                       50,000
Bank                      200,000
Accounts Receivable        80,000
Inventory                 100,000
Supplier Payable                       60,000
Capital                                270,000
Sales                                  150,000
Salary Expense             30,000
Rent Expense               20,000
```

The final totals must match:

``` text
Total Debit = Total Credit
```

------------------------------------------------------------------------

# 47. Profit & Loss

Example:

``` text
REVENUE
--------------------------------
Sales                         ₹10,00,000
Service Income                  ₹20,000
                              ----------
Total Revenue                ₹10,20,000

EXPENSES
--------------------------------
COGS                           ₹5,00,000
Salary                         ₹1,50,000
Rent                             ₹60,000
Electricity                      ₹20,000
Other Expenses                   ₹30,000
                              ----------
Total Expenses                ₹7,60,000

NET PROFIT                    ₹2,60,000
```

------------------------------------------------------------------------

# 48. Balance Sheet

## Assets

``` text
Cash
Bank
Accounts Receivable
Inventory
Fixed Assets
Prepaid Expenses
Other Current Assets
```

## Liabilities

``` text
Accounts Payable
Tax Payable
Salary Payable
Outstanding Expenses
Loans
Other Liabilities
```

## Equity

``` text
Capital
Retained Earnings
Current Year Profit/Loss
Less: Drawings
```

Fundamental equation:

> **Assets = Liabilities + Equity**

------------------------------------------------------------------------

# 49. Accounts Receivable Aging

The system should calculate:

``` text
Current
1-30 Days
31-60 Days
61-90 Days
91-180 Days
180+ Days
```

Example:

``` text
Customer        Current   1-30   31-60   61-90   90+
Rahul Traders   20,000    5,000  10,000  2,000   8,000
```

------------------------------------------------------------------------

# 50. Accounts Payable Aging

Same structure for suppliers:

``` text
Current
1-30 Days
31-60 Days
61-90 Days
91-180 Days
180+ Days
```

This helps with payment planning.

------------------------------------------------------------------------

# 51. Credit Limits

Customer master can contain:

``` text
CreditLimit
PaymentTerms
CreditDays
```

Before posting a credit sale:

``` text
Current Outstanding
+
New Invoice
-
Eligible Credits
```

Compare against the customer's credit limit.

The system may:

``` text
ALLOW
WARN
BLOCK
```

according to company policy.

------------------------------------------------------------------------

# 52. Payment Terms

Examples:

``` text
Immediate
7 Days
15 Days
30 Days
45 Days
60 Days
```

Invoice:

``` text
Invoice Date = 15 Sep
Payment Terms = 30 Days
Due Date = 15 Oct
```

The system should calculate overdue days automatically.

------------------------------------------------------------------------

# 53. Multi-Currency

For businesses dealing with multiple currencies, transaction data should
support:

``` text
Currency
ExchangeRate
ForeignAmount
BaseAmount
```

Example:

``` text
Invoice Currency = USD
Foreign Amount = $1,000
Exchange Rate = ₹83
Base Amount = ₹83,000
```

The system may need exchange-gain/loss entries when settlement occurs at
a different rate.

------------------------------------------------------------------------

# 54. Cost Centers

For larger businesses, journal lines can contain:

``` text
CostCenterId
DepartmentId
ProjectId
BranchId
```

Example:

``` text
Salary Expense
    Department = IT
    Branch = Ahmedabad
    Project = ERP
```

This allows:

``` text
Department P&L
Project P&L
Branch P&L
```

------------------------------------------------------------------------

# 55. Branch Accounting

If the company has multiple branches:

``` text
Company
│
├── Ahmedabad
├── Mumbai
├── Delhi
└── Surat
```

Transactions should include:

``` text
BranchId
```

If branches are treated as separate accounting units, inter-branch
balances should also be tracked.

------------------------------------------------------------------------

# 56. Inter-Company Transactions

If multiple companies exist:

``` text
Company A
Company B
```

A transaction between them should create corresponding entries in both
entities, with appropriate inter-company accounts.

Example:

``` text
Company A:
Due from Company B

Company B:
Due to Company A
```

------------------------------------------------------------------------

# 57. Bank Reconciliation

Bank reconciliation compares:

``` text
ERP Bank Ledger
        VS
Actual Bank Statement
```

The system should support:

``` text
Matched
Unmatched
Bank Charges
Interest
Outstanding Cheques
Deposits in Transit
Direct Bank Credits
Direct Bank Debits
```

Example:

``` text
ERP Balance       ₹5,00,000
Bank Statement    ₹4,98,000
Difference          ₹2,000
```

The reconciliation screen should help identify why.

------------------------------------------------------------------------

# 58. Year-End Closing

At financial year end:

``` text
1. Complete all transactions
2. Reconcile cash
3. Reconcile bank
4. Reconcile customers
5. Reconcile suppliers
6. Verify inventory
7. Calculate depreciation
8. Calculate tax
9. Review outstanding expenses
10. Review prepaid expenses
11. Generate Trial Balance
12. Generate P&L
13. Generate Balance Sheet
14. Close financial year
```

Revenue and expense accounts are closed/transferred according to the
accounting method used.

Balance-sheet accounts carry forward as opening balances.

------------------------------------------------------------------------

# 59. System Security

Accounting systems need strong authorization.

Recommended roles:

``` text
SUPER_ADMIN
ADMIN
ACCOUNTANT
SALES_USER
PURCHASE_USER
CASHIER
AUDITOR
VIEWER
```

Permissions should cover:

``` text
Create
Edit
View
Post
Cancel
Reverse
Delete
Approve
Export
Close Period
Reopen Period
```

Posting and reversal permissions should be more restricted than data
entry.

------------------------------------------------------------------------

# 60. Approval Workflow

For sensitive transactions:

``` text
Draft
  ↓
Submitted
  ↓
Approved
  ↓
Posted
```

Example:

``` text
Expense ₹5,000
        ↓
Created by Employee
        ↓
Approved by Manager
        ↓
Posted by Accountant
```

Approval limits can be configured:

``` text
0 - ₹10,000       Manager
₹10,001 - ₹1L     Finance Manager
₹1L+              Director
```

------------------------------------------------------------------------

# 61. Complete Business Transaction Flow

## Sales

``` text
Quotation
   ↓
Sales Order
   ↓
Delivery / Stock Issue
   ↓
Sales Invoice
   ↓
Accounting Journal
   ↓
Receivable
   ↓
Receipt
   ↓
Payment Allocation
   ↓
Invoice Paid
```

## Purchase

``` text
Purchase Request
   ↓
Purchase Order
   ↓
Goods Receipt
   ↓
Purchase Bill
   ↓
Accounting Journal
   ↓
Payable
   ↓
Payment
   ↓
Payment Allocation
   ↓
Bill Paid
```

------------------------------------------------------------------------

# 62. Accounting Architecture

Recommended high-level architecture:

``` text
┌────────────────────────────────────────────┐
│              ERP Application               │
├────────────────────────────────────────────┤
│ Sales │ Purchase │ Inventory │ Expense     │
├────────────────────────────────────────────┤
│ Customer │ Supplier │ Cash │ Bank │ Assets │
├────────────────────────────────────────────┤
│            Accounting Engine               │
├────────────────────────────────────────────┤
│       Journal + Journal Lines              │
├────────────────────────────────────────────┤
│            Ledger / Reporting              │
└────────────────────────────────────────────┘
```

The accounting engine should be the single source of truth for financial
postings.

------------------------------------------------------------------------

# 63. Recommended Accounting Service Flow

``` text
Create Business Transaction
        ↓
Validate Transaction
        ↓
Calculate Subtotal
        ↓
Calculate Discount
        ↓
Calculate Tax
        ↓
Calculate Total
        ↓
Generate Journal
        ↓
Validate Debit = Credit
        ↓
Validate Financial Period
        ↓
Validate Accounts
        ↓
Post Journal
        ↓
Update Transaction Status
        ↓
Create Audit Log
```

------------------------------------------------------------------------

# 64. Example: Complete Sales Invoice

Suppose:

``` text
Customer: Rahul Traders

Product value = ₹10,000
Tax = ₹1,800
Invoice total = ₹11,800
```

Invoice:

``` text
SalesInvoice
------------
InvoiceNo = INV-1001
Customer = Rahul Traders
Subtotal = 10,000
Tax = 1,800
Total = 11,800
Status = POSTED
```

Journal:

``` text
Customer A/C       Dr   11,800
    To Sales A/C        10,000
    To Output Tax A/C    1,800
```

Later payment:

``` text
Bank A/C            Dr   11,800
    To Customer A/C      11,800
```

Invoice status:

``` text
UNPAID
   ↓
PARTIALLY_PAID
   ↓
PAID
```

------------------------------------------------------------------------

# 65. Example: Complete Purchase

``` text
Supplier = ABC Suppliers
Purchase = ₹50,000
Tax = ₹9,000
Total = ₹59,000
```

Journal:

``` text
Inventory/Purchase A/C  Dr   50,000
Input Tax A/C           Dr    9,000
    To Supplier A/C          59,000
```

Payment:

``` text
Supplier A/C             Dr   59,000
    To Bank A/C                59,000
```

------------------------------------------------------------------------

# 66. Important Transaction Statuses

Sales invoice:

``` text
DRAFT
POSTED
PARTIALLY_PAID
PAID
OVERDUE
CANCELLED
VOID
```

Purchase bill:

``` text
DRAFT
POSTED
PARTIALLY_PAID
PAID
OVERDUE
CANCELLED
VOID
```

Payment:

``` text
DRAFT
POSTED
ALLOCATED
PARTIALLY_ALLOCATED
UNALLOCATED
CANCELLED
```

------------------------------------------------------------------------

# 67. Important Edge Cases

A production system should consider:

``` text
Duplicate invoice number
Duplicate payment
Invoice cancellation
Payment cancellation
Payment reversal
Overpayment
Underpayment
Partial payment
Advance payment
Advance adjustment
Credit note
Debit note
Sales return
Purchase return
Tax correction
Tax reversal
Discount
Bad debt
Write-off
Cheque bounce
Bank charges
Bank reconciliation difference
Negative stock
Stock adjustment
Damaged stock
Opening balance
Year-end closing
Backdated transaction
Closed-period transaction
Multi-currency
Exchange gain/loss
Branch transaction
Inter-company transaction
Round-off
Rounding difference
```

------------------------------------------------------------------------

# 68. Round-Off

For example:

``` text
Calculated Total = ₹1,000.49
Invoice Total     = ₹1,000
```

Round-off:

``` text
Round Off A/C       Dr/Credit   ₹0.49
```

The exact debit/credit direction depends on whether rounding increases
or decreases the invoice.

Round-off should be configurable.

------------------------------------------------------------------------

# 69. Negative Stock

If selling more stock than available:

``` text
Available = 5
Sale = 10
```

The system should have a configurable policy:

``` text
BLOCK
WARN
ALLOW
```

If negative stock is allowed, costing must still be handled carefully.

------------------------------------------------------------------------

# 70. Data Integrity Rules

The database should enforce as much as possible:

``` text
Foreign Keys
Unique Invoice Numbers
Unique Voucher Numbers
Not Null Required Fields
Decimal Precision
Company Isolation
Financial Year Isolation
```

Every accounting query should be filtered by the correct:

``` text
CompanyId
FinancialYearId
```

------------------------------------------------------------------------

# 71. Multi-Tenant / Company Isolation

If one ERP supports multiple companies:

``` text
Company A
Company B
Company C
```

Every financial master/transaction should be associated with the correct
company.

Never allow:

``` text
Company A Customer
        ↓
Company B Invoice
```

unless explicitly supported by an inter-company design.

------------------------------------------------------------------------

# 72. Recommended Reports

Minimum report set:

``` text
Day Book
Cash Book
Bank Book
General Ledger
Customer Ledger
Supplier Ledger
Trial Balance
Profit & Loss
Balance Sheet
Receivable Outstanding
Payable Outstanding
Receivable Aging
Payable Aging
Sales Register
Purchase Register
Credit Note Register
Debit Note Register
Tax Register
Inventory Register
Stock Valuation
Stock Movement
Fixed Asset Register
Depreciation Report
Loan Statement
Bank Reconciliation
Audit Log
```

------------------------------------------------------------------------

# 73. Dashboard KPIs

A useful accounting dashboard can show:

``` text
Today's Sales
Today's Purchases
Cash Balance
Bank Balance
Receivables
Payables
Overdue Receivables
Overdue Payables
Monthly Revenue
Monthly Expenses
Gross Profit
Net Profit
Inventory Value
Tax Payable
Loan Outstanding
```

------------------------------------------------------------------------

# 74. Recommended ERP Navigation

``` text
Dashboard

Accounting
├── Chart of Accounts
├── Journal
├── Ledger
├── Trial Balance
├── Profit & Loss
└── Balance Sheet

Sales
├── Customers
├── Quotations
├── Orders
├── Invoices
├── Receipts
├── Credit Notes
└── Sales Returns

Purchase
├── Suppliers
├── Purchase Orders
├── Bills
├── Payments
├── Debit Notes
└── Purchase Returns

Inventory
├── Products
├── Warehouses
├── Stock
├── Transfers
└── Adjustments

Banking
├── Bank Accounts
├── Transactions
└── Reconciliation

Expenses
├── Expenses
├── Categories
└── Reimbursements

Assets
├── Fixed Assets
├── Depreciation
└── Disposal

Loans
├── Loans
├── Schedules
└── Payments

Tax
├── Tax Rates
├── Input Tax
├── Output Tax
└── Tax Reports

Reports
├── Day Book
├── Ledger
├── Trial Balance
├── P&L
├── Balance Sheet
├── AR
├── AP
├── Inventory
└── Tax
```

------------------------------------------------------------------------

# 75. Final Architecture Principle

The most important rule for the implementation is:

``` text
                 BUSINESS TRANSACTION
                         │
                         ▼
                 VALIDATION SERVICE
                         │
                         ▼
                  ACCOUNTING ENGINE
                         │
                         ▼
                    JOURNAL
                         │
                         ▼
                  JOURNAL LINES
                         │
              ┌──────────┼──────────┐
              ▼          ▼          ▼
            LEDGER     TAX       PARTY
              │          │          │
              └──────────┼──────────┘
                         ▼
                     REPORTS
```

Do not maintain separate manually calculated balances such as:

``` text
Customer.Balance
Supplier.Balance
Bank.Balance
Cash.Balance
```

as the primary source of accounting truth.

Instead, balances should be derived from posted ledger transactions,
with carefully designed cached/materialized balances only when
performance requires them.

------------------------------------------------------------------------

# 76. Complete Scenario Checklist

## Sales

-   [x] Cash sale
-   [x] Credit sale
-   [x] Taxable sale
-   [x] Full customer payment
-   [x] Partial customer payment
-   [x] Customer advance
-   [x] Advance adjustment
-   [x] Sales return
-   [x] Credit note
-   [x] Discount allowed
-   [x] Bad debt
-   [x] Customer write-off
-   [x] Customer refund
-   [x] Multiple invoices against one payment
-   [x] Multiple payments against one invoice
-   [x] Overpayment
-   [x] Outstanding invoice
-   [x] Overdue invoice

## Purchase

-   [x] Cash purchase
-   [x] Credit purchase
-   [x] Taxable purchase
-   [x] Full supplier payment
-   [x] Partial supplier payment
-   [x] Supplier advance
-   [x] Advance adjustment
-   [x] Purchase return
-   [x] Debit note
-   [x] Discount received
-   [x] Supplier refund
-   [x] Multiple bills against one payment
-   [x] Multiple payments against one bill
-   [x] Outstanding bill
-   [x] Overdue bill

## Cash / Bank

-   [x] Cash receipt
-   [x] Cash payment
-   [x] Cash deposit to bank
-   [x] Bank withdrawal
-   [x] Bank-to-bank transfer
-   [x] Bank charges
-   [x] Bank interest
-   [x] Cheque received
-   [x] Cheque deposited
-   [x] Cheque cleared
-   [x] Cheque bounced
-   [x] Cheque issued
-   [x] Bank reconciliation

## Expenses

-   [x] Paid expense
-   [x] Outstanding expense
-   [x] Prepaid expense
-   [x] Accrued expense
-   [x] Expense reversal
-   [x] Employee reimbursement

## Assets

-   [x] Asset purchase
-   [x] Asset purchase on credit
-   [x] Depreciation
-   [x] Asset disposal
-   [x] Asset write-off

## Loans

-   [x] Loan received
-   [x] Principal repayment
-   [x] Interest payment
-   [x] EMI
-   [x] Loan schedule
-   [x] Loan closure

## Inventory

-   [x] Opening stock
-   [x] Purchase
-   [x] Sale
-   [x] Sales return
-   [x] Purchase return
-   [x] Stock transfer
-   [x] Stock adjustment
-   [x] Stock shortage
-   [x] Stock excess
-   [x] Damaged stock
-   [x] COGS
-   [x] Stock valuation

## Accounting

-   [x] Chart of accounts
-   [x] Journal
-   [x] Journal lines
-   [x] Ledger
-   [x] Opening balances
-   [x] Manual journal
-   [x] Reversal
-   [x] Period locking
-   [x] Year-end closing
-   [x] Audit trail
-   [x] Approval workflow

## Reporting

-   [x] Day Book
-   [x] Cash Book
-   [x] Bank Book
-   [x] General Ledger
-   [x] Customer Ledger
-   [x] Supplier Ledger
-   [x] Trial Balance
-   [x] Profit & Loss
-   [x] Balance Sheet
-   [x] AR Aging
-   [x] AP Aging
-   [x] Sales Register
-   [x] Purchase Register
-   [x] Tax Reports
-   [x] Inventory Reports
-   [x] Fixed Asset Register
-   [x] Loan Report
-   [x] Bank Reconciliation
-   [x] Audit Report

------------------------------------------------------------------------

# 77. Implementation Priority

If implementing this system from scratch, build in this order:

``` text
Phase 1
Company
Financial Year
Chart of Accounts

        ↓

Phase 2
Journal
JournalLine
Accounting Engine
Ledger

        ↓

Phase 3
Customers
Suppliers
Sales Invoice
Purchase Bill

        ↓

Phase 4
Receipt
Payment
Payment Allocation
AR/AP Outstanding

        ↓

Phase 5
Credit Note
Debit Note
Sales Return
Purchase Return
Discount

        ↓

Phase 6
Cash
Bank
Contra
Bank Reconciliation

        ↓

Phase 7
Inventory
Stock Movement
COGS
Warehouse

        ↓

Phase 8
Tax
Tax Transactions
Tax Reports

        ↓

Phase 9
Fixed Assets
Depreciation
Asset Disposal

        ↓

Phase 10
Loans
Payroll
Cost Centers
Branches
Multi-Currency

        ↓

Phase 11
Trial Balance
P&L
Balance Sheet
Aging Reports

        ↓

Phase 12
Approval
Audit
Period Lock
Year-End Closing
```

------------------------------------------------------------------------

# 78. Most Important Conclusion

For a reliable accounting ERP:

1.  **Everything financial eventually becomes a Journal.**
2.  **Every Journal contains Journal Lines.**
3.  **Every posted Journal must balance.**
4.  **Ledger balances come from posted journal lines.**
5.  **Invoices and bills create receivables/payables.**
6.  **Payments settle receivables/payables through allocation.**
7.  **Inventory transactions affect stock and, where applicable, COGS.**
8.  **Tax is represented through dedicated accounts and transaction
    details.**
9.  **Posted transactions should be reversed, not silently edited.**
10. **Every important financial action needs an audit trail.**
11. **Financial periods must be controlled.**
12. **Reports should derive from the accounting ledger.**
13. **The accounting engine should be shared by every business module.**

This design provides a strong foundation for a real-world double-entry
accounting system rather than a simple debit/credit entry screen.
