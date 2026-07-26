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

WebUI.openBrowser(GlobalVariable.BASE_URL + '/login')

WebUI.maximizeWindow()

WebUI.waitForPageLoad(10)

WebUI.verifyElementPresent(findTestObject('Auth/Login/form_login'), 10)

WebUI.takeFullPageScreenshot('Screenshots/LOG004_01_LoginPageLoaded.png')

WebUI.verifyElementPresent(findTestObject('Auth/Login/input_email'), 5)

WebUI.verifyElementPresent(findTestObject('Auth/Login/input_password'), 5)

WebUI.takeFullPageScreenshot('Screenshots/LOG004_02_EmptyFields.png')

WebUI.click(findTestObject('Auth/Login/btn_login'))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/LOG004_03_AfterSubmitEmptyFields.png')

WebUI.waitForElementPresent(findTestObject('Common/error_amount_field'), 10)

String errorText = WebUI.getText(findTestObject('Common/error_amount_field'))
assert errorText.contains('Email is required') || errorText.contains('email is required')

String currentUrl = WebUI.getUrl()
assert currentUrl.contains('/login') : "Should stay on login page when fields are empty"

WebUI.verifyElementClickable(findTestObject('Auth/Login/btn_login'))

WebUI.closeBrowser()
