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

WebUI.verifyElementPresent(findTestObject('Auth/Login/form_login'), 10)

WebUI.takeFullPageScreenshot('Screenshots/LOG003_01_LoginPageLoaded.png')

WebUI.setText(findTestObject('Auth/Login/input_email'), 'nonexistent@example.com')

WebUI.setText(findTestObject('Auth/Login/input_password'), 'SomePassword123!')

WebUI.takeFullPageScreenshot('Screenshots/LOG003_02_InvalidCredentialsFilled.png')

WebUI.click(findTestObject('Auth/Login/btn_login'))

WebUI.waitForElementPresent(findTestObject('Common/toast'), 10)

WebUI.takeFullPageScreenshot('Screenshots/LOG003_03_ErrorToast.png')

String toastText = WebUI.getText(findTestObject('Common/toast'))
assert toastText.contains('Invalid email or password') : "Expected 'Invalid email or password' error but got: ${toastText}"

String currentUrl = WebUI.getUrl()
assert currentUrl.contains('/login') : "Should stay on login page but got: ${currentUrl}"

WebUI.takeFullPageScreenshot('Screenshots/LOG003_04_StayOnLoginPage.png')

WebUI.verifyElementClickable(findTestObject('Auth/Login/btn_login'))

WebUI.closeBrowser()
