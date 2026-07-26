import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

WebUI.openBrowser(GlobalVariable.BASE_URL + '/register')

WebUI.maximizeWindow()

WebUI.waitForPageLoad(10)

WebUI.takeFullPageScreenshot('Screenshots/REG024_01_RegisterPageLoaded.png')

String baseEmail = ('CaseSensitive' + System.currentTimeMillis()) + '@Example.COM'

WebUI.setText(findTestObject('Auth/Register/input_fullname'), 'Test User CaseSensitive')

WebUI.setText(findTestObject('Auth/Register/input_email_register'), baseEmail)

String uniquePhone = '08' + System.currentTimeMillis().toString().substring(3, 12)

WebUI.setText(findTestObject('Auth/Register/input_phone'), uniquePhone)

WebUI.setText(findTestObject('Auth/Register/input_password_register'), 'Password123!')

WebUI.setText(findTestObject('Auth/Register/input_password_confirmation'), 'Password123!')

WebUI.takeFullPageScreenshot('Screenshots/REG024_02_MixedCaseEmail.png')

WebUI.click(findTestObject('Auth/Register/btn_continue'))

WebUI.delay(3)

WebUI.takeFullPageScreenshot('Screenshots/REG024_03_Step1.png')

String pin = '123456'

String[] pinDigits = pin.split('')

for (String digit : pinDigits) {
    WebUI.click(findTestObject('Transfer/btn_pin_digit_' + digit))

    WebUI.delay(0.3)
}

WebUI.click(findTestObject('Auth/Register/btn_continue'))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/REG024_04_Step2.png')

String[] pinDigitsConfirm = pin.split('')

for (String digit : pinDigitsConfirm) {
    WebUI.click(findTestObject('Transfer/btn_pin_digit_' + digit))

    WebUI.delay(0.3)
}

WebUI.delay(2)

WebUI.click(findTestObject('Auth/Register/btn_submit_register'))

WebUI.delay(3)

WebUI.takeFullPageScreenshot('Screenshots/REG024_05_AfterSubmit.png')

String currentUrl = WebUI.getUrl()

assert currentUrl.contains('/login') || currentUrl.contains('/status')

WebUI.takeFullPageScreenshot('Screenshots/REG024_06_RegistrationComplete.png')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/login')

WebUI.delay(2)

String lowercaseEmail = baseEmail.toLowerCase()

WebUI.setText(findTestObject('Auth/Login/input_email'), lowercaseEmail)

WebUI.setText(findTestObject('Auth/Login/input_password'), 'Password123!')

WebUI.takeFullPageScreenshot('Screenshots/REG024_07_LoginWithLowercase.png')

WebUI.click(findTestObject('Auth/Login/btn_login'))

WebUI.delay(3)

String toastText = WebUI.getText(findTestObject('Common/toast'))

assert toastText.contains('Invalid email or password')

WebUI.takeFullPageScreenshot('Screenshots/REG024_08_LoginSuccess.png')

WebUI.closeBrowser()

