import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import com.kms.katalon.core.webdriver.driver.DriverFactory

// DEBUG TEST CASE - To inspect Calendar structure when date picker opens
// This will help us understand the actual HTML structure of react-day-picker

// Call setup to login as admin
WebUI.callTestCase(findTestCase('Admin/TC_000_Setup_Login_Admin'), [:], FailureHandling.STOP_ON_FAILURE)

// Navigate to Audit Trail page
WebUI.click(findTestObject('Admin/Navigation/link_audit_trail'))

WebUI.delay(3)

WebUI.comment('=== DEBUG: Inspecting Calendar Date Picker ===')

// Click the date range picker button to open calendar
WebUI.click(findTestObject('Admin/AuditTrail/Filters/btn_date_range_picker'))

WebUI.delay(2)

// Take screenshot of opened calendar
WebUI.takeFullPageScreenshot('Screenshots/Admin/Debug/calendar_opened.png')

// Get WebDriver instance
WebDriver driver = DriverFactory.getWebDriver()

// Try to find calendar popover content
try {
    List<WebElement> popovers = driver.findElements(By.xpath("//*[@data-radix-popper-content-wrapper or contains(@class, 'popover')]"))
    WebUI.comment("Found ${popovers.size()} popover elements")
} catch (Exception e) {
    WebUI.comment("Error finding popover: ${e.message}")
}

// Try to find all button elements within visible calendar
try {
    List<WebElement> buttons = driver.findElements(By.xpath("//button"))
    WebUI.comment("Found ${buttons.size()} total buttons on page")

    // Filter to calendar day buttons
    List<WebElement> dayButtons = driver.findElements(By.xpath("//button[contains(@class, 'day')]"))
    WebUI.comment("Found ${dayButtons.size()} day buttons with class 'day'")

    if (dayButtons.size() > 0) {
        // Show first 10 day buttons
        for (int i = 0; i < Math.min(dayButtons.size(), 10); i++) {
            WebElement btn = dayButtons[i]
            String text = btn.getText()
            String className = btn.getAttribute('class')
            String ariaLabel = btn.getAttribute('aria-label')
            String ariaSelected = btn.getAttribute('aria-selected')
            String dataDay = btn.getAttribute('data-day')
            WebUI.comment("Day button ${i}: text='${text}', class='${className}', aria-label='${ariaLabel}', data-day='${dataDay}'")
        }
    }
} catch (Exception e) {
    WebUI.comment("Error finding day buttons: ${e.message}")
}

// Try to find buttons with text "20"
try {
    List<WebElement> button20 = driver.findElements(By.xpath("//button[normalize-space(.)='20']"))
    WebUI.comment("Found ${button20.size()} buttons with text '20'")

    if (button20.size() > 0) {
        WebElement btn = button20[0]
        String fullClass = btn.getAttribute('class')
        String parent = btn.getAttribute('data-month')
        WebUI.comment("First '20' button: class='${fullClass}'")
    }
} catch (Exception e) {
    WebUI.comment("Error finding '20' buttons: ${e.message}")
}

// Try to find buttons with text "31"
try {
    List<WebElement> button31 = driver.findElements(By.xpath("//button[normalize-space(.)='31']"))
    WebUI.comment("Found ${button31.size()} buttons with text '31'")
} catch (Exception e) {
    WebUI.comment("Error finding '31' buttons: ${e.message}")
}

WebUI.comment('=== DEBUG: Calendar Inspection Complete ===')

WebUI.delay(5)

WebUI.closeBrowser()
