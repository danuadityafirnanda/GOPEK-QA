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

WebUI.takeFullPageScreenshot('Screenshots/REG003_01_RegisterPageLoaded.png')

WebUI.setText(findTestObject('Auth/Register/input_fullname'), 'Test PIN Mismatch')

WebUI.setText(findTestObject('Auth/Register/input_email_register'), ('pinmismatch' + System.currentTimeMillis()) + '@example.com')

WebUI.setText(findTestObject('Auth/Register/input_phone'), '08111823353')

WebUI.setText(findTestObject('Auth/Register/input_password_register'), 'Password123!')

WebUI.setText(findTestObject('Auth/Register/input_password_confirmation'), 'Password123!')

WebUI.takeFullPageScreenshot('Screenshots/REG003_02_Step0_AccountInfoFilled.png')

WebUI.click(findTestObject('Auth/Register/btn_continue'))

WebUI.delay(3)

WebUI.takeFullPageScreenshot('Screenshots/REG003_03_Step1_PINPage.png')

String currentUrl = WebUI.getUrl()

assert currentUrl.contains('/register') : 'Should proceed to PIN step (Step 1)'

String pin = '123456'

String[] pinDigits = pin.split('')

for (String digit : pinDigits) {
    WebUI.click(findTestObject('Common/btn_pin_digit_' + digit))

    WebUI.delay(0.3)
}

WebUI.takeFullPageScreenshot('Screenshots/REG003_04_Step1_PINEntered.png')

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/REG003_05_Step2_PINConfirmPage.png')

WebUI.click(findTestObject('Auth/Register/btn_continue'))

String wrongPin = '654321'

String[] wrongPinDigits = wrongPin.split('')

for (String digit : wrongPinDigits) {
    WebUI.click(findTestObject('Common/btn_pin_digit_' + digit))

    WebUI.delay(0.3)
}

WebUI.takeFullPageScreenshot('Screenshots/REG003_06_Step2_WrongPINEntered.png')

WebUI.click(findTestObject('Auth/Register/btn_submit_register'))

WebUI.delay(2)

WebUI.waitForElementPresent(findTestObject('Common/error_pin'), 10)

WebUI.takeFullPageScreenshot('Screenshots/REG003_07_PINErrorDisplayed.png')

String errorText = WebUI.getText(findTestObject('Common/error_pin'))

assert errorText.contains('PINs do not match')

currentUrl = WebUI.getUrl()

assert currentUrl.contains('/register') : 'Should stay on register page (Step 2) when PINs mismatch (client-side validation)'

WebUI.takeFullPageScreenshot('Screenshots/REG003_08_StayOnRegisterStep2.png')

WebUI.verifyElementPresent(findTestObject('Auth/Register/btn_submit_register'), 5)

WebUI.closeBrowser()

