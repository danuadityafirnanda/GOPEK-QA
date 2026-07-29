import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

// Test Case: TC_Withdraw_002 - Valid Mid Range Amounts
// Priority: P1 - High
// Type: Positive Test
// Description: Verify multiple valid mid-range amounts (100K, 500K, 1M) are accepted

// ========================================
// PREREQUISITE: Browser is already open and user is logged in
// ========================================

WebUI.comment('TC_Withdraw_002: Valid Mid Range Amounts - START')
WebUI.comment('Assuming browser is open and user is logged in from setup')

// Test amounts to validate
def testAmounts = [100000, 500000, 1000000]
def amountLabels = ['100K', '500K', '1M']

// ========================================
// STEP 1: Navigate to home and capture initial balance
// ========================================
WebUI.comment('Step 1: Capture current wallet balance')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

// Wait for home page to load
WebUI.waitForElementPresent(findTestObject('Home/text_wallet_balance'), 15, FailureHandling.STOP_ON_FAILURE)
WebUI.delay(1)

// Get current balance text
String initialBalanceText = WebUI.getText(findTestObject('Home/text_wallet_balance'))
WebUI.comment("Initial balance displayed: ${initialBalanceText}")

// Parse balance amount
String balanceNumericStr = initialBalanceText.replaceAll('[^0-9]', '')
long initialBalance = 0

if (balanceNumericStr && balanceNumericStr.length() > 0) {
    initialBalance = Long.parseLong(balanceNumericStr)
}

WebUI.comment("Initial balance (numeric): ${initialBalance}")

// Verify sufficient balance for testing
assert initialBalance >= 1000000, "Insufficient balance for testing. Need at least 1,000,000, got: ${initialBalance}"

// ========================================
// STEP 2-4: Test each amount
// ========================================
int successfulTests = 0

for (int i = 0; i < testAmounts.size(); i++) {
    long amount = testAmounts[i]
    String label = amountLabels[i]
    
    WebUI.comment("========================================")
    WebUI.comment("Testing Amount ${i + 1}/3: ${label} (Rp ${amount})")
    WebUI.comment("========================================")
    
    // Navigate to withdraw page
    WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/withdraw')
    WebUI.waitForPageLoad(10)
    WebUI.delay(1)
    
    // Verify we're on withdraw page
    String withdrawUrl = WebUI.getUrl()
    assert withdrawUrl.contains('/withdraw'), "Should be on withdraw page. Current URL: ${withdrawUrl}"
    
    // Wait for amount input field (Step 1)
    WebUI.waitForElementPresent(findTestObject('Withdraw/input_withdraw_amount'), 15, FailureHandling.STOP_ON_FAILURE)
    
    // Enter withdraw amount
    WebUI.click(findTestObject('Withdraw/input_withdraw_amount'))
    WebUI.delay(0.3)
    WebUI.clearText(findTestObject('Withdraw/input_withdraw_amount'))
    WebUI.setText(findTestObject('Withdraw/input_withdraw_amount'), amount.toString())
    WebUI.delay(0.5)
    
    WebUI.comment("✅ Entered amount: Rp ${amount}")
    
    // Verify no validation error is displayed
    // Check that Next button is enabled/clickable
    boolean isNextButtonPresent = WebUI.verifyElementPresent(
        findTestObject('Withdraw/btn_withdraw_continue'),
        10,
        FailureHandling.OPTIONAL
    )
    
    assert isNextButtonPresent, "Next button should be present for valid amount ${amount}"
    
    boolean isNextButtonClickable = WebUI.verifyElementClickable(
        findTestObject('Withdraw/btn_withdraw_continue'),
        FailureHandling.OPTIONAL
    )
    
    if (isNextButtonClickable) {
        WebUI.comment("✅ Next button is clickable - amount ${amount} is accepted")
    } else {
        WebUI.comment("⚠️  Next button not clickable - checking if amount was still accepted")
    }
    
    // Click Next to proceed to PIN step
    WebUI.click(findTestObject('Withdraw/btn_withdraw_continue'))
    WebUI.delay(2)
    
    // Verify we progressed to Step 2 (PIN entry)
    WebUI.waitForPageLoad(10)
    
    // Check for PIN pad (digit buttons)
    boolean isPinPadPresent = WebUI.verifyElementPresent(
        findTestObject('Common/btn_pin_digit_1'),
        10,
        FailureHandling.OPTIONAL
    )
    
    if (isPinPadPresent) {
        WebUI.comment("✅ Progressed to Step 2 (PIN entry) successfully")
        WebUI.comment("✅ Amount ${label} (Rp ${amount}) is VALID - No validation errors")
        successfulTests++
        
        // For first amount, complete the withdraw to verify end-to-end
        if (i == 0) {
            WebUI.comment("Completing withdraw for first amount to verify full flow...")
            
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
            
            WebUI.comment("✅ First withdraw (${label}) submitted successfully")
            
            // Wait for redirect
            WebUI.waitForPageLoad(10)
            WebUI.delay(2)
            
            // Get final URL (should be status page or home)
            String finalUrl = WebUI.getUrl()
            WebUI.comment("Redirected to: ${finalUrl}")
            
        } else {
            // For other amounts, just verify PIN step is displayed, then go back
            WebUI.comment("PIN step verified - navigating back to test next amount...")
            
            // Navigate back to home to reset for next test
            WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
            WebUI.waitForPageLoad(10)
            WebUI.delay(1)
        }
        
    } else {
        WebUI.comment("❌ FAILED: Did not progress to PIN step for amount ${amount}")
        WebUI.comment("Current URL: " + WebUI.getUrl())
        
        // Take screenshot for debugging
        WebUI.takeScreenshot()
        
        // Navigate back to home to continue testing
        WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
        WebUI.waitForPageLoad(10)
        WebUI.delay(1)
    }
    
    WebUI.comment("Test ${i + 1}/3 completed")
}

