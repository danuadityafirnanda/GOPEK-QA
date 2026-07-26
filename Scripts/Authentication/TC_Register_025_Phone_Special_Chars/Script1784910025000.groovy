import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

WebUI.openBrowser(GlobalVariable.BASE_URL + '/register')
WebUI.maximizeWindow()
WebUI.waitForPageLoad(10)
WebUI.takeFullPageScreenshot('Screenshots/REG025_01_RegisterPageLoaded.png')

WebUI.setText(findTestObject('Auth/Register/input_fullname'), 'Test User PhoneSpecialChars')
WebUI.setText(findTestObject('Auth/Register/input_email_register'), 'phonespecial' + System.currentTimeMillis() + '@example.com')
WebUI.setText(findTestObject('Auth/Register/input_phone'), '+62 812-3456-7890')
WebUI.setText(findTestObject('Auth/Register/input_password_register'), 'Password123!')
WebUI.setText(findTestObject('Auth/Register/input_password_confirmation'), 'Password123!')
WebUI.takeFullPageScreenshot('Screenshots/REG025_02_PhoneWithSpecialChars.png')

WebUI.click(findTestObject('Auth/Register/btn_continue'))
WebUI.delay(2)
WebUI.takeFullPageScreenshot('Screenshots/REG025_03_AfterSubmit.png')

WebUI.waitForElementPresent(findTestObject('Common/error_amount_field'), 10)
String errorText = WebUI.getText(findTestObject('Common/error_amount_field'))
assert errorText.toLowerCase().contains('phone') || errorText.toLowerCase().contains('invalid') || errorText.toLowerCase().contains('08') : "Expected error about invalid phone format but got: ${errorText}"

WebUI.takeFullPageScreenshot('Screenshots/REG025_04_ErrorShown.png')

String currentUrl = WebUI.getUrl()
assert currentUrl.contains('/register') : 'Should stay on register page for phone with special chars'

WebUI.verifyElementClickable(findTestObject('Auth/Register/btn_continue'))

WebUI.closeBrowser()
