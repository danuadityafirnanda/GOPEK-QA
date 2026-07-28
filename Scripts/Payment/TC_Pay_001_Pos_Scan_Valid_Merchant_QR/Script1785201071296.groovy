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

WebUI.openBrowser(null)

WebUI.navigateToUrl('https://gopek.live/')

WebUI.setText(findTestObject('Payment/Page_Gopek  Digital Wallet/input_Enter your email'), 'danu@test.com')

WebUI.setEncryptedText(findTestObject('Payment/Page_Gopek  Digital Wallet/input_Enter your password'), 'RigbBhfdqOBGNlJIWM1ClA==')

WebUI.sendKeys(findTestObject('Payment/Page_Gopek  Digital Wallet/input_Enter your password'), Keys.chord(Keys.ENTER))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/svg_lucide lucide-qr-code size-8'))

WebUI.setText(findTestObject('Payment/Page_Gopek  Digital Wallet/input_0'), '10.00')

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_Continue'))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_Continue_1'))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_1'))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_2'))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_3'))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_4'))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_5'))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_6'))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_Confirm  Pay'))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_Back to Home'))

