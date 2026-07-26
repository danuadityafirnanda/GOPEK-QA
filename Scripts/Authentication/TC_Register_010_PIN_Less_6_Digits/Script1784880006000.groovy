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

WebUI.openBrowser(GlobalVariable.BASE_URL + '/register')

WebUI.maximizeWindow()

WebUI.waitForPageLoad(10)

WebUI.verifyElementPresent(findTestObject('Auth/Register/form_register'), 10)

WebUI.takeFullPageScreenshot('Screenshots/REG010_01_RegisterPageLoaded.png')

WebUI.setText(findTestObject('Auth/Register/input_fullname'), 'Test PIN Short')

WebUI.setText(findTestObject('Auth/Register/input_email_register'), ('pinshort' + System.currentTimeMillis()) + '@example.com')

WebUI.setText(findTestObject('Auth/Register/input_phone'), '08' + System.currentTimeMillis().toString().substring(3, 12))

WebUI.setText(findTestObject('Auth/Register/input_password_register'), 'Password123!')

WebUI.setText(findTestObject('Auth/Register/input_password_confirmation'), 'Password123!')

WebUI.takeFullPageScreenshot('Screenshots/REG010_02_Step0_AccountInfoFilled.png')

WebUI.click(findTestObject('Auth/Register/btn_continue'))

WebUI.takeFullPageScreenshot('Screenshots/REG010_03_Step1_PINPage.png')

WebUI.delay(3)

String pin = '1234'

String[] pinDigits = pin.split('')

for (String digit : pinDigits) {
    WebUI.click(findTestObject('Common/btn_pin_digit_' + digit))

    WebUI.delay(0.3)
}

WebUI.click(findTestObject('Auth/Register/btn_continue'))

WebUI.takeFullPageScreenshot('Screenshots/REG010_04_Step1_ShortPINEntered.png')

String errorText = WebUI.getText(findTestObject('Common/error_pin'))

assert errorText.contains('6 digits')

WebUI.takeFullPageScreenshot('Screenshots/REG010_05_ErrorShown.png')

String currentUrl = WebUI.getUrl()

assert currentUrl.contains('/register') : 'Should stay on register page for short PIN'

WebUI.closeBrowser()

