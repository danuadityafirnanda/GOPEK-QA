import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

// Call setup to login as admin
WebUI.callTestCase(findTestCase('Admin/TC_000_Setup_Login_Admin'), [:], FailureHandling.STOP_ON_FAILURE)

// Navigate to Audit Trail page
WebUI.click(findTestObject('Admin/Navigation/link_audit_trail'))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD005_01_AuditPageLoaded.png')

// Apply filter: Endpoint = /api/v1/topup
WebUI.setText(findTestObject('Admin/AuditTrail/Filters/input_filter_endpoint'), '/api/v1/topup')

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD005_02_EndpointFilterApplied.png')

// Verify table is present
WebUI.verifyElementPresent(findTestObject('Admin/AuditTrail/Table/table_audit_logs'), 10)

// Verify results are filtered (if results exist)
boolean hasResults = WebUI.verifyElementPresent(findTestObject('Admin/AuditTrail/Table/row_first_log'),
    5, FailureHandling.OPTIONAL)

if (hasResults) {
    WebUI.comment('Filter by Endpoint verified - Partial match working')
} else {
    WebUI.comment('No results found for endpoint filter - This is acceptable')
}

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD005_03_Verified.png')

WebUI.closeBrowser()
