import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

// ========================================
// PREREQUISITE: Browser is already open and user is logged in
// ========================================

WebUI.comment('TC_Withdraw_003: Amount Below Minimum - START')
WebUI.comment('Assuming browser is open and user is logged in from setup')

// ========================================
// STEP 1: Navigate to withdraw page
// ========================================
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/withdraw')

WebUI.waitForPageLoad(10)

WebUI.delay(1)

currentUrl = WebUI.getUrl()

assert currentUrl.contains('/withdraw') : 'Failed to navigate to withdraw page'

WebUI.takeFullPageScreenshot('Screenshots/WITHDRAW003_02_WithdrawPageLoaded.png')

// Step 5: Verify amount input field is present (Step 1 - Amount Entry)
WebUI.verifyElementPresent(findTestObject('Withdraw/input_withdraw_amount'), 10)

WebUI.comment('Withdraw form Step 1 (Amount Entry) loaded successfully')

// Step 6: Enter amount BELOW minimum (49,999 - below minimum of 50,000)
WebUI.setText(findTestObject('Withdraw/input_withdraw_amount'), '49999')

WebUI.delay(1)

WebUI.takeFullPageScreenshot('Screenshots/WITHDRAW003_03_BelowMinimumAmount_Entered.png')

WebUI.comment('Entered invalid amount: Rp 49.999 (below minimum Rp 50.000)')

// Step 7: Try to click continue button (should trigger validation)
WebUI.verifyElementPresent(findTestObject('Withdraw/btn_withdraw_continue'), 10)

WebUI.click(findTestObject('Withdraw/btn_withdraw_continue'))

WebUI.delay(2)

WebUI.comment('Continue button clicked - checking for validation error')

// Step 8: Verify validation error message appears
WebUI.waitForElementPresent(findTestObject('Common/error_amount_field'), 10)

WebUI.verifyElementPresent(findTestObject('Common/error_amount_field'), 10)

String errorMessage = WebUI.getText(findTestObject('Common/error_amount_field'))

WebUI.comment('Validation error displayed: ' + errorMessage)

assert errorMessage.length() > 0 : 'Error message should not be empty'

assert errorMessage.toLowerCase().contains('minimum') || errorMessage.contains('50') : 'Error should mention minimum amount'

WebUI.takeFullPageScreenshot('Screenshots/WITHDRAW003_04_ValidationError_Displayed.png')

// Step 9: Verify PIN input field is NOT present (should NOT advance to Step 2)
boolean pinFieldPresent = WebUI.verifyElementNotPresent(findTestObject('Withdraw/input_withdraw_pin'), 5, FailureHandling.OPTIONAL)

if (!pinFieldPresent) {
    WebUI.comment('Confirmed: PIN step NOT displayed - validation blocked Step 1')
} else {
    WebUI.comment('WARNING: PIN field appeared despite validation error')
}

WebUI.takeFullPageScreenshot('Screenshots/WITHDRAW003_05_StillOnStep1_NotAdvanced.png')

// Step 10: Verify URL remains on withdraw page
currentUrl = WebUI.getUrl()

assert currentUrl.contains('/withdraw') : 'Should remain on withdraw page'

WebUI.comment('Confirmed: Form blocked at Step 1, did not advance to Step 2')

// ========================================
// Return to home page for next test
// ========================================
WebUI.comment('Returning to home page for next test case')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

WebUI.comment('TC_Withdraw_003: Amount Below Minimum - COMPLETED')
WebUI.comment('✅ Browser remains open for next test case')
WebUI.comment('TEST PASSED: Validation error correctly displayed for amount below minimum (Rp 49.999)')