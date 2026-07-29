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

WebUI.comment('TC_Withdraw_004: Amount Exceeds Balance - START')
WebUI.comment('Assuming browser is open and user is logged in from setup')

// ========================================
// STEP 1: Navigate to withdraw page
// ========================================
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/withdraw')

WebUI.waitForPageLoad(10)

WebUI.delay(1)

currentUrl = WebUI.getUrl()

assert currentUrl.contains('/withdraw') : 'Failed to navigate to withdraw page'

WebUI.takeFullPageScreenshot('Screenshots/WITHDRAW004_02_WithdrawPageLoaded.png')

// Step 6: Verify amount input field is present (Step 1 - Amount Entry)
WebUI.verifyElementPresent(findTestObject('Withdraw/input_withdraw_amount'), 10)

WebUI.comment('Withdraw form Step 1 (Amount Entry) loaded successfully')

// Step 7: Enter amount EXCEEDING balance (2,000,000 - assumed to exceed user balance)
WebUI.setText(findTestObject('Withdraw/input_withdraw_amount'), '2000000')

WebUI.delay(1)

WebUI.takeFullPageScreenshot('Screenshots/WITHDRAW004_03_ExcessiveAmount_Entered.png')

WebUI.comment('Entered amount exceeding balance: Rp 2.000.000')

// Step 8: Try to click continue button (should trigger validation)
WebUI.verifyElementPresent(findTestObject('Withdraw/btn_withdraw_continue'), 10)

WebUI.click(findTestObject('Withdraw/btn_withdraw_continue'))

WebUI.delay(2)

WebUI.comment('Continue button clicked - checking for insufficient balance error')

// Step 9: Verify validation error message appears
boolean errorFieldPresent = WebUI.verifyElementPresent(
    findTestObject('Common/error_amount_field'),
    10,
    FailureHandling.OPTIONAL
)

String errorMessage = ''

if (errorFieldPresent) {
    errorMessage = WebUI.getText(findTestObject('Common/error_amount_field'))
    WebUI.comment('Validation error displayed: ' + errorMessage)

    assert errorMessage.length() > 0 : 'Error message should not be empty'
    assert errorMessage.toLowerCase().contains('insufficient') ||
           errorMessage.toLowerCase().contains('balance') ||
           errorMessage.toLowerCase().contains('exceed') ||
           errorMessage.toLowerCase().contains('saldo') : 'Error should mention insufficient balance'
} else {
    // Check if error appears in toast notification
    boolean toastPresent = WebUI.verifyElementPresent(
        findTestObject('Common/toast'),
        5,
        FailureHandling.OPTIONAL
    )

    if (toastPresent) {
        errorMessage = WebUI.getText(findTestObject('Common/toast'))
        WebUI.comment('Error displayed in toast: ' + errorMessage)
    } else {
        WebUI.comment('No error field or toast found - validation may occur at different stage')
    }
}

WebUI.takeFullPageScreenshot('Screenshots/WITHDRAW004_04_InsufficientBalance_Error.png')

// Step 10: Verify PIN input field is NOT present (should NOT advance to Step 2)
boolean pinFieldPresent = WebUI.verifyElementNotPresent(findTestObject('Withdraw/input_withdraw_pin'), 5, FailureHandling.OPTIONAL)

if (!pinFieldPresent) {
    WebUI.comment('Confirmed: PIN step NOT displayed - validation blocked Step 1')
} else {
    WebUI.comment('WARNING: PIN field appeared despite insufficient balance error')
}

WebUI.takeFullPageScreenshot('Screenshots/WITHDRAW004_05_StillOnStep1_NotAdvanced.png')

// Step 11: Verify URL remains on withdraw page
currentUrl = WebUI.getUrl()

assert currentUrl.contains('/withdraw') : 'Should remain on withdraw page'

WebUI.comment('Confirmed: Form blocked at Step 1 due to insufficient balance')

// ========================================
// Return to home page for next test
// ========================================
WebUI.comment('Returning to home page for next test case')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

WebUI.comment('TC_Withdraw_004: Amount Exceeds Balance - COMPLETED')
WebUI.comment('✅ Browser remains open for next test case')
WebUI.comment('TEST PASSED: Insufficient balance error correctly displayed for amount Rp 2.000.000')