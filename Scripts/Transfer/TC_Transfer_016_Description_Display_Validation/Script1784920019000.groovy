import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import org.openqa.selenium.Keys as Keys

WebUI.openBrowser(GlobalVariable.BASE_URL)

WebUI.maximizeWindow()

WebUI.setText(findTestObject('Auth/Login/input_email'), GlobalVariable.TEST_USER_EMAIL)

WebUI.setText(findTestObject('Auth/Login/input_password'), GlobalVariable.TEST_USER_PASSWORD)

WebUI.click(findTestObject('Auth/Login/btn_login'))

WebUI.delay(3)

WebUI.takeFullPageScreenshot('Screenshots/TRF016_01_HomePage.png')

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/transfer')

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/TRF016_02_TransferPage.png')

WebUI.setText(findTestObject('Transfer/input_account_number'), GlobalVariable.TEST_DEST_ACCOUNT_1)

WebUI.click(findTestObject('Transfer/btn_check_account'))

WebUI.delay(3)

WebUI.waitForElementPresent(findTestObject('Transfer/card_account_valid'), 10)

WebUI.click(findTestObject('Transfer/btn_continue_transfer'))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/TRF016_03_AmountPage.png')

WebUI.setText(findTestObject('Common/input_amount_numeric'), '50000')

String testDescription = 'Test Description ' + System.currentTimeMillis()

WebUI.setText(findTestObject('Transfer/input_transfer_note'), testDescription)

WebUI.takeFullPageScreenshot('Screenshots/TRF016_04_AmountAndDescriptionFilled.png')

WebUI.click(findTestObject('Transfer/btn_continue_transfer_amount'))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/TRF016_05_SummaryPage.png')

String summaryDescription = WebUI.getText(findTestObject('Page_Gopek  Digital Wallet/summary_description'))

assert summaryDescription.contains(testDescription)

WebUI.takeFullPageScreenshot('Screenshots/TRF016_06_DescriptionInSummary.png')

WebUI.click(findTestObject('Page_Gopek  Digital Wallet/button_continue_transfer_summary'))

WebUI.delay(2)

String pin = GlobalVariable.TEST_USER_PIN

String[] pinDigits = pin.split('')

for (String digit : pinDigits) {
    WebUI.click(findTestObject('Common/btn_pin_digit_' + digit))

    WebUI.delay(0.3)
}

WebUI.delay(3)

WebUI.takeFullPageScreenshot('Screenshots/TRF016_07_StatusPage.png')

WebUI.click(findTestObject('Transfer/btn_submit_transfer'))

String statusDescription = WebUI.getText(findTestObject('Page_Gopek  Digital Wallet/status_description'))

assert statusDescription.contains(testDescription)

WebUI.takeFullPageScreenshot('Screenshots/TRF016_08_DescriptionInStatus.png')

WebUI.closeBrowser()

