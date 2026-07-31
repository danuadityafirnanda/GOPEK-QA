import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import com.kms.katalon.core.webdriver.driver.DriverFactory

// DEBUG TEST CASE - To inspect dropdown HTML structure
// This will help us understand the actual HTML rendered by Radix UI

// Call setup to login as admin
WebUI.callTestCase(findTestCase('Admin/TC_000_Setup_Login_Admin'), [:], FailureHandling.STOP_ON_FAILURE)

// Navigate to Audit Trail page
WebUI.click(findTestObject('Admin/Navigation/link_audit_trail'))

WebUI.delay(3)

WebUI.comment('=== DEBUG: Inspecting Module Filter Dropdown ===')

// Click the module filter dropdown to open it
WebUI.click(findTestObject('Admin/AuditTrail/Filters/select_filter_module'))

WebUI.delay(2)

// Take screenshot of opened dropdown
WebUI.takeFullPageScreenshot('Screenshots/Admin/Debug/dropdown_opened.png')

// Get WebDriver instance
WebDriver driver = DriverFactory.getWebDriver()

// Try to find all elements with role='option'
try {
    List<WebElement> optionsRole = driver.findElements(By.xpath("//div[@role='option']"))
    WebUI.comment("Found ${optionsRole.size()} elements with role='option'")

    if (optionsRole.size() > 0) {
        for (int i = 0; i < Math.min(optionsRole.size(), 5); i++) {
            WebElement opt = optionsRole[i]
            String text = opt.getText()
            String dataValue = opt.getAttribute('data-value')
            String dataSlot = opt.getAttribute('data-slot')
            String className = opt.getAttribute('class')
            WebUI.comment("Option ${i}: text='${text}', data-value='${dataValue}', data-slot='${dataSlot}'")
        }
    }
} catch (Exception e) {
    WebUI.comment("Error finding role='option': ${e.message}")
}

// Try to find all elements with data-slot='select-item'
try {
    List<WebElement> optionsSlot = driver.findElements(By.xpath("//div[@data-slot='select-item']"))
    WebUI.comment("Found ${optionsSlot.size()} elements with data-slot='select-item'")

    if (optionsSlot.size() > 0) {
        for (int i = 0; i < Math.min(optionsSlot.size(), 5); i++) {
            WebElement opt = optionsSlot[i]
            String text = opt.getText()
            String dataValue = opt.getAttribute('data-value')
            String role = opt.getAttribute('role')
            WebUI.comment("SelectItem ${i}: text='${text}', data-value='${dataValue}', role='${role}'")
        }
    }
} catch (Exception e) {
    WebUI.comment("Error finding data-slot='select-item': ${e.message}")
}

// Try to find select content container
try {
    List<WebElement> contents = driver.findElements(By.xpath("//div[@data-slot='select-content']"))
    WebUI.comment("Found ${contents.size()} elements with data-slot='select-content'")

    if (contents.size() > 0) {
        WebElement content = contents[0]
        String innerHTML = content.getAttribute('innerHTML')
        WebUI.comment("Select content HTML (first 500 chars): ${innerHTML.substring(0, Math.min(500, innerHTML.length()))}")
    }
} catch (Exception e) {
    WebUI.comment("Error finding select-content: ${e.message}")
}

// Try to find by text content
try {
    List<WebElement> transferElements = driver.findElements(By.xpath("//*[normalize-space(text())='Transfer']"))
    WebUI.comment("Found ${transferElements.size()} elements with text 'Transfer'")

    if (transferElements.size() > 0) {
        WebElement elem = transferElements[0]
        String tagName = elem.getTagName()
        String dataValue = elem.getAttribute('data-value')
        String dataSlot = elem.getAttribute('data-slot')
        String role = elem.getAttribute('role')
        WebUI.comment("Transfer element: tag=${tagName}, data-value=${dataValue}, data-slot=${dataSlot}, role=${role}")
    }
} catch (Exception e) {
    WebUI.comment("Error finding by text: ${e.message}")
}

WebUI.comment('=== DEBUG: Inspection Complete ===')

WebUI.delay(5)

WebUI.closeBrowser()
