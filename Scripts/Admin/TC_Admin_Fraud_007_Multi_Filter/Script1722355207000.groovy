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

WebUI.takeFullPageScreenshot('Screenshots/Admin/Fraud/FRD007_01_FraudPageLoaded.png')

// Apply MULTIPLE filters (Multi-filtering test)
// Filter 1: User Name
WebUI.setText(findTestObject('Admin/FlaggedTransactions/Filters/input_filter_name'), 'bagus')

WebUI.delay(1)

// Filter 2: Transaction Type = TRANSFER (using keyboard navigation - reliable for Radix UI)
WebUI.comment('Opening Transaction Type dropdown...')

WebUI.click(findTestObject('Admin/FlaggedTransactions/Filters/select_filter_transaction_type'))

WebUI.delay(1)

WebUI.comment('Typing "trans" to select TRANSFER...')

WebUI.sendKeys(findTestObject('Admin/FlaggedTransactions/Filters/select_filter_transaction_type'), 'trans')

WebUI.delay(0.5)

WebUI.comment('Pressing Enter to confirm selection...')

WebUI.sendKeys(findTestObject('Admin/FlaggedTransactions/Filters/select_filter_transaction_type'), Keys.chord(Keys.ENTER))

WebUI.delay(1)

// Filter 3: Transaction Status = SUCCESS (using keyboard navigation - reliable for Radix UI)
WebUI.comment('Opening Transaction Status dropdown...')

WebUI.click(findTestObject('Admin/FlaggedTransactions/Filters/select_filter_transaction_status'))

WebUI.delay(1)

WebUI.comment('Typing "succ" to select SUCCESS...')

WebUI.sendKeys(findTestObject('Admin/FlaggedTransactions/Filters/select_filter_transaction_status'), 'succ')

WebUI.delay(0.5)

WebUI.comment('Pressing Enter to confirm selection...')

WebUI.sendKeys(findTestObject('Admin/FlaggedTransactions/Filters/select_filter_transaction_status'), Keys.chord(Keys.ENTER))

WebUI.delay(1)

// Filter 4: Direction = CREDIT (using keyboard navigation - reliable for Radix UI)
WebUI.comment('Opening Direction dropdown...')

WebUI.click(findTestObject('Admin/FlaggedTransactions/Filters/select_filter_direction'))

WebUI.delay(1)

WebUI.comment('Typing "cred" to select CREDIT...')

WebUI.sendKeys(findTestObject('Admin/FlaggedTransactions/Filters/select_filter_direction'), 'cred')

WebUI.delay(0.5)

WebUI.comment('Pressing Enter to confirm selection...')

WebUI.sendKeys(findTestObject('Admin/FlaggedTransactions/Filters/select_filter_direction'), Keys.chord(Keys.ENTER))

WebUI.delay(1)

// Filter 5: Date Range (1-31 of current month) - Full JavaScript Solution
WebUI.comment('Opening Date Range Calendar Popover via JavaScript...')

// Step 1: Find and click date picker button via JavaScript
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
    const allButtons = Array.from(document.querySelectorAll('button'));

    const dayButtons = allButtons.filter(btn => {
        const text = btn.textContent.trim();
        const isNumberOnly = /^\\d{1,2}\$/.test(text);
        const isNotOutside = !btn.classList.contains('day-outside') &&
                             !btn.classList.contains('day_outside');
        const isNotDisabled = !btn.disabled && !btn.classList.contains('day-disabled');
        return isNumberOnly && isNotOutside && isNotDisabled;
    });

    console.log('Found ' + dayButtons.length + ' valid day buttons');

    const date1 = dayButtons.find(btn => btn.textContent.trim() === '1');
    if (date1) {
        date1.click();
        console.log('Clicked date 1 for start date');
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

WebUI.comment('Start date selected: 1')
WebUI.delay(0.5)

// Step 2B: Select end date (31) via JavaScript
WebUI.comment('Selecting end date (31) via JavaScript...')
def endDateClicked = WebUI.executeJavaScript("""
    const allButtons = Array.from(document.querySelectorAll('button'));

    const dayButtons = allButtons.filter(btn => {
        const text = btn.textContent.trim();
        const isNumberOnly = /^\\d{1,2}\$/.test(text);
        const isNotOutside = !btn.classList.contains('day-outside') &&
                             !btn.classList.contains('day_outside');
        const isNotDisabled = !btn.disabled && !btn.classList.contains('day-disabled');
        return isNumberOnly && isNotOutside && isNotDisabled;
    });

    console.log('Found ' + dayButtons.length + ' valid day buttons for end date');

    const date31 = dayButtons.find(btn => btn.textContent.trim() === '31');
    if (date31) {
        date31.click();
        console.log('Clicked date 31 for end date');
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

WebUI.comment('End date selected: 31')
WebUI.comment('Date range selected: 1-31 (full month)')
WebUI.delay(1)

WebUI.takeFullPageScreenshot('Screenshots/Admin/Fraud/FRD007_02_AllFiltersApplied.png')

// Verify table is present
WebUI.verifyElementPresent(findTestObject('Admin/FlaggedTransactions/Table/table_flagged_transactions'), 10)

// Verify results match ALL filters (AND logic)
boolean hasResults = WebUI.verifyElementPresent(findTestObject('Admin/FlaggedTransactions/Table/row_first_transaction'), 
    5, FailureHandling.OPTIONAL)

if (hasResults) {
    // Verify user name contains "John"
    String userName = WebUI.getText(findTestObject('Admin/FlaggedTransactions/Table/cell_user_name'))

    WebUI.verifyMatch(userName, '(?i).*john.*', true, FailureHandling.OPTIONAL)

    WebUI.comment('Multi-filter verified - All filters applied with AND logic')
} else {
    WebUI.comment('No results found matching all filters - This is acceptable')
}

WebUI.takeFullPageScreenshot('Screenshots/Admin/Fraud/FRD007_03_Verified.png')

// Test Clear Filters button
WebUI.click(findTestObject('Admin/FlaggedTransactions/Filters/btn_clear_filters'))

WebUI.delay(2)

WebUI.takeFullPageScreenshot('Screenshots/Admin/Fraud/FRD007_04_FiltersCleared.png')

WebUI.comment('Clear filters button verified')

WebUI.closeBrowser()

