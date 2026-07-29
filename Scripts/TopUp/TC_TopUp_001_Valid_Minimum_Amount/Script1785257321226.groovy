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
// This test case is part of a test suite that reuses browser session
// ========================================

WebUI.comment('Assuming browser is open and user is logged in from setup')

// Step 1: Navigate to Top Up page
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/topup')

WebUI.waitForPageLoad(10)

WebUI.delay(1)

currentUrl = WebUI.getUrl()

assert currentUrl.contains('/topup') : 'Failed to navigate to topup page'

WebUI.takeFullPageScreenshot('Screenshots/TOPUP001_02_TopUpPageLoaded.png')

// Step 5: Verify amount input field is present
WebUI.verifyElementPresent(findTestObject('TopUp/input_topup_amount'), 10)

WebUI.comment('Top Up form loaded successfully')

// Step 6: Enter minimum valid amount (10,000)
WebUI.setText(findTestObject('TopUp/input_topup_amount'), '10000')

WebUI.delay(1)

WebUI.takeFullPageScreenshot('Screenshots/TOPUP001_03_AmountEntered.png')

// Step 7: Click submit/continue button
WebUI.verifyElementPresent(findTestObject('TopUp/btn_submit_topup'), 10)

WebUI.click(findTestObject('TopUp/btn_submit_topup'))

WebUI.comment('Submit button clicked, waiting for redirect to payment page')

// Step 8: Wait for redirect to payment page
WebUI.delay(3)

currentUrl = WebUI.getUrl()

assert currentUrl.contains('/topup/payment') : 'Failed to redirect to payment page'

WebUI.comment('Successfully redirected to payment page: ' + currentUrl)

WebUI.takeFullPageScreenshot('Screenshots/TOPUP001_04_PaymentPageLoaded.png')

// Step 9: Verify VA number is displayed
WebUI.waitForElementPresent(findTestObject('TopUp/text_va_number'), 10)

WebUI.verifyElementPresent(findTestObject('TopUp/text_va_number'), 10)

String vaNumber = WebUI.getText(findTestObject('TopUp/text_va_number'))

WebUI.comment('VA Number displayed: ' + vaNumber)

assert vaNumber.length() > 0 : 'VA number should not be empty'

assert vaNumber.startsWith('8808') : 'VA number should start with 8808'

WebUI.takeFullPageScreenshot('Screenshots/TOPUP001_05_VANumber_Displayed.png')

// Step 10: Verify amount is displayed on payment page
WebUI.verifyElementPresent(findTestObject('TopUp/text_payment_amount'), 10, FailureHandling.OPTIONAL)

String displayedAmount = WebUI.getText(findTestObject('TopUp/text_payment_amount'), FailureHandling.OPTIONAL)

if (displayedAmount != null && displayedAmount.length() > 0) {
    WebUI.comment('Payment amount displayed: ' + displayedAmount)
    assert displayedAmount.contains('10') : 'Payment amount should contain 10 (for 10,000)'
}

WebUI.takeFullPageScreenshot('Screenshots/TOPUP001_06_PaymentDetails_Complete.png')

// Step 11: Verify URL contains transaction ID parameter
assert currentUrl.contains('tx=') : 'Payment URL should contain transaction ID parameter'

WebUI.comment('Transaction created successfully with transaction ID in URL')

// Step 12: Return to home page for next test
WebUI.comment('Test completed - returning to home page')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

WebUI.comment('✅ TEST PASSED: Top-up transaction created successfully with minimum amount Rp 10.000')
WebUI.comment('✅ Browser remains open for next test case')
