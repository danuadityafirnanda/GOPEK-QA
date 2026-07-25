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

WebUI.takeFullPageScreenshot('Screenshots/TRF006_01_LoginPage.png')

WebUI.setText(findTestObject('Auth/Login/input_email'), GlobalVariable.TEST_USER_EMAIL)

WebUI.setText(findTestObject('Auth/Login/input_password'), GlobalVariable.TEST_USER_PASSWORD)

WebUI.click(findTestObject('Auth/Login/btn_login'))

WebUI.delay(5)

WebUI.takeFullPageScreenshot('Screenshots/TRF006_02_Dashboard.png')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/transfer')

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/TRF006_03_TransferPage.png')

WebUI.setText(findTestObject('Transfer/input_account_number'), GlobalVariable.TEST_DEST_ACCOUNT_LOCKED)

WebUI.click(findTestObject('Transfer/btn_check_account'))

WebUI.delay(3)

WebUI.takeFullPageScreenshot('Screenshots/TRF006_04_LockedAccountRedCard.png')

WebUI.waitForElementPresent(findTestObject('Transfer/card_account_invalid'), 10)

String cardText = WebUI.getText(findTestObject('Transfer/card_account_invalid'))
assert cardText.contains('locked') || cardText.contains('Account is locked')

String currentUrl = WebUI.getUrl()
assert currentUrl.contains('/transfer')

WebUI.closeBrowser()
