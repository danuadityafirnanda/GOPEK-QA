import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

WebUI.openBrowser(GlobalVariable.BASE_URL + '/register')

WebUI.maximizeWindow()

WebUI.waitForPageLoad(10)

WebUI.takeFullPageScreenshot('Screenshots/REG026_01_RegisterPageLoaded.png')

WebUI.setText(findTestObject('Auth/Register/input_fullname'), 'Test User BackButton')

WebUI.setText(findTestObject('Auth/Register/input_email_register'), ('backbutton' + System.currentTimeMillis()) + '@example.com')

String uniquePhone = '08' + System.currentTimeMillis().toString().substring(3, 12)

WebUI.setText(findTestObject('Auth/Register/input_phone'), uniquePhone)

WebUI.setText(findTestObject('Auth/Register/input_password_register'), 'Password123!')

WebUI.setText(findTestObject('Auth/Register/input_password_confirmation'), 'Password123!')

WebUI.takeFullPageScreenshot('Screenshots/REG026_02_Step0Filled.png')

WebUI.click(findTestObject('Auth/Register/btn_continue'))

WebUI.delay(3)

WebUI.takeFullPageScreenshot('Screenshots/REG026_03_Step1PINPage.png')

String step1Url = WebUI.getUrl()

assert step1Url.contains('/register') : 'Should be on register page Step 1'

WebUI.click(findTestObject('Auth/Register/btn_back'))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/REG026_04_AfterBackButton.png')

String backUrl = WebUI.getUrl()

assert backUrl.contains('/register') : 'Should still be on register page after back button'

WebUI.verifyElementPresent(findTestObject('Auth/Register/input_fullname'), 10)

String nameValue = WebUI.getAttribute(findTestObject('Auth/Register/input_fullname'), 'value')

assert nameValue == 'Test User BackButton' : 'Form data should be preserved after back button'

WebUI.takeFullPageScreenshot('Screenshots/REG026_05_DataPreserved.png')

WebUI.click(findTestObject('Auth/Register/btn_continue'))

WebUI.delay(3)

WebUI.takeFullPageScreenshot('Screenshots/REG026_06_BackToStep1.png')

String pin = '123456'

String[] pinDigits = pin.split('')

for (String digit : pinDigits) {
    WebUI.click(findTestObject('Transfer/btn_pin_digit_' + digit))

    WebUI.delay(0.3)
}

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/REG026_07_PINEntered.png')

String returnUrl = WebUI.getUrl()

assert returnUrl.contains('/register') : 'Should be able to continue to Step 1 again after back'

WebUI.closeBrowser()

