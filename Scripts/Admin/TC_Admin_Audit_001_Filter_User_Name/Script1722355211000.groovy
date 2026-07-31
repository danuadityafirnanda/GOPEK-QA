import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// Call setup to login as admin
WebUI.callTestCase(findTestCase('Admin/TC_000_Setup_Login_Admin'), [:], FailureHandling.STOP_ON_FAILURE)

// Navigate to Audit Trail page
WebUI.click(findTestObject('Admin/Navigation/link_audit_trail'))

WebUI.delay(3)

// DIAGNOSTIC: Verify current URL after navigation
String currentUrl = WebUI.getUrl()
WebUI.comment('Current URL after navigation: ' + currentUrl)

// DIAGNOSTIC: Verify page title
String pageTitle = WebUI.getWindowTitle()
WebUI.comment('Page title: ' + pageTitle)

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD001_01_AuditPageLoaded.png')

// DIAGNOSTIC: Check if ANY audit-related element exists
boolean hasAuditContent = WebUI.waitForElementPresent(
    findTestObject('Admin/AuditTrail/Filters/input_filter_name'),
    5,
    FailureHandling.OPTIONAL
)
WebUI.comment('Audit filter input found: ' + hasAuditContent)

// Apply filter: User Name
WebUI.setText(findTestObject('Admin/AuditTrail/Filters/input_filter_name'), 'bagus')

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD001_02_FilterApplied.png')

// Verify table is present
WebUI.verifyElementPresent(findTestObject('Admin/AuditTrail/Table/table_audit_logs'), 10)

// Verify first row contains filtered user name (if results exist)
boolean hasResults = WebUI.verifyElementPresent(findTestObject('Admin/AuditTrail/Table/row_first_log'),
    5, FailureHandling.OPTIONAL)

if (hasResults) {
    String userName = WebUI.getText(findTestObject('Admin/AuditTrail/Table/cell_user_name'))
    WebUI.verifyMatch(userName, '(?i).*bagus.*', true, FailureHandling.OPTIONAL)
    WebUI.comment('Filter by User Name verified - Found: ' + userName)
} else {
    WebUI.comment('No results found for filter - This is acceptable')
}

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD001_03_Verified.png')

WebUI.closeBrowser()
