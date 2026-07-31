import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import internal.GlobalVariable as GlobalVariable

// ========================================
// SETUP TEST CASE - History Test Suite
// ========================================
// Test case ini membuka browser, melakukan login ONCE, 
// lalu navigasi ke halaman History via tombol "See All".
// Browser akan tetap terbuka untuk di-reuse oleh test case History berikutnya.

WebUI.comment('TC_000_Setup_Login (History) - START')

// ========================================
// STEP 1: Buka browser & navigasi ke halaman login
// ========================================
WebUI.comment('Step 1: Open browser and navigate to login page')

WebUI.openBrowser('')
WebUI.maximizeWindow()
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/login')
WebUI.waitForPageLoad(10)
WebUI.delay(2) // Memberi waktu React untuk inisialisasi

WebUI.comment('✅ Browser opened and navigated to login page')

// ========================================
// STEP 2: Input kredensial & submit login
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
// STEP 3: Verifikasi login & redirect ke Home
// ========================================
WebUI.comment('Step 3: Verify successful login')

WebUI.waitForPageLoad(10)
String currentUrl = WebUI.getUrl()

// Verify URL contains /home
assert currentUrl.contains('/home'), "Failed to redirect to home page after login. Current URL: ${currentUrl}"

WebUI.comment("✅ Successfully logged in and redirected to home: ${currentUrl}")

// ========================================
// STEP 4: Navigasi ke Halaman History via "See All"
// ========================================
WebUI.comment('Step 4: Navigate to History page via "See All" button')

WebUI.waitForElementPresent(findTestObject('History/Page_Gopek  Digital Wallet/a_See All'), 10, FailureHandling.STOP_ON_FAILURE)
WebUI.click(findTestObject('History/Page_Gopek  Digital Wallet/a_See All'))
WebUI.delay(1.5)

WebUI.comment('✅ Navigated to History page via "See All"')

// Take screenshot for verification
WebUI.takeFullPageScreenshot('Screenshots/History_Setup_Login_Complete.png')

// ========================================
// SETUP COMPLETE
// ========================================
WebUI.comment('========================================')
WebUI.comment('✅ TC_000_Setup_Login (History) - COMPLETED')
WebUI.comment('✅ Browser is open, user logged in, and on History page')
WebUI.comment('✅ Ready for History test cases')
WebUI.comment('⚠️  DO NOT CLOSE BROWSER - will be reused by next test cases')
WebUI.comment('========================================')

// NOTE: Browser remains open for next test cases in the suite