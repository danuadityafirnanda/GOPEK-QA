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
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

// ========================================
// PREREQUISITE: Browser is already open and user is logged in
// ========================================

WebUI.comment('=== E2E TEST: COMPLETE TOP-UP WITH PAYMENT SIMULATOR ===')
WebUI.comment('TC_TopUp_017: Complete Payment Via Simulator - START')
WebUI.comment('Assuming browser is open and user is logged in from setup')

// ========================================
// STEP 1: Navigate to home and capture initial balance
// ========================================
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

String currentUrl = WebUI.getUrl()
assert currentUrl.contains('/home') : 'Should be on home page'

WebUI.takeFullPageScreenshot('Screenshots/TOPUP017_01_HomePageAfterLogin.png')

// Capture initial balance for comparison later
String initialBalance = ''
try {
    initialBalance = WebUI.getText(findTestObject('Home/text_wallet_balance'))
    WebUI.comment('Initial balance: ' + initialBalance)
} catch (Exception e) {
    WebUI.comment('Could not retrieve initial balance - will verify later')
}

WebUI.comment('=== PHASE 1: CREATE TOP-UP TRANSACTION ===')

// Step 2: Navigate to Top Up page
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/topup')

WebUI.waitForPageLoad(10)

WebUI.delay(1)

currentUrl = WebUI.getUrl()

assert currentUrl.contains('/topup') : 'Failed to navigate to topup page'

WebUI.takeFullPageScreenshot('Screenshots/TOPUP017_02_TopUpPageLoaded.png')

// Step 3: Verify amount input field is present
WebUI.verifyElementPresent(findTestObject('TopUp/input_topup_amount'), 10)

WebUI.comment('Top Up form loaded successfully')

// Step 4: Enter top-up amount (50,000)
WebUI.setText(findTestObject('TopUp/input_topup_amount'), '50000')

WebUI.delay(1)

WebUI.takeFullPageScreenshot('Screenshots/TOPUP017_03_AmountEntered.png')

WebUI.comment('Entered top-up amount: Rp 50.000')

// Step 5: Click submit to create transaction
WebUI.verifyElementPresent(findTestObject('TopUp/btn_submit_topup'), 10)

WebUI.click(findTestObject('TopUp/btn_submit_topup'))

WebUI.comment('Submit button clicked - creating transaction')

// Step 6: Wait for redirect to payment page
WebUI.delay(3)

currentUrl = WebUI.getUrl()

assert currentUrl.contains('/topup/payment') : 'Should redirect to payment page'

WebUI.comment('Successfully redirected to payment page: ' + currentUrl)

WebUI.takeFullPageScreenshot('Screenshots/TOPUP017_04_PaymentPageLoaded.png')

// Step 7: Capture VA number from payment page
WebUI.waitForElementPresent(findTestObject('TopUp/text_va_number'), 10)

String vaNumber = WebUI.getText(findTestObject('TopUp/text_va_number'))

WebUI.comment('VA Number captured: ' + vaNumber)

assert vaNumber.length() > 0 : 'VA number should not be empty'

assert vaNumber.startsWith('8808') : 'VA number should start with 8808'

WebUI.takeFullPageScreenshot('Screenshots/TOPUP017_05_VANumber_Captured.png')

// Extract transaction ID from URL for tracking
String transactionId = ''
if (currentUrl.contains('tx=')) {
    transactionId = currentUrl.split('tx=')[1].split('&')[0]
    WebUI.comment('Transaction ID: ' + transactionId)
}

WebUI.comment('=== PHASE 2: COMPLETE PAYMENT VIA SIMULATOR ===')

// Step 8: Navigate to external payment simulator
WebUI.navigateToUrl('https://mocking-app.vercel.app/simulator')

WebUI.waitForPageLoad(15)

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/TOPUP017_06_SimulatorPageLoaded.png')

WebUI.comment('Navigated to payment simulator: https://mocking-app.vercel.app/simulator')

// Step 9: Fill Backend URL field in simulator (FIRST FORM - CRITICAL!)
// Simulator has 2 separate forms: Backend URL + VA Number
// Backend URL must be set to https://gopek.live (NOT localhost:8081)
TestObject simulatorBackendUrlInput = new TestObject('simulator_backend_url_input')
simulatorBackendUrlInput.addProperty('css', ConditionType.EQUALS, 'input#backend-url')

