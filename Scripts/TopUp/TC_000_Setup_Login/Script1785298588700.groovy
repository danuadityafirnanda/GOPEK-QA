import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

// ========================================
// SETUP TEST CASE - TopUp Test Suite
// ========================================
// This test case opens browser and performs login ONCE
// Subsequent test cases in the test suite will reuse the same browser session
// Browser will remain open until test suite completes

WebUI.comment('TC_000_Setup_Login (TopUp) - START')

// ========================================
// STEP 1: Open browser and navigate to login page
// ========================================
WebUI.comment('Step 1: Open browser and navigate to login page')

WebUI.openBrowser('')
WebUI.maximizeWindow()
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/login')
WebUI.waitForPageLoad(10)
WebUI.delay(2) // Let React initialize

WebUI.comment('✅ Browser opened and navigated to login page')

// ========================================
// STEP 2: Perform login with test credentials
// ========================================
WebUI.comment('Step 2: Login with test user credentials')

// Wait for login form elements
WebUI.waitForElementPresent(findTestObject('Auth/Login/input_email'), 15, FailureHandling.STOP_ON_FAILURE)
WebUI.waitForElementClickable(findTestObject('Auth/Login/input_email'), 10)

// Enter email
WebUI.delay(0.3)
WebUI.click(findTestObject('Auth/Login/input_email'))
WebUI.delay(0.3)
WebUI.setText(findTestObject('Auth/Login/input_email'), GlobalVariable.TEST_USER_EMAIL)
WebUI.delay(0.5)

// Enter password
WebUI.click(findTestObject('Auth/Login/input_password'))
WebUI.delay(0.3)
WebUI.setText(findTestObject('Auth/Login/input_password'), GlobalVariable.TEST_USER_PASSWORD)
WebUI.delay(0.5)

// Submit login
WebUI.click(findTestObject('Auth/Login/btn_login'))
WebUI.delay(3) // Wait for login processing

WebUI.comment('✅ Login form submitted')

// ========================================
// STEP 3: Verify successful login and redirect to home
// ========================================
WebUI.comment('Step 3: Verify successful login')

WebUI.waitForPageLoad(10)
String currentUrl = WebUI.getUrl()

// Verify URL contains /home
assert currentUrl.contains('/home'), "Failed to redirect to home page after login. Current URL: ${currentUrl}"

WebUI.comment("✅ Successfully logged in and redirected to home: ${currentUrl}")

// Take screenshot for verification
WebUI.takeFullPageScreenshot('Screenshots/TopUp_Setup_Login_Complete.png')

// ========================================
// SETUP COMPLETE
// ========================================
WebUI.comment('========================================')
WebUI.comment('✅ TC_000_Setup_Login (TopUp) - COMPLETED')
WebUI.comment('✅ Browser is open and user is logged in')
WebUI.comment('✅ Ready for TopUp test cases')
WebUI.comment('⚠️  DO NOT CLOSE BROWSER - will be reused by next test cases')
WebUI.comment('========================================')

// NOTE: Browser remains open for next test cases in the suite