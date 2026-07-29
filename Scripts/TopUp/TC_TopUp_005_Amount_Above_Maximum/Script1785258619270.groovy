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

WebUI.comment('TC_TopUp_005: Amount Above Maximum - START')
WebUI.comment('Assuming browser is open and user is logged in from setup')

// Maximum amount + 1 (exceeds limit)
long invalidAmount = 10000001

// ========================================
// STEP 1: Navigate to top-up page
// ========================================
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/topup')

WebUI.waitForPageLoad(10)

WebUI.delay(1)

String currentUrl = WebUI.getUrl()

assert currentUrl.contains('/topup') : 'Failed to navigate to topup page'

WebUI.takeFullPageScreenshot('Screenshots/TOPUP005_02_TopUpPageLoaded.png')

// Step 2: Verify amount input field is present
WebUI.verifyElementPresent(findTestObject('TopUp/input_topup_amount'), 10)

WebUI.comment('Top Up form loaded successfully')

// Step 3: Enter amount ABOVE maximum (10,000,001 - above maximum of 10,000,000)
WebUI.setText(findTestObject('TopUp/input_topup_amount'), '10000001')

WebUI.delay(1)

WebUI.takeFullPageScreenshot('Screenshots/TOPUP005_03_AboveMaximumAmount_Entered.png')

WebUI.comment('Entered invalid amount: Rp 10.000.001 (above maximum Rp 10.000.000)')

// Step 4: Try to click submit button (may trigger validation)
WebUI.verifyElementPresent(findTestObject('TopUp/btn_submit_topup'), 10)

WebUI.click(findTestObject('TopUp/btn_submit_topup'))

WebUI.delay(2)

WebUI.comment('Submit button clicked - checking for validation error')

// Step 5: Verify validation error message appears
WebUI.waitForElementPresent(findTestObject('Common/error_amount_field'), 10)

WebUI.verifyElementPresent(findTestObject('Common/error_amount_field'), 10)

String errorMessage = WebUI.getText(findTestObject('Common/error_amount_field'))

WebUI.comment('Validation error displayed: ' + errorMessage)

assert errorMessage.length() > 0 : 'Error message should not be empty'

assert errorMessage.toLowerCase().contains('maximum') || errorMessage.contains('10.000.000') : 'Error should mention maximum amount'

WebUI.takeFullPageScreenshot('Screenshots/TOPUP005_04_ValidationError_Displayed.png')

// Step 6: Verify URL remains on topup page (not redirected to payment)
currentUrl = WebUI.getUrl()

assert currentUrl.contains('/topup') && !currentUrl.contains('/payment') : 'Should remain on topup form, not redirect to payment'

WebUI.comment('Confirmed: Form submission blocked, still on topup page')

WebUI.takeFullPageScreenshot('Screenshots/TOPUP005_05_FormSubmission_Blocked.png')

// ========================================
// Return to home page for next test
// ========================================
WebUI.comment('Returning to home page for next test case')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

WebUI.comment('TC_TopUp_005: Amount Above Maximum - COMPLETED')
WebUI.comment('✅ Browser remains open for next test case')
WebUI.comment('TEST PASSED: Validation error correctly displayed for amount above maximum (Rp 10.000.001)')