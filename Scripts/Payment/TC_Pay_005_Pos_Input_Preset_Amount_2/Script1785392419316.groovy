import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.WebElement as WebElement
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import org.openqa.selenium.Keys as Keys

WebUI.openBrowser(null)

WebUI.navigateToUrl('https://gopek.live/')

WebUI.setText(findTestObject('Payment/Page_Gopek  Digital Wallet/input_Enter your email'), 'danu@test.com')

WebUI.setEncryptedText(findTestObject('Payment/Page_Gopek  Digital Wallet/input_Enter your password'), 'RigbBhfdqOBGNlJIWM1ClA==')

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_Log in'))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/svg_lucide lucide-qr-code size-8'))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_Rp 50.000'))

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

// ============================================================================
// HELPER FUNCTIONS
// ============================================================================
TestObject createDynamicObject(String xpath) {
    TestObject to = new TestObject(xpath)

    to.addProperty('xpath', ConditionType.EQUALS, xpath)

    return to
}

