import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

// Test Case: TC_Withdraw_008 - Amount Equals Balance
// Priority: P1 - High
// Type: Boundary Test, Edge Case
// Description: Verify withdraw with amount equal to exact balance succeeds and account remains functional

WebUI.comment('TC_Withdraw_008: Amount Equals Balance - START')

// ========================================
// PREREQUISITE: Browser is already open and user is logged in
// ========================================

WebUI.comment('TC_Withdraw_008: Amount Equals Balance - START')
WebUI.comment('Assuming browser is open and user is logged in from setup')

// ========================================
// STEP 1: Navigate to home and capture current balance
// ========================================
WebUI.comment('Step 4: Capture current balance')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

WebUI.waitForElementPresent(findTestObject('Home/text_wallet_balance'), 15, FailureHandling.STOP_ON_FAILURE)
WebUI.delay(1)

String balanceText = WebUI.getText(findTestObject('Home/text_wallet_balance'))
WebUI.comment("Current balance displayed: ${balanceText}")

// Parse balance amount
String balanceNumericStr = balanceText.replaceAll('[^0-9]', '')
long currentBalance = 0

if (balanceNumericStr && balanceNumericStr.length() > 0) {
    currentBalance = Long.parseLong(balanceNumericStr)
}

WebUI.comment("Current balance (numeric): ${currentBalance}")

// Verify balance is sufficient for testing (at least minimum withdraw)
assert currentBalance >= 50000, "Balance too low for testing. Need at least 50,000, got: ${currentBalance}"

WebUI.comment("✅ Will withdraw EXACT balance: Rp ${currentBalance}")

// ========================================
// STEP 5: Navigate to withdraw page
// ========================================
WebUI.comment('Step 5: Navigate to withdraw page')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/withdraw')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

String withdrawUrl = WebUI.getUrl()
assert withdrawUrl.contains('/withdraw'), "Should be on withdraw page. Current URL: ${withdrawUrl}"

WebUI.comment('Step 5: Navigate to withdraw - COMPLETED')

// ========================================
// STEP 6: Enter amount equal to exact balance (Step 1)
// ========================================
WebUI.comment('Step 6: Enter amount equal to exact current balance')

WebUI.waitForElementPresent(findTestObject('Withdraw/input_withdraw_amount'), 15, FailureHandling.STOP_ON_FAILURE)

WebUI.click(findTestObject('Withdraw/input_withdraw_amount'))
WebUI.delay(0.3)
WebUI.clearText(findTestObject('Withdraw/input_withdraw_amount'))
WebUI.setText(findTestObject('Withdraw/input_withdraw_amount'), currentBalance.toString())
WebUI.delay(0.5)

WebUI.comment("✅ Entered exact balance amount: Rp ${currentBalance}")

// ========================================
// STEP 7: Proceed to PIN step (Step 2)
// ========================================
WebUI.comment('Step 7: Click Next to proceed to PIN step')

WebUI.click(findTestObject('Withdraw/btn_withdraw_continue'))
WebUI.delay(2)

// Verify we progressed to Step 2
WebUI.waitForPageLoad(10)

boolean isPinPadPresent = WebUI.verifyElementPresent(
    findTestObject('Common/btn_pin_digit_1'),
    10,
    FailureHandling.OPTIONAL
)

assert isPinPadPresent, 'Should progress to PIN step for valid amount equal to balance'

WebUI.comment('✅ Progressed to Step 2 (PIN entry)')

// ========================================
// STEP 8: Enter PIN using PIN pad and submit withdraw
// ========================================
WebUI.comment('Step 8: Enter PIN using PIN pad and submit withdraw')

// Enter PIN using PIN pad
String[] pinDigits = GlobalVariable.TEST_USER_PIN.split('')
for (String digit : pinDigits) {
    WebUI.click(findTestObject('Common/btn_pin_digit_' + digit))
    WebUI.delay(0.2)
}

WebUI.comment('✅ Entered PIN via PIN pad')
WebUI.delay(0.5)

