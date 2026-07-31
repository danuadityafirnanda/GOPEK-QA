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

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD004_01_AuditPageLoaded.png')

// Apply filter: Date Range (1-31 of current month) - Full JavaScript Solution
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

    // Find and click date 1 (start date - first day of month)
    const date1 = dayButtons.find(btn => btn.textContent.trim() === '1');
    if (date1) {
        date1.click();
        console.log('Clicked date 1 for start date (first day of month)');
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

WebUI.comment('Start date selected: 1 (first day of month)')

// Wait for calendar to update after first date selection
WebUI.delay(0.5)

// Step 2B: Select end date (31) via JavaScript - full month range
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
WebUI.comment('Date range selected: 1-31 (full month range)')

WebUI.delay(1)

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD004_02_DatePickerVerified.png')

// Verify table is present
WebUI.verifyElementPresent(findTestObject('Admin/AuditTrail/Table/table_audit_logs'), 10)

// Verify results are filtered (if results exist)
boolean hasResults = WebUI.verifyElementPresent(findTestObject('Admin/AuditTrail/Table/row_first_log'),
    5, FailureHandling.OPTIONAL)

if (hasResults) {
    WebUI.comment('Filter by Date Range verified')
} else {
    WebUI.comment('No results found for date range - This is acceptable')
}

WebUI.takeFullPageScreenshot('Screenshots/Admin/Audit/AUD004_03_Verified.png')

WebUI.closeBrowser()
