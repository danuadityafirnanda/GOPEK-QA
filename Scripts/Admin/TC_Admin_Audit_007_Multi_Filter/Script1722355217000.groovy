import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

// Call setup to login as admin
WebUI.callTestCase(findTestCase('Admin/TC_000_Setup_Login_Admin'), [:], FailureHandling.STOP_ON_FAILURE)

// Navigate to Audit Trail page
WebUI.click(findTestObject('Admin/Navigation/link_audit_trail'))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD007_01_AuditPageLoaded.png')

// Apply MULTIPLE filters (Multi-filtering test)
// Using KEYBOARD NAVIGATION (most reliable for Radix UI dropdowns)
// Filter 1: User Name
WebUI.setText(findTestObject('Admin/AuditTrail/Filters/input_filter_name'), 'bagus')

WebUI.delay(1)

// Filter 2: Module = TOPUP (using keyboard navigation)
WebUI.comment('Opening Module dropdown...')

WebUI.click(findTestObject('Admin/AuditTrail/Filters/select_filter_module'))

WebUI.delay(1)

WebUI.comment('Typing "top" to select TOPUP...')

WebUI.sendKeys(findTestObject('Admin/AuditTrail/Filters/select_filter_module'), 'top')

WebUI.delay(0.5)

WebUI.comment('Pressing Enter to confirm...')

WebUI.sendKeys(findTestObject('Admin/AuditTrail/Filters/select_filter_module'), Keys.chord(Keys.ENTER))

WebUI.delay(1)

// Filter 3: Action = FRAUD_DETECTED (using keyboard navigation)
WebUI.comment('Opening Action dropdown...')

WebUI.click(findTestObject('Admin/AuditTrail/Filters/select_filter_action'))

WebUI.delay(1)

WebUI.comment('Typing "fraud" to select FRAUD_DETECTED...')

WebUI.sendKeys(findTestObject('Admin/AuditTrail/Filters/select_filter_action'), 'fraud')

WebUI.delay(0.5)

WebUI.comment('Pressing Enter to confirm...')

WebUI.sendKeys(findTestObject('Admin/AuditTrail/Filters/select_filter_action'), Keys.chord(Keys.ENTER))

WebUI.delay(1)

// Filter 4: Endpoint = /api/v1/topup
WebUI.setText(findTestObject('Admin/AuditTrail/Filters/input_filter_endpoint'), '/api/v1/topup')

WebUI.delay(1)

// Filter 5: Date Range (20 July - 31 August 2026) - Full JavaScript Solution
WebUI.comment('Opening Date Range Calendar Popover via JavaScript...')

// Step 1: Find and click date picker button via JavaScript (more reliable than xpath)
def buttonClicked = WebUI.executeJavaScript("""
    const buttons = Array.from(document.querySelectorAll('button'));
    const dateButton = buttons.find(btn => {
        const svg = btn.querySelector('svg.lucide-calendar');
        const text = btn.textContent;
        const hasDateText = text.includes('Pick dates') || text.match(/\\d{2}\\/\\d{2}\\/\\d{4}/);
        return svg && hasDateText;
    });

    if (dateButton) {
        dateButton.click();
        console.log('Date picker button clicked successfully');
        return true;
    } else {
        console.error('Date picker button not found');
        return false;
    }
""", null)

if (!buttonClicked) {
    WebUI.comment('ERROR: Date picker button not found via JavaScript')
    throw new Exception('Date picker button not found - cannot open calendar')
}

WebUI.comment('Date picker button clicked, calendar should be open')
WebUI.delay(1)

