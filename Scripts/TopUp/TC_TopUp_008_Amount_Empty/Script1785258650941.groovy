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

WebUI.comment('TC_TopUp_008: Amount Empty - START')
WebUI.comment('Assuming browser is open and user is logged in from setup')

// ========================================
// STEP 1: Navigate to top-up page
// ========================================
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/topup')

WebUI.waitForPageLoad(10)

WebUI.delay(1)

String currentUrl = WebUI.getUrl()

assert currentUrl.contains('/topup') : 'Failed to navigate to topup page'

WebUI.takeFullPageScreenshot('Screenshots/TOPUP008_02_TopUpPageLoaded.png')

// Step 2: Verify amount input field is present
WebUI.verifyElementPresent(findTestObject('TopUp/input_topup_amount'), 10)

WebUI.comment('Top Up form loaded successfully')

// Step 3: Leave amount field EMPTY (do not enter any value)
WebUI.comment('Amount field left empty - no value entered')

WebUI.takeFullPageScreenshot('Screenshots/TOPUP008_03_AmountField_Empty.png')

// Step 4: Try to click submit button
WebUI.verifyElementPresent(findTestObject('TopUp/btn_submit_topup'), 10)

// Check if button is clickable/enabled
boolean isButtonClickable = WebUI.verifyElementClickable(findTestObject('TopUp/btn_submit_topup'), FailureHandling.OPTIONAL)

if (!isButtonClickable) {
    WebUI.comment('Submit button is DISABLED with empty amount - validation working correctly')
    WebUI.takeFullPageScreenshot('Screenshots/TOPUP008_04_SubmitButton_Disabled.png')
} else {
    WebUI.comment('Submit button is enabled - attempting to click to trigger validation')
    WebUI.click(findTestObject('TopUp/btn_submit_topup'))
    WebUI.delay(2)
    
    // Step 5: Check for validation error message
    boolean errorPresent = WebUI.verifyElementPresent(findTestObject('Common/error_amount_field'), 10, FailureHandling.OPTIONAL)
    
    if (errorPresent) {
        String errorMessage = WebUI.getText(findTestObject('Common/error_amount_field'))
        WebUI.comment('Validation error displayed: ' + errorMessage)
        assert errorMessage.length() > 0 : 'Error message should not be empty'
        WebUI.takeFullPageScreenshot('Screenshots/TOPUP008_04_ValidationError_Displayed.png')
    } else {
        WebUI.comment('No explicit error message, but checking if form submission was blocked')
    }
    
    // Step 6: Verify URL remains on topup page (not redirected to payment)
    currentUrl = WebUI.getUrl()
    assert currentUrl.contains('/topup') && !currentUrl.contains('/payment') : 'Should remain on topup form with empty amount'
    WebUI.comment('Confirmed: Form submission blocked with empty amount')
}

WebUI.takeFullPageScreenshot('Screenshots/TOPUP008_05_EmptyAmount_Validation_Passed.png')

// ========================================
// Return to home page for next test
// ========================================
WebUI.comment('Returning to home page for next test case')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

WebUI.comment('TC_TopUp_008: Amount Empty - COMPLETED')
WebUI.comment('✅ Browser remains open for next test case')
WebUI.comment('TEST PASSED: Empty amount field is properly validated - transaction cannot be created')