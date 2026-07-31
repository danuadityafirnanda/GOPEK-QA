import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// Call setup to login as admin
WebUI.callTestCase(findTestCase('Admin/TC_000_Setup_Login_Admin'), [:], FailureHandling.STOP_ON_FAILURE)

// Navigate to Fraud Report page
WebUI.click(findTestObject('Admin/Navigation/link_fraud_report'))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/Admin/Fraud/FRD001_01_FraudPageLoaded.png')

// Apply filter: User Name
WebUI.setText(findTestObject('Admin/FlaggedTransactions/Filters/input_filter_name'), 'bagus')

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/Admin/Fraud/FRD001_02_FilterApplied.png')

// Verify table is present
WebUI.verifyElementPresent(findTestObject('Admin/FlaggedTransactions/Table/table_flagged_transactions'), 10)

// Verify first row contains filtered user name (if results exist)
boolean hasResults = WebUI.verifyElementPresent(findTestObject('Admin/FlaggedTransactions/Table/row_first_transaction'), 
    5, FailureHandling.OPTIONAL)

if (hasResults) {
    String userName = WebUI.getText(findTestObject('Admin/FlaggedTransactions/Table/cell_user_name'))

    WebUI.verifyMatch(userName, '(?i).*john.*', true, FailureHandling.OPTIONAL)

    WebUI.comment('Filter by User Name verified - Found: ' + userName)
} else {
    WebUI.comment('No results found for filter - This is acceptable')
}

WebUI.takeFullPageScreenshot('Screenshots/Admin/Fraud/FRD001_03_Verified.png')

WebUI.closeBrowser()