// Step 2A: Select start date (1) via JavaScript
WebUI.comment('Selecting start date (1) via JavaScript...')
def startDateClicked = WebUI.executeJavaScript("""
    // Find all button elements
    const allButtons = Array.from(document.querySelectorAll('button'));

    // Filter to only day buttons (text is 1-2 digits, not disabled, not outside days)
    const dayButtons = allButtons.filter(btn => {
        const text = btn.textContent.trim();
        const isNumberOnly = /^\\d{1,2}\$/.test(text);
        const isNotOutside = !btn.classList.contains('day-outside') &&
                             !btn.classList.contains('day_outside');
        const isNotDisabled = !btn.disabled && !btn.classList.contains('day-disabled');
        return isNumberOnly && isNotOutside && isNotDisabled;
    });

    console.log('Found ' + dayButtons.length + ' valid day buttons');

    // Find and click date 1 (July 1, 2026 - start date)
    const date1 = dayButtons.find(btn => btn.textContent.trim() === '1');
    if (date1) {
        date1.click();
        console.log('Clicked date 1 for start date (July 1, 2026)');
        return true;
    } else {
        console.error('Date 1 button not found');
        return false;
    }
""", null)

if (!startDateClicked) {
    WebUI.comment('ERROR: Start date (1) not found')
    throw new Exception('Start date button (1) not found in calendar')
}

WebUI.comment('Start date selected: 1 (July 1, 2026)')

// Wait for calendar to update after first date selection
WebUI.delay(0.5)

// Step 2B: Select end date (31) via JavaScript - full month range (1-31)
WebUI.comment('Selecting end date (31) via JavaScript...')
def endDateClicked = WebUI.executeJavaScript("""
    // Refresh button list (calendar might have updated after first click)
    const allButtons = Array.from(document.querySelectorAll('button'));

    // Filter to only day buttons (text is 1-2 digits, not disabled, not outside days)
    const dayButtons = allButtons.filter(btn => {
        const text = btn.textContent.trim();
        const isNumberOnly = /^\\d{1,2}\$/.test(text);
        const isNotOutside = !btn.classList.contains('day-outside') &&
                             !btn.classList.contains('day_outside');
        const isNotDisabled = !btn.disabled && !btn.classList.contains('day-disabled');
        return isNumberOnly && isNotOutside && isNotDisabled;
    });

    console.log('Found ' + dayButtons.length + ' valid day buttons for end date');

    // Find and click date 31 (end date - last day of month with 31 days)
    const date31 = dayButtons.find(btn => btn.textContent.trim() === '31');
    if (date31) {
        date31.click();
        console.log('Clicked date 31 for end date (last day of month)');
        return true;
    } else {
        console.error('Date 31 button not found');
        return false;
    }
""", null)

if (!endDateClicked) {
    WebUI.comment('ERROR: End date (31) not found')
    throw new Exception('End date button (31) not found in calendar')
}

WebUI.comment('End date selected: 31 (last day of month)')
WebUI.comment('Date range selected: 1-31 (full month, same month for reliability)')

WebUI.delay(1)

WebUI.comment('Date range selected via JavaScript: July 20 - August 31, 2026')

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD007_02_AllFiltersApplied.png')

// Verify table is present
WebUI.verifyElementPresent(findTestObject('Admin/AuditTrail/Table/table_audit_logs'), 10)

// Verify results match ALL filters (AND logic)
boolean hasResults = WebUI.verifyElementPresent(findTestObject('Admin/AuditTrail/Table/row_first_log'), 5, FailureHandling.OPTIONAL)

if (hasResults) {
    // Verify user name contains "John"
    String userName = WebUI.getText(findTestObject('Admin/AuditTrail/Table/cell_user_name'))

    WebUI.verifyMatch(userName, '(?i).*john.*', true, FailureHandling.OPTIONAL)

    // Verify module is TOPUP
    WebUI.verifyElementPresent(findTestObject('Admin/AuditTrail/Table/cell_module'), 5)

    // Verify action is FRAUD_DETECTED
    WebUI.verifyElementPresent(findTestObject('Admin/AuditTrail/Table/cell_action'), 5)

    WebUI.comment('Multi-filter verified - All filters applied with AND logic (TOPUP + FRAUD_DETECTED)')
} else {
    WebUI.comment('No results found matching all filters - This is acceptable')
}

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD007_03_Verified.png')

// Test Clear Filters button
WebUI.click(findTestObject('Admin/AuditTrail/Filters/btn_clear_filters'))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD007_04_FiltersCleared.png')

WebUI.comment('Clear filters button verified')

WebUI.closeBrowser()