// Submit withdraw
WebUI.click(findTestObject('Withdraw/btn_withdraw_submit'))
WebUI.delay(3) // Wait for transaction processing

WebUI.comment('✅ Submitted withdraw request')

// ========================================
// STEP 9: Verify redirect after successful withdraw
// ========================================
WebUI.comment('Step 9: Verify successful withdraw redirect')

WebUI.waitForPageLoad(10)
WebUI.delay(2)

String finalUrl = WebUI.getUrl()
WebUI.comment("Redirected to: ${finalUrl}")

// Should redirect to status page or home
boolean redirectedCorrectly = finalUrl.contains('/status') || finalUrl.contains('/home')
assert redirectedCorrectly, "Should redirect to status or home page after withdraw. Current URL: ${finalUrl}"

WebUI.comment('✅ Redirected successfully after withdraw')

// ========================================
// STEP 10: Verify balance is now zero
// ========================================
WebUI.comment('Step 10: Verify balance is now zero')

// Navigate to home to check balance
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(2)

// Wait for balance to update
WebUI.waitForElementPresent(findTestObject('Home/text_wallet_balance'), 15, FailureHandling.STOP_ON_FAILURE)

String newBalanceText = WebUI.getText(findTestObject('Home/text_wallet_balance'))
WebUI.comment("New balance displayed: ${newBalanceText}")

// Parse new balance
String newBalanceNumericStr = newBalanceText.replaceAll('[^0-9]', '')
long newBalance = 0

if (newBalanceNumericStr && newBalanceNumericStr.length() > 0) {
    newBalance = Long.parseLong(newBalanceNumericStr)
}

WebUI.comment("New balance (numeric): ${newBalance}")

// Verify balance is zero
assert newBalance == 0, "Balance should be zero after withdrawing exact balance. Got: ${newBalance}"

WebUI.comment('✅ Balance is now zero (Rp 0)')

// ========================================
// STEP 11: Verify account remains functional (not locked)
// ========================================
WebUI.comment('Step 11: Verify account remains functional with zero balance')

// Verify UI elements are still displayed
boolean isUserNameVisible = WebUI.verifyElementPresent(
    findTestObject('Home/text_user_name'),
    10,
    FailureHandling.OPTIONAL
)
assert isUserNameVisible, 'User name should remain visible with zero balance'

boolean isAccountNumberVisible = WebUI.verifyElementPresent(
    findTestObject('Home/badge_home_account_number'),
    10,
    FailureHandling.OPTIONAL
)
assert isAccountNumberVisible, 'Account number should remain visible with zero balance'

WebUI.comment('✅ UI elements remain visible')

// Verify we can navigate to top-up (account not locked)
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/topup')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

String topupUrl = WebUI.getUrl()
assert topupUrl.contains('/topup'), "Should be able to access top-up with zero balance. Current URL: ${topupUrl}"

WebUI.comment('✅ Can access top-up feature - account is not locked')

// ========================================
// TEST SUMMARY
// ========================================
WebUI.comment('========================================')
WebUI.comment('TC_Withdraw_008: Amount Equals Balance - SUMMARY')
WebUI.comment("✅ Withdrew exact balance: Rp ${currentBalance}")
WebUI.comment('✅ Amount equal to balance was accepted')
WebUI.comment('✅ Withdraw completed successfully')
WebUI.comment('✅ Final balance is zero (Rp 0)')
WebUI.comment('✅ Account remains functional (not locked)')
WebUI.comment('✅ UI elements remain visible')
WebUI.comment('✅ Can access other features (top-up)')
WebUI.comment('✅ PIN entry via PIN pad works correctly')
WebUI.comment('✅ Boundary test passed - withdrawing entire balance works correctly')
WebUI.comment('========================================')

// ========================================
// Return to home page for next test
// ========================================
WebUI.comment('Returning to home page for next test case')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

WebUI.comment('TC_Withdraw_008: Amount Equals Balance - COMPLETED')
WebUI.comment('✅ Browser remains open for next test case')