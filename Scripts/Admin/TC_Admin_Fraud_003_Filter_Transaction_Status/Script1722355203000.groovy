import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

// Call setup to login as admin
WebUI.callTestCase(findTestCase('Admin/TC_000_Setup_Login_Admin'), [:], FailureHandling.STOP_ON_FAILURE)

// Navigate to Fraud Report page
WebUI.click(findTestObject('Admin/Navigation/link_fraud_report'))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/Admin/Fraud/FRD003_01_FraudPageLoaded.png')

// Apply filter: Transaction Status = SUCCESS (using keyboard navigation - reliable for Radix UI)
WebUI.comment('Opening Transaction Status dropdown...')

WebUI.click(findTestObject('Admin/FlaggedTransactions/Filters/select_filter_transaction_status'))

WebUI.delay(1)

WebUI.comment('Typing "succ" to select SUCCESS...')

WebUI.sendKeys(findTestObject('Admin/FlaggedTransactions/Filters/select_filter_transaction_status'), 'succ')

WebUI.delay(0.5)

WebUI.comment('Pressing Enter to confirm selection...')

WebUI.sendKeys(findTestObject('Admin/FlaggedTransactions/Filters/select_filter_transaction_status'), Keys.chord(Keys.ENTER))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/Admin/Fraud/FRD003_02_FilterApplied.png')

// Verify table is present
WebUI.verifyElementPresent(findTestObject('Admin/FlaggedTransactions/Table/table_flagged_transactions'), 10)

// Verify status badge (if results exist)
boolean hasResults = WebUI.verifyElementPresent(findTestObject('Admin/FlaggedTransactions/Table/row_first_transaction'),
    5, FailureHandling.OPTIONAL)

if (hasResults) {
    WebUI.verifyElementPresent(findTestObject('Admin/FlaggedTransactions/Table/cell_status'), 5)
    WebUI.comment('Filter by Transaction Status verified')
} else {
    WebUI.comment('No results found for filter - This is acceptable')
}

WebUI.takeFullPageScreenshot('Screenshots/Admin/Fraud/FRD003_03_Verified.png')

WebUI.closeBrowser()
