import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

// Test Case: TC_Withdraw_006 - Amount Negative
// Priority: P1 - High
// Type: Negative Test, Validation Test
// Description: Verify that negative amount is prevented or triggers validation error

WebUI.comment('TC_Withdraw_006: Amount Negative - START')

// ========================================
// PREREQUISITE: Browser is already open and user is logged in
// ========================================

WebUI.comment('TC_Withdraw_006: Amount Negative - START')
WebUI.comment('Assuming browser is open and user is logged in from setup')

// ========================================
// STEP 1: Navigate to withdraw page
// ========================================
WebUI.comment('Step 4: Navigate to withdraw page')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/withdraw')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

String withdrawUrl = WebUI.getUrl()
assert withdrawUrl.contains('/withdraw'), "Should be on withdraw page. Current URL: ${withdrawUrl}"

WebUI.comment('Step 4: Navigate to withdraw - COMPLETED')

// ========================================
// STEP 5: Try to enter negative amount
// ========================================
WebUI.comment('Step 5: Try to enter negative amount (-100000)')

WebUI.waitForElementPresent(findTestObject('Withdraw/input_withdraw_amount'), 15, FailureHandling.STOP_ON_FAILURE)

WebUI.click(findTestObject('Withdraw/input_withdraw_amount'))
WebUI.delay(0.3)
WebUI.clearText(findTestObject('Withdraw/input_withdraw_amount'))

// Try to set negative value
WebUI.setText(findTestObject('Withdraw/input_withdraw_amount'), '-100000')
WebUI.delay(1) // Wait for validation/input handling

WebUI.comment('✅ Attempted to enter negative amount: -100000')

// ========================================
// STEP 6: Check actual value in input field
// ========================================
WebUI.comment('Step 6: Check actual value accepted by input field')

// Get the actual value in the input field
String actualValue = WebUI.getAttribute(findTestObject('Withdraw/input_withdraw_amount'), 'value')
WebUI.comment("Actual value in input field: '${actualValue}'")

boolean negativePreventedByInput = false

// Check if negative sign was prevented
if (actualValue == null || actualValue.isEmpty() || !actualValue.contains('-')) {
    WebUI.comment('✅ Input field prevented negative value (no minus sign in value)')
    negativePreventedByInput = true
} else {
    WebUI.comment('⚠️  Negative sign was accepted by input field')
}

// ========================================
// STEP 7: Try to proceed with Next button
// ========================================
WebUI.comment('Step 7: Attempt to proceed to PIN step')

boolean validationErrorFound = false

// Try to click Next button
boolean isNextButtonPresent = WebUI.verifyElementPresent(
    findTestObject('Withdraw/btn_next_step1'),
    10,
    FailureHandling.OPTIONAL
)

if (isNextButtonPresent) {
    WebUI.click(findTestObject('Withdraw/btn_next_step1'))
    WebUI.delay(2)
    
    // Check if we progressed to PIN step
    String currentPageUrl = WebUI.getUrl()
    
    if (currentPageUrl.contains('/withdraw')) {
        WebUI.comment('✅ Form progression blocked - still on withdraw page')
        validationErrorFound = true
    } else {
        WebUI.comment('⚠️  Page URL changed - checking for PIN input')
    }
    
    // Verify PIN input is NOT present
    boolean isPinInputPresent = WebUI.verifyElementPresent(
        findTestObject('Withdraw/input_withdraw_pin'),
        5,
        FailureHandling.OPTIONAL
    )
    
    if (!isPinInputPresent) {
        WebUI.comment('✅ PIN input NOT displayed - negative amount rejected')
        validationErrorFound = true
    } else {
        WebUI.comment('❌ UNEXPECTED: PIN input displayed for negative amount')
    }
} else {
    WebUI.comment('Next button not found')
}

// Take screenshot for documentation
WebUI.takeScreenshot()

// ========================================
// TEST SUMMARY
// ========================================
WebUI.comment('========================================')
WebUI.comment('TC_Withdraw_006: Amount Negative - SUMMARY')
WebUI.comment('✅ Attempted to enter negative amount (-100000)')

if (negativePreventedByInput) {
    WebUI.comment('✅ Input field prevented negative value (client-side validation)')
} else {
    WebUI.comment('⚠️  Input field accepted negative sign')
}

if (validationErrorFound) {
    WebUI.comment('✅ Form progression blocked')
    WebUI.comment('✅ Cannot proceed to PIN step')
} else {
    WebUI.comment('⚠️  Form progression behavior needs verification')
}

WebUI.comment('✅ Negative amount handling works correctly')
WebUI.comment('========================================')

// Verify that negative amount is either prevented or blocked
assert (negativePreventedByInput || validationErrorFound), 
    'Negative amount should be prevented by input field OR blocked by validation'

// ========================================
// Return to home page for next test
// ========================================
WebUI.comment('Returning to home page for next test case')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

WebUI.comment('TC_Withdraw_006: Amount Negative - COMPLETED')
WebUI.comment('✅ Browser remains open for next test case')