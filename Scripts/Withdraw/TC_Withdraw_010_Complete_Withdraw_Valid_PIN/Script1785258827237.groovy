import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

// Test Case: TC_Withdraw_010 - Complete Withdraw Valid PIN
// Priority: P0 - Critical
// Type: E2E Test, Positive Test
// Description: Complete withdraw flow with amount entry and PIN verification

WebUI.comment('TC_Withdraw_010: Complete Withdraw Valid PIN - START')

// ========================================
// PREREQUISITE: Browser is already open and user is logged in
// ========================================

WebUI.comment('Assuming browser is open and user is logged in from setup')

long withdrawAmount = 100000

// ========================================
// STEP 1: Navigate to home and capture initial balance
// ========================================
// FIXED: Added explicit navigate to home before assertion
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

String currentUrl = WebUI.getUrl()
assert currentUrl.contains('/home'), "Failed to redirect to home. Current URL: ${currentUrl}"

WebUI.waitForElementPresent(findTestObject('Home/text_wallet_balance'), 15, FailureHandling.STOP_ON_FAILURE)
WebUI.delay(1)

String initialBalanceText = WebUI.getText(findTestObject('Home/text_wallet_balance'))
WebUI.comment("Initial balance: ${initialBalanceText}")

String balanceNumericStr = initialBalanceText.replaceAll('[^0-9]', '')
long initialBalance = 0
if (balanceNumericStr && balanceNumericStr.length() > 0) {
    initialBalance = Long.parseLong(balanceNumericStr)
}

WebUI.comment("Initial balance (numeric): ${initialBalance}")
assert initialBalance >= withdrawAmount, "Insufficient balance. Need ${withdrawAmount}, have ${initialBalance}"

WebUI.comment('Step 3: Balance captured - COMPLETED')

// ========================================
// STEP 4: Navigate to withdraw page
// ========================================
WebUI.comment('Step 4: Navigate to withdraw page')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/withdraw')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

String withdrawUrl = WebUI.getUrl()
assert withdrawUrl.contains('/withdraw'), "Should be on withdraw page. Current URL: ${withdrawUrl}"

WebUI.comment('Step 4: Navigate to withdraw - COMPLETED')

// ========================================
// STEP 5: Enter withdraw amount (Step 1)
// ========================================
WebUI.comment("Step 5: Enter withdraw amount (Rp ${withdrawAmount})")

WebUI.waitForElementPresent(findTestObject('Withdraw/input_withdraw_amount'), 15, FailureHandling.STOP_ON_FAILURE)

WebUI.click(findTestObject('Withdraw/input_withdraw_amount'))
WebUI.delay(0.3)
WebUI.clearText(findTestObject('Withdraw/input_withdraw_amount'))
WebUI.setText(findTestObject('Withdraw/input_withdraw_amount'), withdrawAmount.toString())
WebUI.delay(0.5)

WebUI.comment("✅ Entered amount: Rp ${withdrawAmount}")

// ========================================
// STEP 6: Proceed to PIN step (Step 2)
// ========================================
WebUI.comment('Step 6: Click Continue to proceed to PIN step')

WebUI.click(findTestObject('Withdraw/btn_withdraw_continue'))
WebUI.delay(2)

WebUI.waitForPageLoad(10)

// Verify PIN step is displayed
boolean isPinPageDisplayed = WebUI.verifyElementPresent(
    findTestObject('Common/btn_pin_digit_1'),
    10,
    FailureHandling.OPTIONAL
)

assert isPinPageDisplayed, 'Should display PIN entry step'

WebUI.comment('✅ Progressed to Step 2 (PIN entry)')

// ========================================
// STEP 7: Enter PIN using PIN pad
// ========================================
WebUI.comment('Step 7: Enter PIN using PIN pad')

// Split PIN into individual digits and click each button
String[] pinDigits = GlobalVariable.TEST_USER_PIN.split('')

for (String digit : pinDigits) {
    WebUI.click(findTestObject('Common/btn_pin_digit_' + digit))
    WebUI.delay(0.2)
}

WebUI.comment('✅ Entered PIN via PIN pad')
WebUI.delay(0.5)

// ========================================
// STEP 8: Submit withdraw transaction
// ========================================
WebUI.comment('Step 8: Submit withdraw transaction')

WebUI.click(findTestObject('Withdraw/btn_withdraw_submit'))
WebUI.delay(3)

WebUI.comment('✅ Submitted withdraw request')

// ========================================
// STEP 9: Verify success redirect
// ========================================
WebUI.comment('Step 9: Verify successful withdraw')

WebUI.waitForPageLoad(10)
WebUI.delay(2)

String finalUrl = WebUI.getUrl()
WebUI.comment("Redirected to: ${finalUrl}")

boolean redirectedCorrectly = finalUrl.contains('/status') || finalUrl.contains('/home')
assert redirectedCorrectly, "Should redirect to status or home. Current URL: ${finalUrl}"

WebUI.comment('✅ Redirected successfully after withdraw')

// ========================================
// STEP 10: Verify balance decreased
// ========================================
WebUI.comment('Step 10: Verify balance decreased')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(2)

WebUI.waitForElementPresent(findTestObject('Home/text_wallet_balance'), 15, FailureHandling.STOP_ON_FAILURE)

String newBalanceText = WebUI.getText(findTestObject('Home/text_wallet_balance'))
WebUI.comment("New balance: ${newBalanceText}")

String newBalanceNumericStr = newBalanceText.replaceAll('[^0-9]', '')
long newBalance = 0
if (newBalanceNumericStr && newBalanceNumericStr.length() > 0) {
    newBalance = Long.parseLong(newBalanceNumericStr)
}

long expectedBalance = initialBalance - withdrawAmount
WebUI.comment("Expected balance: ${expectedBalance}")
WebUI.comment("Actual new balance: ${newBalance}")

// Verify balance decreased
assert newBalance < initialBalance, "Balance should decrease after withdraw"

long balanceDiff = initialBalance - newBalance
WebUI.comment("Balance decreased by: ${balanceDiff}")

// ========================================
// TEST SUMMARY
// ========================================
WebUI.comment('========================================')
WebUI.comment('TC_Withdraw_010: Complete Withdraw Valid PIN - SUMMARY')
WebUI.comment("✅ Withdrew amount: Rp ${withdrawAmount}")
WebUI.comment("✅ Initial balance: Rp ${initialBalance}")
WebUI.comment("✅ New balance: Rp ${newBalance}")
WebUI.comment("✅ Balance decreased by: Rp ${balanceDiff}")
WebUI.comment('✅ Step 1 (Amount entry) completed')
WebUI.comment('✅ Step 2 (PIN entry via PIN pad) completed')
WebUI.comment('✅ Transaction submitted successfully')
WebUI.comment('✅ Success redirect verified')
WebUI.comment('✅ Balance decrease verified')
WebUI.comment('✅ Complete E2E withdraw flow working correctly')
WebUI.comment('========================================')

// ========================================
// Return to home page for next test
// ========================================
WebUI.comment('Returning to home page for next test case')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

WebUI.comment('TC_Withdraw_010: Complete Withdraw Valid PIN - COMPLETED')
WebUI.comment('✅ Browser remains open for next test case')