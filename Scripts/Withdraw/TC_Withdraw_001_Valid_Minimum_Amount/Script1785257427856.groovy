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

WebUI.comment('TC_Withdraw_001: Valid Minimum Amount - START')
WebUI.comment('Assuming browser is open and user is logged in from setup')

// ========================================
// STEP 1: Navigate to withdraw page
// ========================================
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/withdraw')

WebUI.waitForPageLoad(10)

WebUI.delay(1)

String currentUrl = WebUI.getUrl()

assert currentUrl.contains('/withdraw') : 'Failed to navigate to withdraw page'

WebUI.takeFullPageScreenshot('Screenshots/WITHDRAW001_02_WithdrawPageLoaded.png')

// Step 2: Verify amount input field is present (Step 1 - Amount Entry)
WebUI.verifyElementPresent(findTestObject('Withdraw/input_withdraw_amount'), 10)

WebUI.comment('Withdraw form Step 1 (Amount Entry) loaded successfully')

// Step 3: Enter minimum valid amount (50,000)
WebUI.setText(findTestObject('Withdraw/input_withdraw_amount'), '50000')

WebUI.delay(1)

WebUI.takeFullPageScreenshot('Screenshots/WITHDRAW001_03_AmountEntered.png')

WebUI.comment('Entered minimum valid amount: Rp 50.000')

// Step 4: Click continue button to proceed to Step 2 (PIN Entry)
WebUI.verifyElementPresent(findTestObject('Withdraw/btn_withdraw_continue'), 10)

WebUI.click(findTestObject('Withdraw/btn_withdraw_continue'))

WebUI.comment('Continue button clicked, advancing to PIN entry step')

// Step 5: Wait for Step 2 (PIN Entry) to appear
WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/WITHDRAW001_04_PINStepLoaded.png')

// Step 6: Verify PIN input field is present (Step 2 displayed)
WebUI.verifyElementPresent(findTestObject('Withdraw/input_withdraw_pin'), 10)

WebUI.comment('Successfully advanced to Step 2 - PIN Entry')

// Step 7: Verify submit button is present on PIN step
WebUI.verifyElementPresent(findTestObject('Withdraw/btn_withdraw_submit'), 10)

WebUI.comment('Submit button visible on PIN step')

// Step 8: Verify URL is still on withdraw page (not redirected)
currentUrl = WebUI.getUrl()

assert currentUrl.contains('/withdraw') : 'Should remain on withdraw page'

WebUI.takeFullPageScreenshot('Screenshots/WITHDRAW001_05_PINStep_Verified.png')

// ========================================
// Return to home page for next test
// ========================================
WebUI.comment('Returning to home page for next test case')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

WebUI.comment('TC_Withdraw_001: Valid Minimum Amount - COMPLETED')
WebUI.comment('✅ Browser remains open for next test case')
WebUI.comment('TEST PASSED: Withdraw amount entry successful - minimum amount Rp 50.000 accepted and advanced to PIN step')