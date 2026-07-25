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

WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/register')

WebUI.maximizeWindow()

WebUI.waitForPageLoad(10)

WebUI.verifyElementPresent(findTestObject('Auth/Register/form_register'), 10)

WebUI.takeFullPageScreenshot('Screenshots/REG002_01_RegisterPageLoaded.png')

WebUI.setText(findTestObject('Auth/Register/input_fullname'), 'Existing User')

WebUI.setText(findTestObject('Auth/Register/input_email_register'), GlobalVariable.TEST_USER_EMAIL)

WebUI.setText(findTestObject('Auth/Register/input_phone'), '08987654321')

WebUI.setText(findTestObject('Auth/Register/input_password_register'), 'Password123!')

WebUI.setText(findTestObject('Auth/Register/input_password_confirmation'), 'Password123!')

WebUI.takeFullPageScreenshot('Screenshots/REG002_02_DuplicateEmailFilled.png')

WebUI.click(findTestObject('Auth/Register/btn_continue'))

WebUI.delay(3)

WebUI.waitForElementPresent(findTestObject('Common/toast'), 10)

WebUI.takeFullPageScreenshot('Screenshots/REG002_03_ErrorToast.png')

String toastText = WebUI.getText(findTestObject('Common/toast'))

assert toastText.contains('Email is already registered')

String currentUrl = WebUI.getUrl()

assert currentUrl.contains('/register') : 'Should stay on register page (Step 0) for duplicate email'

WebUI.takeFullPageScreenshot('Screenshots/REG002_04_StayOnRegisterPage.png')

WebUI.verifyElementClickable(findTestObject('Auth/Register/btn_continue'))

WebUI.closeBrowser()

