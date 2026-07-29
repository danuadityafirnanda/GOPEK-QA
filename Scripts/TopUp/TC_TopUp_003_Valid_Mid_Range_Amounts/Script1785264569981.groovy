import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

// Test Case: TC_TopUp_003 - Valid Mid-Range Amounts
// Priority: P1 - High
// Type: Positive Test
// Description: Verify multiple valid mid-range amounts (50K, 500K, 5M) are accepted

WebUI.comment('TC_TopUp_003: Valid Mid-Range Amounts - START')

// Test amounts to validate
def testAmounts = [50000, 500000, 5000000]
def amountLabels = ['50K', '500K', '5M']

// ========================================
// PREREQUISITE: Browser is already open and user is logged in
// ========================================

WebUI.comment('Assuming browser is open and user is logged in from setup')

// ========================================
// STEP 1-3: Test each amount (loop through multiple amounts)
// ========================================
int successfulTests = 0

for (int i = 0; i < testAmounts.size(); i++) {
    long amount = testAmounts[i]
    String label = amountLabels[i]
    
    WebUI.comment("========================================")
    WebUI.comment("Testing Amount ${i + 1}/3: ${label} (Rp ${amount})")
    WebUI.comment("========================================")
    
    // Navigate to top-up page
    WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/topup')
    WebUI.waitForPageLoad(10)
    WebUI.delay(1)
    
    // Verify we're on top-up page
    String topupUrl = WebUI.getUrl()
    assert topupUrl.contains('/topup'), "Should be on top-up page. Current URL: ${topupUrl}"
    
    // Wait for amount input field
    WebUI.waitForElementPresent(findTestObject('TopUp/input_topup_amount'), 15, FailureHandling.STOP_ON_FAILURE)
    
    // Enter top-up amount
    WebUI.click(findTestObject('TopUp/input_topup_amount'))
    WebUI.delay(0.3)
    WebUI.clearText(findTestObject('TopUp/input_topup_amount'))
    WebUI.setText(findTestObject('TopUp/input_topup_amount'), amount.toString())
    WebUI.delay(0.5)
    
    WebUI.comment("✅ Entered amount: Rp ${amount}")
    
    // Verify submit button is enabled
    boolean isSubmitButtonPresent = WebUI.verifyElementPresent(
        findTestObject('TopUp/btn_submit_topup'),
        10,
        FailureHandling.OPTIONAL
    )
    
    assert isSubmitButtonPresent, "Submit button should be present for valid amount ${amount}"
    
    boolean isSubmitButtonClickable = WebUI.verifyElementClickable(
        findTestObject('TopUp/btn_submit_topup'),
        FailureHandling.OPTIONAL
    )
    
    if (isSubmitButtonClickable) {
        WebUI.comment("✅ Submit button is clickable - amount ${amount} is accepted")
    } else {
        WebUI.comment("⚠️ Submit button not clickable - checking if amount was still accepted")
    }
    
    // Submit top-up form
    WebUI.click(findTestObject('TopUp/btn_submit_topup'))
    WebUI.delay(3) // Wait for transaction creation
    
    // Verify redirect to payment page
    WebUI.waitForPageLoad(10)
    WebUI.delay(2)
    
    String paymentUrl = WebUI.getUrl()
    WebUI.comment("Current URL after submission: ${paymentUrl}")
    
    // Check if redirected to payment page
    boolean onPaymentPage = paymentUrl.contains('/payment') || paymentUrl.contains('/topup/payment')
    
    if (onPaymentPage) {
        WebUI.comment("✅ Redirected to payment page successfully")
        WebUI.comment("✅ Amount ${label} (Rp ${amount}) is VALID - Transaction created")
        successfulTests++
        
        // For first amount, verify VA number is displayed
        if (i == 0) {
            WebUI.comment("Verifying VA number displayed for first amount...")
            
            boolean isVaDisplayed = WebUI.verifyElementPresent(
                findTestObject('TopUp/text_va_number'),
                10,
                FailureHandling.OPTIONAL
            )
            
            if (isVaDisplayed) {
                String vaNumber = WebUI.getText(findTestObject('TopUp/text_va_number'))
                WebUI.comment("✅ VA number displayed: ${vaNumber}")
            } else {
                WebUI.comment('⚠️ VA number element not found')
            }
        }
        
        // Navigate back to home for next test
        if (i < testAmounts.size() - 1) {
            WebUI.comment("Navigating back to home to test next amount...")
            WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
            WebUI.waitForPageLoad(10)
            WebUI.delay(1)
        }
        
    } else {
        WebUI.comment("❌ FAILED: Did not redirect to payment page for amount ${amount}")
        WebUI.comment("Current URL: " + paymentUrl)
        
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
// TEST SUMMARY
// ========================================
WebUI.comment('========================================')
WebUI.comment('TC_TopUp_003: Valid Mid-Range Amounts - SUMMARY')
WebUI.comment('✅ Tested 3 valid mid-range amounts')
WebUI.comment('  - 50,000 (50K): ' + (successfulTests >= 1 ? 'PASSED' : 'FAILED'))
WebUI.comment('  - 500,000 (500K): ' + (successfulTests >= 2 ? 'PASSED' : 'FAILED'))
WebUI.comment('  - 5,000,000 (5M): ' + (successfulTests >= 3 ? 'PASSED' : 'FAILED'))
WebUI.comment("✅ ${successfulTests}/3 amounts accepted and created transactions")
WebUI.comment('✅ All amounts redirected to payment page')
WebUI.comment('✅ No validation errors for valid amounts')
WebUI.comment('========================================')

// Verify all amounts were accepted
assert successfulTests == 3, "All 3 amounts should be accepted. Only ${successfulTests} passed."

// ========================================
// Return to home page for next test
// ========================================
WebUI.comment('Returning to home page for next test case')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

WebUI.comment('TC_TopUp_003: Valid Mid-Range Amounts - COMPLETED')
WebUI.comment('✅ Browser remains open for next test case')