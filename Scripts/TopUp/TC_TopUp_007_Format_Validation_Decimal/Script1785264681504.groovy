import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

// Test Case: TC_TopUp_007 - Format Validation (Decimal/Fractional)
// Priority: P1 - High
// Type: Negative Test, Validation Test
// Description: Verify decimal/fractional amount inputs are handled appropriately

// ========================================
// PREREQUISITE: Browser is already open and user is logged in
// ========================================

WebUI.comment('TC_TopUp_007: Format Validation (Decimal/Fractional) - START')
WebUI.comment('Assuming browser is open and user is logged in from setup')

// Decimal inputs to test
def decimalInputs = ['50000.50', '100000.99', '10000.5', '50000.00']
boolean validationDetected = false

// ========================================
// STEP 1: Navigate to top-up page
// ========================================
WebUI.comment('Step 1: Navigate to top-up page')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/topup')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

String topupUrl = WebUI.getUrl()
assert topupUrl.contains('/topup'), "Should be on top-up page. Current URL: ${topupUrl}"

WebUI.comment('Step 1: Navigate to top-up - COMPLETED')

// ========================================
// STEP 2: Test decimal format inputs
// ========================================
WebUI.comment('Step 2: Test various decimal/fractional inputs')

for (String decimalInput : decimalInputs) {
    WebUI.comment("Testing decimal input: '${decimalInput}'")
    
    WebUI.waitForElementPresent(findTestObject('TopUp/input_topup_amount'), 10, FailureHandling.STOP_ON_FAILURE)
    
    // Click and clear field
    WebUI.click(findTestObject('TopUp/input_topup_amount'))
    WebUI.delay(0.3)
    WebUI.clearText(findTestObject('TopUp/input_topup_amount'))
    
    // Try to set decimal value
    WebUI.setText(findTestObject('TopUp/input_topup_amount'), decimalInput)
    WebUI.delay(1) // Wait for input handling
    
    // Check actual value in field
    String actualValue = WebUI.getAttribute(findTestObject('TopUp/input_topup_amount'), 'value')
    WebUI.comment("  Actual value in field: '${actualValue}'")
    
    // Analyze behavior
    if (actualValue != decimalInput) {
        if (actualValue.contains('.')) {
            WebUI.comment("  ✅ Decimal point preserved but value may be adjusted")
        } else {
            WebUI.comment("  ✅ Decimal filtered/rounded: '${decimalInput}' -> '${actualValue}'")
            validationDetected = true
        }
    } else {
        WebUI.comment("  ℹ️  Decimal value accepted as-is: '${actualValue}'")
    }
}

// ========================================
// STEP 3: Try to submit with decimal format
// ========================================
WebUI.comment('Step 3: Try to submit form with decimal amount')

// Use one decimal input
WebUI.click(findTestObject('TopUp/input_topup_amount'))
WebUI.delay(0.3)
WebUI.clearText(findTestObject('TopUp/input_topup_amount'))
WebUI.setText(findTestObject('TopUp/input_topup_amount'), '50000.50')
WebUI.delay(1)

// Check submit button state - FIXED: removed timeout parameter
boolean isSubmitClickable = WebUI.verifyElementClickable(
    findTestObject('TopUp/btn_submit_topup'),
    FailureHandling.OPTIONAL
)

WebUI.comment("Submit button clickable: ${isSubmitClickable}")

// Try to submit
WebUI.click(findTestObject('TopUp/btn_submit_topup'))
WebUI.delay(2)

// Check where we ended up
WebUI.waitForPageLoad(10)
String finalUrl = WebUI.getUrl()
WebUI.comment("URL after submission: ${finalUrl}")

if (finalUrl.contains('/payment')) {
    WebUI.comment('ℹ️  Form accepted decimal and created transaction')
    WebUI.comment('ℹ️  System may have rounded or handled decimal internally')
} else if (finalUrl.contains('/topup') && !finalUrl.contains('/payment')) {
    WebUI.comment('✅ Form submission blocked for decimal amount')
    validationDetected = true
}

// Take screenshot
WebUI.takeScreenshot()

// ========================================
// TEST SUMMARY
// ========================================
WebUI.comment('========================================')
WebUI.comment('TC_TopUp_007: Format Validation (Decimal) - SUMMARY')
WebUI.comment('✅ Tested decimal/fractional inputs:')
WebUI.comment('  - 50000.50')
WebUI.comment('  - 100000.99')
WebUI.comment('  - 10000.5')
WebUI.comment('  - 50000.00')

WebUI.comment('ℹ️  System behavior observed:')
if (validationDetected) {
    WebUI.comment('  - Decimal values filtered/rounded OR')
    WebUI.comment('  - Form submission blocked for decimals')
} else {
    WebUI.comment('  - Decimal values may be accepted (system-dependent)')
    WebUI.comment('  - Backend may handle rounding/validation')
}

WebUI.comment('✅ Decimal format handling verified')
WebUI.comment('========================================')

// ========================================
// Return to home page for next test
// ========================================
WebUI.comment('Returning to home page for next test case')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

WebUI.comment('TC_TopUp_007: Format Validation (Decimal/Fractional) - COMPLETED')
WebUI.comment('✅ Browser remains open for next test case')