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

WebUI.takeFullPageScreenshot('Screenshots/REG001_01_RegisterPageLoaded.png')

WebUI.setText(findTestObject('Auth/Register/input_fullname'), 'Test User Complete')

String uniqueEmail = ('newuser' + System.currentTimeMillis()) + '@example.com'

WebUI.setText(findTestObject('Auth/Register/input_email_register'), uniqueEmail)

String uniquePhone = '08' + System.currentTimeMillis().toString().substring(3, 12)

WebUI.setText(findTestObject('Auth/Register/input_phone'), uniquePhone)

WebUI.setText(findTestObject('Auth/Register/input_password_register'), 'Password123!')

WebUI.setText(findTestObject('Auth/Register/input_password_confirmation'), 'Password123!')

WebUI.takeFullPageScreenshot('Screenshots/REG001_02_Step0_AccountInfoFilled.png')

WebUI.click(findTestObject('Auth/Register/btn_continue'))

WebUI.delay(3)

WebUI.takeFullPageScreenshot('Screenshots/REG001_03_Step1_PINPage.png')

String currentUrl = WebUI.getUrl()

assert currentUrl.contains('/register') : 'Should proceed to PIN step'

String pin = '123456'

String[] pinDigits = pin.split('')

for (String digit : pinDigits) {
    WebUI.click(findTestObject('Common/btn_pin_digit_' + digit))

    WebUI.delay(0.3)
}

WebUI.takeFullPageScreenshot('Screenshots/REG001_04_Step1_PINEntered.png')

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/REG001_05_Step2_PINConfirmPage.png')

WebUI.click(findTestObject('Auth/Register/btn_continue'))

for (String digit : pinDigits) {
    WebUI.click(findTestObject('Common/btn_pin_digit_' + digit))

    WebUI.delay(0.3)
}

WebUI.takeFullPageScreenshot('Screenshots/REG001_06_Step2_PINConfirmEntered.png')

WebUI.click(findTestObject('Auth/Register/btn_submit_register'))

WebUI.delay(8)

currentUrl = WebUI.getUrl()

assert currentUrl.contains('/login')

WebUI.takeFullPageScreenshot('Screenshots/REG001_07_RedirectToLogin.png')

WebUI.verifyElementPresent(findTestObject('Auth/Login/form_login'), 10)

WebUI.closeBrowser()

