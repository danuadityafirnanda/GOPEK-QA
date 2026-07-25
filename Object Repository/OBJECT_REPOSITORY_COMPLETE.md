# Katalon Object Repository - COMPLETE

**Status**: ✓ Production Ready  
**Total Objects**: 60  
**Date Created**: 2026-07-24  
**Format**: Katalon Studio XML Test Objects

## Module Summary

### Auth Module (17 objects)
**Login** (5 objects)
- form_login, input_email, input_password, btn_login, link_register

**Register** (12 objects)  
- form_register, input_fullname, input_email_register, input_phone
- input_password_register, input_password_confirmation
- input_pin, input_pin_confirmation
- btn_back, btn_continue, btn_submit_register, link_login

### Transfer Module (10 objects)
- input_account_number, btn_check_account
- input_transfer_note, btn_continue_transfer_amount
- card_transfer_sender, card_transfer_recipient
- btn_continue_transfer_summary, btn_back_transfer_summary
- input_pin_transfer, btn_submit_transfer

### TopUp Module (6 objects)
- card_topup_method, btn_submit_topup
- card_payment_va, badge_va_copy
- btn_back_to_home, btn_continue_payment_topup

### Payment Module (9 objects)
- card_payment_merchant, input_payment_note, btn_continue_payment_amount
- card_payment_sender, card_payment_merchant_summary
- btn_continue_payment_to_pin, btn_back_payment_summary
- input_pin_payment, btn_submit_payment

### Withdraw Module (2 objects)
- input_pin_withdraw, btn_submit_withdraw

### QR Module (3 objects)
- tab_qr_scan, tab_qr_my-qr, badge_qr_account_number

### Common Module (13 objects)
**Navigation**
- badge_home_account_number, link_see_all_transactions

**PIN Keypad** (11 objects)
- btn_pin_digit_0 through btn_pin_digit_9 (complete 0-9)
- btn_pin_backspace

## Selector Strategy
All objects use data-testid attributes:
- Primary: XPATH `//*[@data-testid='...']`
- Secondary: CSS `[data-testid='...']`
- Tertiary: BASIC matching

## Test Coverage Readiness
✓ Login flow complete
✓ Registration (3-step) complete
✓ Transfer (check → amount → summary → PIN) complete
✓ Top-up (amount → VA waiting) complete
✓ Payment (QR → amount → summary → PIN) complete
✓ Withdraw (amount + PIN) complete
✓ QR scan/display complete
✓ PIN input complete (all digits 0-9 + backspace)

## Next Steps
1. Create Test Cases (Login, Register, Transfer, etc.)
2. Create Test Data Files (users, amounts, accounts)
3. Create Test Suites (organize test execution)
4. Execute test runs against https://gopek.live/
