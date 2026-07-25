# Object Repository Summary

## Total Objects Created: 54

### Auth Module (17 objects)

#### Login (5 objects)
- form_login - Login form container
- input_email - Email input field
- input_password - Password input field
- btn_login - Login submit button
- link_register - Link to registration page

#### Register (12 objects)
- form_register - Registration form container
- input_fullname - Full name input
- input_email_register - Email input
- input_phone - Phone number input
- input_password_register - Password input
- input_password_confirmation - Password confirmation input
- input_pin - PIN input (step 1)
- input_pin_confirmation - PIN confirmation input (step 2)
- btn_back - Back button (navigation)
- btn_continue - Continue button (step navigation)
- btn_submit_register - Final submit button
- link_login - Link back to login page

### Transfer Module (11 objects)
- input_account_number - Destination account number input
- btn_check_account - Check account button
- input_transfer_note - Transfer note/description input
- btn_continue_transfer_amount - Continue from amount page
- card_transfer_sender - Sender card display (summary)
- card_transfer_recipient - Recipient card display (summary)
- btn_continue_transfer_summary - Continue from review to PIN
- btn_back_transfer_summary - Back button on summary
- input_pin_transfer - PIN input on summary page
- btn_submit_transfer - Final transfer submit button

### TopUp Module (6 objects)
- card_topup_method - Payment method card (Virtual Account)
- btn_submit_topup - Submit top-up request button
- card_payment_va - VA card on payment waiting page
- badge_va_copy - VA number copy badge
- btn_back_to_home - Back to home button
- btn_continue_payment_topup - Continue after topup created

### Payment Module (10 objects)
- card_payment_merchant - Merchant card (amount page)
- input_payment_note - Payment note input
- btn_continue_payment_amount - Continue from amount page
- card_payment_sender - Sender card (summary)
- card_payment_merchant_summary - Merchant card (summary)
- btn_continue_payment_to_pin - Continue from review to PIN
- btn_back_payment_summary - Back button on summary
- input_pin_payment - PIN input on summary page
- btn_submit_payment - Final payment submit button

### Withdraw Module (2 objects)
- input_pin_withdraw - PIN input on withdraw form
- btn_submit_withdraw - Submit withdraw button

### QR Module (3 objects)
- tab_qr_scan - Scan QR tab button
- tab_qr_my-qr - My QR tab button
- badge_qr_account_number - Account number copy badge (My QR display)

### Common Module (5 objects)
- badge_home_account_number - Account number badge on home page
- link_see_all_transactions - See all transactions link
- btn_pin_backspace - PIN keypad backspace button
- btn_pin_digit_0 - PIN keypad digit 0
- btn_pin_digit_1 - PIN keypad digit 1
- btn_pin_digit_5 - PIN keypad digit 5

## Selector Strategy
All objects use:
1. Primary: XPATH with data-testid attribute
2. Secondary: CSS selector with data-testid
3. Tertiary: BASIC selector with data-testid

## File Format
- XML-based Katalon Test Object format
- Each object has unique GUID
- Selector collections for fallback strategies
- Descriptive names matching frontend conventions

## Next Steps
1. Create remaining PIN digit buttons (2, 3, 4, 6, 7, 8, 9) if needed
2. Create Test Cases using these objects
3. Create Test Data Files for parameterized testing
4. Create Test Suites to organize test execution
