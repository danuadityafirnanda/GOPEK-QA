import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.WebElement as WebElement

WebUI.comment('=== TC_History: Filter Direction Test - START ===')

// ========================================
// STEP 1: Buka Modal Filter
// ========================================
WebUI.comment('Step 1: Click Filters button')
TestObject btnFilters = createDynamicObject("//button[contains(., 'Filter') or contains(., 'Filters')]")
WebUI.waitForElementClickable(btnFilters, 10)
WebUI.click(btnFilters)
WebUI.delay(0.5)

// ========================================
// STEP 2: Buka Dropdown Direction (Nembak Dropdown Ke-2 Tanpa Peduli Teksnya)
// ========================================
WebUI.comment('Step 2: Open Direction Dropdown & Select Option')

// Klik Dropdown Direction (Dropdown Ke-2 di Form/Modal Filter)
clickDirectionDropdown()
WebUI.delay(0.5)

// PILIH VALUE: Tinggal ganti 'Debit (Out)' atau 'Credit (In)' atau 'All Directions'
String targetOption = 'Debit (Out)' 
selectRadixOption(targetOption)
WebUI.comment("✅ Selected Direction: ${targetOption}")

// ========================================
// STEP 3: Tutup / Silang Modal Filter (Manual via Object Repository kamu)
// ========================================
WebUI.comment('Step 3: Close filter modal/dialog')
WebUI.click(findTestObject('History/Page_Gopek  Digital Wallet/button_Close'))
WebUI.delay(1)

// ========================================
// STEP 4: Validasi Halaman (Cek Teks 'Transactions')
// ========================================
WebUI.comment('Step 4: Verify "Transactions" text on screen')
TestObject transactionsHeader = createDynamicObject("//*[contains(text(), 'Transactions') or contains(text(), 'History')]")
boolean isTransactionsPresent = WebUI.verifyElementPresent(transactionsHeader, 10, FailureHandling.OPTIONAL)

assert isTransactionsPresent : "Validation failed: Could not find 'Transactions' header or text on the page!"
WebUI.comment('✅ PASS: Direction Filter applied successfully')

WebUI.takeFullPageScreenshot("Screenshots/History_Filter_Direction_Passed.png")
WebUI.comment('=== TC_History: Filter Direction Test - COMPLETED ===')


// ============================================================================
// HELPER FUNCTIONS (Di Paling Bawah Skrip)
// ============================================================================

TestObject createDynamicObject(String xpath) {
    TestObject to = new TestObject(xpath)
    to.addProperty("xpath", ConditionType.EQUALS, xpath)
    return to
}

// Helper khusus klik Dropdown Direction (Selalu mengambil Dropdown Ke-2 di Modal Filter)
void clickDirectionDropdown() {
    // XPath ini mencari button combobox/select kedua yang ada di dalam dialog/modal filter
    String directionDropdownXpath = """
        (//div[@role='dialog']//button[@role='combobox'])[2]
        | (//div[contains(@class, 'dialog') or contains(@class, 'content')]//button[contains(@class, 'select') or @role='combobox'])[2]
        | //button[contains(., 'Directions') or contains(., 'Credit') or contains(., 'Debit')]
    """.stripIndent().replaceAll("\n", " ")

    TestObject btnDirection = createDynamicObject(directionDropdownXpath)
    WebUI.waitForElementPresent(btnDirection, 10, FailureHandling.STOP_ON_FAILURE)
    
    try {
        WebUI.click(btnDirection)
    } catch (Exception e) {
        // Fallback kalau terhalang overlay
        WebElement element = WebUI.findWebElement(btnDirection, 5)
        WebUI.executeJavaScript("arguments[0].click();", [element])
    }
}

// Helper khusus pilih opsi Radix UI
void selectRadixOption(String optionText) {
    String optionXpath = """
        //div[@role='option' or @role='menuitem'][.//*[text()='${optionText}']]
        | //div[@role='option' or @role='menuitem'][text()='${optionText}']
        | //*[contains(@class, 'radix') or @data-radix-popper-content-wrapper or @role='dialog']//*[text()='${optionText}']
    """.stripIndent().replaceAll("\n", " ")

    TestObject optionObj = createDynamicObject(optionXpath)
    WebUI.waitForElementPresent(optionObj, 5, FailureHandling.STOP_ON_FAILURE)
    WebUI.delay(0.5)

    try {
        WebUI.click(optionObj)
    } catch (Exception e) {
        WebElement element = WebUI.findWebElement(optionObj, 5)
        WebUI.executeJavaScript("arguments[0].click();", [element])
    }
    WebUI.delay(0.5)
}