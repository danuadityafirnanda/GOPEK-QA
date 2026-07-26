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

WebUI.takeFullPageScreenshot('Screenshots/REG018_01_RegisterPageLoaded.png')

WebUI.setText(findTestObject('Auth/Register/input_fullname'), 'Test User ShortPass')

WebUI.setText(findTestObject('Auth/Register/input_email_register'), 'testuser_short@example.com')

WebUI.setText(findTestObject('Auth/Register/input_phone'), '081234567890')

WebUI.setText(findTestObject('Auth/Register/input_password_register'), 'Pass1!')

WebUI.setText(findTestObject('Auth/Register/input_password_confirmation'), 'Pass1!')

WebUI.takeFullPageScreenshot('Screenshots/REG018_02_ShortPasswordFilled.png')

WebUI.click(findTestObject('Auth/Register/btn_continue'))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/REG018_03_AfterSubmit.png')

WebUI.waitForElementPresent(findTestObject('Common/error_amount_field'), 10)

String errorText = WebUI.getText(findTestObject('Common/error_amount_field'))
assert errorText.toLowerCase().contains('8') || errorText.toLowerCase().contains('characters') : "Expected error about minimum 8 characters but got: ${errorText}"

String currentUrl = WebUI.getUrl()
assert currentUrl.contains('/register') : 'Should stay on register page (Step 0) for short password'

WebUI.takeFullPageScreenshot('Screenshots/REG018_04_StayOnRegisterPage.png')

WebUI.verifyElementClickable(findTestObject('Auth/Register/btn_continue'))

WebUI.closeBrowser()
