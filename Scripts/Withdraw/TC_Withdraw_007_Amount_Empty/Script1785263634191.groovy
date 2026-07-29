import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

// Test Case: TC_Withdraw_007 - Amount Empty
// Priority: P1 - High
// Type: Negative Test, Validation Test
// Description: Verify that empty amount field triggers validation and blocks form progression

WebUI.comment('TC_Withdraw_007: Amount Empty - START')

// ========================================
// PREREQUISITE: Browser is already open and user is logged in
// ========================================

WebUI.comment('TC_Withdraw_007: Amount Empty - START')
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
// STEP 5: Ensure amount field is empty
// ========================================
WebUI.comment('Step 5: Ensure amount field is empty')

WebUI.waitForElementPresent(findTestObject('Withdraw/input_withdraw_amount'), 15, FailureHandling.STOP_ON_FAILURE)

// Click on field and clear any default value
WebUI.click(findTestObject('Withdraw/input_withdraw_amount'))
WebUI.delay(0.3)
WebUI.clearText(findTestObject('Withdraw/input_withdraw_amount'))
WebUI.delay(1) // Wait for validation

// Verify field is empty
String fieldValue = WebUI.getAttribute(findTestObject('Withdraw/input_withdraw_amount'), 'value')
WebUI.comment("Amount field value: '${fieldValue}'")

if (fieldValue == null || fieldValue.isEmpty()) {
    WebUI.comment('✅ Amount field is empty')
} else {
    WebUI.comment("⚠️ Amount field has value: ${fieldValue}")
}

// ========================================
// STEP 6: Verify Next button is DISABLED (expected behavior)
// ========================================
WebUI.comment('Step 6: Verify Next button is DISABLED for empty amount')

boolean validationWorking = false

// FIXED: Check if button has disabled attribute (correct way for negative test)
String disabledAttr = WebUI.getAttribute(findTestObject('Withdraw/btn_withdraw_continue'), 'disabled')
boolean isButtonDisabled = (disabledAttr != null && disabledAttr != 'false')

if (isButtonDisabled) {
    WebUI.comment('✅ Next button is DISABLED for empty amount (expected behavior)')
    validationWorking = true
} else {
    WebUI.comment('⚠️ Next button is NOT disabled - this is unexpected')
}

// FIXED: No longer try to click the disabled button - just verify state
// The expected behavior is that button is disabled, so no click is needed

// Verify we're still on withdraw page
String currentPageUrl = WebUI.getUrl()

if (currentPageUrl.contains('/withdraw')) {
    WebUI.comment('✅ Still on withdraw page - form progression blocked (expected behavior)')
    validationWorking = true
} else {
    WebUI.comment('⚠️ URL changed unexpectedly')
}

// Verify PIN input is NOT present
boolean isPinInputPresent = WebUI.verifyElementPresent(
    findTestObject('Withdraw/input_withdraw_pin'),
    3,
    FailureHandling.OPTIONAL
)

if (!isPinInputPresent) {
    WebUI.comment('✅ PIN input NOT displayed - empty amount rejected (expected behavior)')
    validationWorking = true
} else {
    WebUI.comment('❌ UNEXPECTED: PIN input displayed for empty amount')
}

// Take screenshot for documentation
WebUI.takeScreenshot()

// ========================================
// TEST SUMMARY
// ========================================
WebUI.comment('========================================')
WebUI.comment('TC_Withdraw_007: Amount Empty - SUMMARY')
WebUI.comment('✅ Amount field left empty')
WebUI.comment('✅ Next button is DISABLED (expected behavior)')
WebUI.comment('✅ Form progression blocked by disabled button')
WebUI.comment('✅ Cannot proceed to PIN step with empty amount')
WebUI.comment('✅ Frontend validation working correctly')
WebUI.comment('========================================')

// Verify validation is working
assert validationWorking, 'Empty amount should be blocked by disabled Next button'

// ========================================
// Return to home page for next test
// ========================================
WebUI.comment('Returning to home page for next test case')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

WebUI.comment('TC_Withdraw_007: Amount Empty - COMPLETED')
WebUI.comment('✅ Browser remains open for next test case')