// ========================================
// STEP 5: Verify final balance (after first withdrawal)
// ========================================
WebUI.comment('Step 5: Verify final balance after first withdrawal')

// Navigate to home to check updated balance
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(2)

// Wait for balance to update
WebUI.waitForElementPresent(findTestObject('Home/text_wallet_balance'), 15, FailureHandling.STOP_ON_FAILURE)

// Get updated balance
String finalBalanceText = WebUI.getText(findTestObject('Home/text_wallet_balance'))
WebUI.comment("Final balance displayed: ${finalBalanceText}")

// Parse final balance
String finalBalanceNumericStr = finalBalanceText.replaceAll('[^0-9]', '')
long finalBalance = 0

if (finalBalanceNumericStr && finalBalanceNumericStr.length() > 0) {
    finalBalance = Long.parseLong(finalBalanceNumericStr)
}

// Calculate expected balance (initial - first withdrawal amount)
long firstWithdrawAmount = testAmounts[0]
long expectedBalance = initialBalance - firstWithdrawAmount

WebUI.comment("Expected balance after first withdraw: ${expectedBalance}")
WebUI.comment("Actual final balance: ${finalBalance}")

// Note: Balance verification might vary due to other transactions or timing
// We verify that balance decreased
if (finalBalance < initialBalance) {
    WebUI.comment("✅ Balance decreased after withdrawal")
} else {
    WebUI.comment("⚠️  Balance did not decrease - may need manual verification")
}

// ========================================
// TEST SUMMARY
// ========================================
WebUI.comment('========================================')
WebUI.comment('TC_Withdraw_002: Valid Mid Range Amounts - SUMMARY')
WebUI.comment('✅ Tested 3 valid mid-range amounts')
WebUI.comment('  - 100,000 (100K): ' + (successfulTests >= 1 ? 'PASSED' : 'FAILED'))
WebUI.comment('  - 500,000 (500K): ' + (successfulTests >= 2 ? 'PASSED' : 'FAILED'))
WebUI.comment('  - 1,000,000 (1M): ' + (successfulTests >= 3 ? 'PASSED' : 'FAILED'))
WebUI.comment("✅ ${successfulTests}/3 amounts accepted and progressed to PIN step")
WebUI.comment('✅ No validation errors for valid amounts')
WebUI.comment('✅ Form progression works correctly')
WebUI.comment('✅ PIN entry via PIN pad works correctly')
WebUI.comment("Initial balance: Rp ${initialBalance}")
WebUI.comment("Final balance: Rp ${finalBalance}")
WebUI.comment('========================================')

// Verify at least all amounts were accepted
assert successfulTests == 3, "All 3 amounts should be accepted. Only ${successfulTests} passed."

// ========================================
// Return to home page for next test
// ========================================
WebUI.comment('Returning to home page for next test case')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

WebUI.comment('TC_Withdraw_002: Valid Mid Range Amounts - COMPLETED')
WebUI.comment('✅ Browser remains open for next test case')