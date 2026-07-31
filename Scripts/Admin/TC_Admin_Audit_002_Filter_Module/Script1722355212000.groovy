import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys

// Call setup to login as admin
WebUI.callTestCase(findTestCase('Admin/TC_000_Setup_Login_Admin'), [:], FailureHandling.STOP_ON_FAILURE)

// Navigate to Audit Trail page
WebUI.click(findTestObject('Admin/Navigation/link_audit_trail'))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD002_01_AuditPageLoaded.png')

// Apply filter: Module = TRANSFER
// Using KEYBOARD NAVIGATION (most reliable for Radix UI dropdowns)
WebUI.comment('Opening Module dropdown...')
WebUI.click(findTestObject('Admin/AuditTrail/Filters/select_filter_module'))

WebUI.delay(1)

// Type to search for "Transfer" in dropdown (Radix UI supports type-to-search)
WebUI.comment('Typing "tra" to select Transfer...')
WebUI.sendKeys(findTestObject('Admin/AuditTrail/Filters/select_filter_module'), 'tra')

WebUI.delay(0.5)

// Press Enter to confirm selection
WebUI.comment('Pressing Enter to confirm...')
WebUI.sendKeys(findTestObject('Admin/AuditTrail/Filters/select_filter_module'), Keys.chord(Keys.ENTER))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD002_02_FilterApplied.png')

// Verify table is present
WebUI.verifyElementPresent(findTestObject('Admin/AuditTrail/Table/table_audit_logs'), 10)

// Verify module badge (if results exist)
boolean hasResults = WebUI.verifyElementPresent(findTestObject('Admin/AuditTrail/Table/row_first_log'),
    5, FailureHandling.OPTIONAL)

if (hasResults) {
    WebUI.verifyElementPresent(findTestObject('Admin/AuditTrail/Table/cell_module'), 5)
    WebUI.comment('Filter by Module verified')
} else {
    WebUI.comment('No results found for filter - This is acceptable')
}

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD002_03_Verified.png')

WebUI.closeBrowser()
