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

WebUI.comment('Assuming browser is open and user is logged in from setup')

// Step 1: Navigate to Top Up page
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/topup')

WebUI.waitForPageLoad(10)

WebUI.delay(1)

currentUrl = WebUI.getUrl()

assert currentUrl.contains('/topup') : 'Failed to navigate to topup page'

WebUI.takeFullPageScreenshot('Screenshots/TOPUP004_02_TopUpPageLoaded.png')

// Step 5: Verify amount input field is present
WebUI.verifyElementPresent(findTestObject('TopUp/input_topup_amount'), 10)

WebUI.comment('Top Up form loaded successfully')

// Step 6: Enter amount BELOW minimum (9,999 - below minimum of 10,000)
WebUI.setText(findTestObject('TopUp/input_topup_amount'), '9999')

WebUI.delay(1)

WebUI.takeFullPageScreenshot('Screenshots/TOPUP004_03_BelowMinimumAmount_Entered.png')

WebUI.comment('Entered invalid amount: Rp 9.999 (below minimum Rp 10.000)')

// Step 7: Try to click submit button (may trigger validation)
WebUI.verifyElementPresent(findTestObject('TopUp/btn_submit_topup'), 10)

WebUI.click(findTestObject('TopUp/btn_submit_topup'))

WebUI.delay(2)

WebUI.comment('Submit button clicked - checking for validation error')

// Step 8: Verify validation error message appears
WebUI.waitForElementPresent(findTestObject('Common/error_amount_field'), 10)

WebUI.verifyElementPresent(findTestObject('Common/error_amount_field'), 10)

String errorMessage = WebUI.getText(findTestObject('Common/error_amount_field'))

WebUI.comment('Validation error displayed: ' + errorMessage)

assert errorMessage.length() > 0 : 'Error message should not be empty'

assert errorMessage.toLowerCase().contains('minimum') || errorMessage.contains('10') : 'Error should mention minimum amount'

WebUI.takeFullPageScreenshot('Screenshots/TOPUP004_04_ValidationError_Displayed.png')

// Step 9: Verify URL remains on topup page (not redirected to payment)
currentUrl = WebUI.getUrl()

assert currentUrl.contains('/topup') && !currentUrl.contains('/payment') : 'Should remain on topup form, not redirect to payment'

WebUI.comment('Confirmed: Form submission blocked, still on topup page')

WebUI.takeFullPageScreenshot('Screenshots/TOPUP004_05_FormSubmission_Blocked.png')

// ========================================
// Return to home page for next test
// ========================================
WebUI.comment('Returning to home page for next test case')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

WebUI.comment('TEST PASSED: Validation error correctly displayed for amount below minimum (Rp 9.999)')
WebUI.comment('✅ Browser remains open for next test case')