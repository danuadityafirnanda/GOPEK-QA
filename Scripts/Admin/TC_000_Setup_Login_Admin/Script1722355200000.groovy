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

// Open browser and navigate to login page
WebUI.openBrowser(GlobalVariable.BASE_URL + '/login')

WebUI.maximizeWindow()

WebUI.waitForPageLoad(10)

WebUI.takeFullPageScreenshot('Screenshots/Admin/SETUP_01_LoginPage.png')

// Enter admin credentials
WebUI.setText(findTestObject('Admin/Login/input_email'), GlobalVariable.ADMIN_EMAIL)

WebUI.setText(findTestObject('Admin/Login/input_password'), GlobalVariable.ADMIN_PASSWORD)

WebUI.takeFullPageScreenshot('Screenshots/Admin/SETUP_02_CredentialsFilled.png')

// Click login button
WebUI.click(findTestObject('Admin/Login/btn_login'))

WebUI.delay(3)

// Verify redirect to admin page
String currentUrl = WebUI.getUrl()

WebUI.verifyMatch(currentUrl, '.*admin.*', true, FailureHandling.STOP_ON_FAILURE)

WebUI.takeFullPageScreenshot('Screenshots/Admin/SETUP_03_AdminPageLoaded.png')

WebUI.comment('Admin login successful')
