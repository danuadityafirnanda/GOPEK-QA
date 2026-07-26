import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

WebUI.openBrowser(GlobalVariable.BASE_URL + '/login')

WebUI.maximizeWindow()

WebUI.waitForPageLoad(10)

WebUI.takeFullPageScreenshot('Screenshots/TRF017_01_LoginPage.png')

WebUI.setText(findTestObject('Auth/Login/input_email'), GlobalVariable.TEST_USER_EMAIL)

WebUI.setText(findTestObject('Auth/Login/input_password'), GlobalVariable.TEST_USER_PASSWORD)

WebUI.click(findTestObject('Auth/Login/btn_login'))

WebUI.delay(5)

WebUI.takeFullPageScreenshot('Screenshots/TRF017_02_Dashboard.png')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/transfer')

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/TRF017_03_TransferPage.png')

WebUI.setText(findTestObject('Transfer/input_account_number'), GlobalVariable.TEST_DEST_ACCOUNT_1)

WebUI.click(findTestObject('Transfer/btn_check_account'))

WebUI.delay(3)

WebUI.waitForElementPresent(findTestObject('Transfer/card_account_valid'), 10)

WebUI.takeFullPageScreenshot('Screenshots/TRF017_04_AccountValidated.png')

WebUI.click(findTestObject('Transfer/btn_continue_transfer'))

WebUI.takeFullPageScreenshot('Screenshots/TRF017_05_AmountPage.png')

WebUI.click(findTestObject('Common/btn_quick_amount_100000'))

WebUI.delay(1)

WebUI.takeFullPageScreenshot('Screenshots/TRF017_06_QuickAmountSelected.png')

WebUI.click(findTestObject('Transfer/btn_continue_transfer_amount'))

String amountValue = WebUI.getText(findTestObject('Page_Gopek  Digital Wallet/span_Rp 50.000'))

assert amountValue == 'Rp 100.000'

WebUI.takeFullPageScreenshot('Screenshots/TRF017_07_AmountValidated.png')

WebUI.closeBrowser()

