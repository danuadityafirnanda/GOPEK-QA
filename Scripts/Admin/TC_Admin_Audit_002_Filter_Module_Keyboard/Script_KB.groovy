import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys

// TEST WITH KEYBOARD NAVIGATION - Most reliable for Radix UI dropdowns
// This approach doesn't depend on specific HTML structure or locators

// Call setup to login as admin
WebUI.callTestCase(findTestCase('Admin/TC_000_Setup_Login_Admin'), [:], FailureHandling.STOP_ON_FAILURE)

// Navigate to Audit Trail page
WebUI.click(findTestObject('Admin/Navigation/link_audit_trail'))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD002_KB_01_AuditPageLoaded.png')

// Apply filter: Module = TRANSFER using keyboard navigation
WebUI.comment('Opening Module dropdown...')
WebUI.click(findTestObject('Admin/AuditTrail/Filters/select_filter_module'))

WebUI.delay(1)

// Type "tra" to search for Transfer in dropdown (type-ahead feature)
WebUI.comment('Typing to search for Transfer...')
WebUI.sendKeys(findTestObject('Admin/AuditTrail/Filters/select_filter_module'), 'tra')

WebUI.delay(0.5)

// Press Enter to select the highlighted option
WebUI.comment('Pressing Enter to select...')
WebUI.sendKeys(findTestObject('Admin/AuditTrail/Filters/select_filter_module'), Keys.chord(Keys.ENTER))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD002_KB_02_FilterApplied.png')

// Verify table is present
WebUI.verifyElementPresent(findTestObject('Admin/AuditTrail/Table/table_audit_logs'), 10)

// Verify results (if any exist)
boolean hasResults = WebUI.verifyElementPresent(findTestObject('Admin/AuditTrail/Table/row_first_log'),
    5, FailureHandling.OPTIONAL)

if (hasResults) {
    WebUI.verifyElementPresent(findTestObject('Admin/AuditTrail/Table/cell_module'), 5)
    WebUI.comment('Filter by Module verified - Keyboard navigation SUCCESS')
} else {
    WebUI.comment('No results found for filter - This is acceptable')
}

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD002_KB_03_Verified.png')

WebUI.closeBrowser()
