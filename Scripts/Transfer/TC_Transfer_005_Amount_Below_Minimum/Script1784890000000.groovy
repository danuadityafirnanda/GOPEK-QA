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

WebUI.openBrowser('')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/login')

WebUI.maximizeWindow()

WebUI.waitForPageLoad(10)

WebUI.takeFullPageScreenshot('Screenshots/TRF005_01_LoginPage.png')

WebUI.setText(findTestObject('Auth/Login/input_email'), GlobalVariable.TEST_USER_EMAIL)

WebUI.setText(findTestObject('Auth/Login/input_password'), GlobalVariable.TEST_USER_PASSWORD)

WebUI.click(findTestObject('Auth/Login/btn_login'))

WebUI.delay(5)

WebUI.takeFullPageScreenshot('Screenshots/TRF005_02_Dashboard.png')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/transfer')

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/TRF005_03_TransferPage.png')

WebUI.setText(findTestObject('Transfer/input_account_number'), GlobalVariable.TEST_DEST_ACCOUNT_1)

WebUI.click(findTestObject('Transfer/btn_check_account'))

WebUI.delay(3)

WebUI.waitForElementPresent(findTestObject('Transfer/card_account_valid'), 10)

WebUI.takeFullPageScreenshot('Screenshots/TRF005_04_AccountValidated.png')

WebUI.click(findTestObject('Transfer/btn_continue_transfer'))

WebUI.takeFullPageScreenshot('Screenshots/TRF005_05_AmountPage.png')

WebUI.click(findTestObject('Common/btn_quick_amount_10000'))

WebUI.delay(1)

WebUI.takeFullPageScreenshot('Screenshots/TRF005_06_BelowMinimumAmount.png')

WebUI.click(findTestObject('Transfer/btn_continue_transfer_amount'))

WebUI.delay(2)

WebUI.waitForElementPresent(findTestObject('Common/error_amount_field'), 10)

WebUI.takeFullPageScreenshot('Screenshots/TRF005_07_ClientValidationError.png')

String errorText = WebUI.getText(findTestObject('Common/error_amount_field'))
assert errorText.contains('Minimum') || errorText.contains('minimum')

String currentUrl = WebUI.getUrl()
assert currentUrl.contains('/transfer')

WebUI.closeBrowser()