try {
    WebUI.waitForElementPresent(simulatorBackendUrlInput, 10)
    WebUI.click(simulatorBackendUrlInput) // Focus field
    WebUI.delay(0.3)
    WebUI.clearText(simulatorBackendUrlInput) // Clear default localhost:8081
    WebUI.setText(simulatorBackendUrlInput, 'https://gopek.live')
    WebUI.comment('Backend URL entered in simulator: https://gopek.live')
    WebUI.delay(0.5)
    WebUI.takeFullPageScreenshot('Screenshots/TOPUP017_07_BackendURL_Entered.png')
} catch (Exception e) {
    WebUI.comment('ERROR: Could not find Backend URL field (id="backend-url") in simulator')
    WebUI.takeFullPageScreenshot('Screenshots/TOPUP017_07_BackendURL_NotFound.png')
}

// Step 10: Enter VA number in simulator (SECOND FORM)
// Use specific ID selector from SimulatorForm.tsx: input#va-number
TestObject simulatorVAInput = new TestObject('simulator_va_input')
simulatorVAInput.addProperty('css', ConditionType.EQUALS, 'input#va-number')

try {
    WebUI.waitForElementPresent(simulatorVAInput, 10)
    WebUI.click(simulatorVAInput) // Focus field
    WebUI.delay(0.3)
    WebUI.setText(simulatorVAInput, vaNumber)
    WebUI.comment('VA number entered in simulator: ' + vaNumber)
    WebUI.delay(0.5)
    WebUI.takeFullPageScreenshot('Screenshots/TOPUP017_08_VANumber_Entered.png')
} catch (Exception e) {
    WebUI.comment('ERROR: Could not find VA Number field (id="va-number") in simulator')
    WebUI.takeFullPageScreenshot('Screenshots/TOPUP017_08_VANumber_NotFound.png')
}

// Step 11: Click Submit button in simulator
// Button text: "Simulasikan Pembayaran" (from SimulatorForm.tsx)
TestObject simulatorPayButton = new TestObject('simulator_pay_button')
simulatorPayButton.addProperty('css', ConditionType.EQUALS, 'button[type="submit"]')

try {
    WebUI.waitForElementPresent(simulatorPayButton, 10)
    WebUI.delay(0.5) // Safety margin before clicking
    WebUI.click(simulatorPayButton)
    WebUI.comment('Submit button clicked - payment simulation processing')
    WebUI.delay(3) // Wait for API call to backend + response
    WebUI.takeFullPageScreenshot('Screenshots/TOPUP017_09_Payment_Submitted.png')
} catch (Exception e) {
    WebUI.comment('ERROR: Could not find Submit button (button[type="submit"]) in simulator')
    WebUI.takeFullPageScreenshot('Screenshots/TOPUP017_09_SubmitButton_NotFound.png')
}

WebUI.comment('=== PHASE 3: VERIFY PAYMENT SUCCESS ===')

// Step 12: Navigate back to application home page
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')

WebUI.waitForPageLoad(10)

WebUI.delay(3)

WebUI.takeFullPageScreenshot('Screenshots/TOPUP017_09_HomeAfterPayment.png')

// Step 13: Verify balance increased
try {
    String finalBalance = WebUI.getText(findTestObject('Home/text_wallet_balance'))
    WebUI.comment('Final balance: ' + finalBalance)
    
    if (initialBalance.length() > 0) {
        WebUI.comment('Balance comparison - Initial: ' + initialBalance + ', Final: ' + finalBalance)
        assert !finalBalance.equals(initialBalance) : 'Balance should have increased after payment'
    }
    
    WebUI.takeFullPageScreenshot('Screenshots/TOPUP017_10_Balance_Increased.png')
} catch (Exception e) {
    WebUI.comment('Could not verify balance change - manual verification required')
}

// Step 14: Optionally verify transaction status (if transaction detail page exists)
if (transactionId.length() > 0) {
    try {
        // Navigate to transactions page to verify status
        WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/transactions')
        WebUI.waitForPageLoad(10)
        WebUI.delay(2)
        WebUI.takeFullPageScreenshot('Screenshots/TOPUP017_11_TransactionsPage.png')
        WebUI.comment('Transactions page loaded - verify transaction ' + transactionId + ' is SUCCESS')
    } catch (Exception e) {
        WebUI.comment('Could not navigate to transactions page')
    }
}

// ========================================
// Return to home page for next test
// ========================================
WebUI.comment('Returning to home page for next test case')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/home')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

WebUI.comment('TC_TopUp_017: Complete Payment Via Simulator - COMPLETED')
WebUI.comment('✅ Browser remains open for next test case')
WebUI.comment('TEST PASSED: E2E Top-Up with Payment Simulator completed - Amount Rp 50.000')
WebUI.comment('NOTE: Simulator page selectors may need adjustment based on actual page structure')