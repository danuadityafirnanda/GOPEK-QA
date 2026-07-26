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

WebUI.openBrowser(GlobalVariable.BASE_URL + '/login')

WebUI.maximizeWindow()

WebUI.waitForPageLoad(10)

WebUI.takeFullPageScreenshot('Screenshots/TRF015_01_LoginPage.png')

WebUI.setText(findTestObject('Auth/Login/input_email'), GlobalVariable.TEST_USER_EMAIL)

WebUI.setText(findTestObject('Auth/Login/input_password'), GlobalVariable.TEST_USER_PASSWORD)

WebUI.click(findTestObject('Auth/Login/btn_login'))

WebUI.delay(5)

WebUI.takeFullPageScreenshot('Screenshots/TRF015_02_Dashboard.png')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/transfer')

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/TRF015_03_TransferPage.png')

WebUI.setText(findTestObject('Transfer/input_account_number'), GlobalVariable.TEST_DEST_ACCOUNT_1)

WebUI.click(findTestObject('Transfer/btn_check_account'))

WebUI.delay(3)

WebUI.waitForElementPresent(findTestObject('Transfer/card_account_valid'), 10)

WebUI.takeFullPageScreenshot('Screenshots/TRF015_04_AccountValidated.png')

WebUI.click(findTestObject('Transfer/btn_continue_transfer'))

WebUI.takeFullPageScreenshot('Screenshots/TRF015_05_AmountPage.png')

WebUI.setText(findTestObject('Common/input_amount_numeric'), '50000')

WebUI.delay(1)

WebUI.click(findTestObject('Transfer/btn_continue_transfer_amount'))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/TRF015_06_SummaryPage.png')

WebUI.click(findTestObject('Transfer/btn_continue_to_pin'))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/TRF015_07_PINPage.png')

WebUI.click(findTestObject('Transfer/btn_pin_digit_1'))
WebUI.click(findTestObject('Transfer/btn_pin_digit_2'))
WebUI.click(findTestObject('Transfer/btn_pin_digit_3'))
WebUI.click(findTestObject('Transfer/btn_pin_digit_4'))
WebUI.click(findTestObject('Transfer/btn_pin_digit_5'))
WebUI.click(findTestObject('Transfer/btn_pin_digit_6'))

WebUI.delay(1)

WebUI.takeFullPageScreenshot('Screenshots/TRF015_08_SixDigitsEntered.png')

WebUI.click(findTestObject('Transfer/btn_pin_digit_7'))

WebUI.delay(1)

WebUI.takeFullPageScreenshot('Screenshots/TRF015_09_AttemptSeventhDigit.png')

String pinValue = WebUI.getAttribute(findTestObject('Transfer/input_pin_transfer'), 'value')
int pinLength = pinValue.length()
assert pinLength == 6 : "PIN should remain 6 digits, but got ${pinLength}"

WebUI.closeBrowser()
