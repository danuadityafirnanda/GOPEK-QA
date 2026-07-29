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

// Step 1: Open browser and navigate to login page
WebUI.openBrowser(GlobalVariable.BASE_URL + '/login')

WebUI.maximizeWindow()

WebUI.waitForPageLoad(10)

// Step 2: Login with valid credentials
WebUI.setText(findTestObject('Auth/Login/input_email'), GlobalVariable.TEST_USER_EMAIL)

WebUI.setText(findTestObject('Auth/Login/input_password'), GlobalVariable.TEST_USER_PASSWORD)

WebUI.click(findTestObject('Auth/Login/btn_login'))

WebUI.delay(2)

// Step 3: Verify redirect to home page
String currentUrl = WebUI.getUrl()

assert currentUrl.contains('/home') : 'Failed to redirect to home page'

WebUI.takeFullPageScreenshot('Screenshots/WALLET001_01_HomePage_Loaded.png')

// Step 4: Wait for wallet information to load
WebUI.waitForElementPresent(findTestObject('Home/text_wallet_balance'), 10)

// Step 5: Verify user name is displayed
WebUI.verifyElementPresent(findTestObject('Home/text_user_name'), 10)

String userName = WebUI.getText(findTestObject('Home/text_user_name'))

WebUI.comment("User Name displayed: " + userName)

assert userName.length() > 0 : 'User name should not be empty'

WebUI.takeFullPageScreenshot('Screenshots/WALLET001_02_UserName_Visible.png')

// Step 6: Verify account number is displayed
WebUI.verifyElementPresent(findTestObject('Home/badge_home_account_number'), 10)

String accountNumber = WebUI.getText(findTestObject('Home/badge_home_account_number'))

WebUI.comment("Account Number displayed: " + accountNumber)

assert accountNumber.contains(GlobalVariable.TEST_USER_ACCOUNT) : 'Account number should match expected value'

WebUI.takeFullPageScreenshot('Screenshots/WALLET001_03_AccountNumber_Visible.png')

// Step 7: Verify balance is displayed in Rupiah format
WebUI.verifyElementPresent(findTestObject('Home/text_wallet_balance'), 10)

String balance = WebUI.getText(findTestObject('Home/text_wallet_balance'))

WebUI.comment("Current Balance displayed: " + balance)

assert balance.contains('Rp') : 'Balance should contain Rupiah symbol (Rp)'

assert balance.length() > 3 : 'Balance should not be empty'

WebUI.takeFullPageScreenshot('Screenshots/WALLET001_04_Balance_Visible.png')

// Step 8: Verify no error message is shown
WebUI.verifyElementNotPresent(findTestObject('Common/toast'), 2, FailureHandling.OPTIONAL)

WebUI.takeFullPageScreenshot('Screenshots/WALLET001_05_Final_Verification.png')

// Step 9: Close browser
WebUI.closeBrowser()

WebUI.comment('TEST PASSED: Wallet information displayed successfully')
