import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.WebElement as WebElement

WebUI.comment('=== TC_History: Filter Direction CREDIT - START ===')

// ========================================
// STEP 1: Buka Modal Filter
// ========================================
WebUI.comment('Step 1: Click Filters button')
// Menggunakan XPath dinamis berbasis teks agar tahan perubahan UI
TestObject btnFilters = createDynamicObject("//button[contains(., 'Filter') or contains(., 'Filters')]")
WebUI.waitForElementClickable(btnFilters, 10)
WebUI.click(btnFilters)
WebUI.delay(0.5)

// ========================================
// STEP 2: Buka Dropdown Direction & Pilih Credit (In)
// ========================================
WebUI.comment('Step 2: Select Direction -> Credit (In)')

// FIX: Gunakan XPath berbasis teks untuk tombol All Directions agar tidak error posisi div/class
TestObject btnAllDirections = createDynamicObject("//button[contains(., 'All Directions') or contains(., 'Direction')] | //*[text()='All Directions']")
WebUI.waitForElementClickable(btnAllDirections, 10)
WebUI.click(btnAllDirections)
WebUI.delay(0.5)

// Pilih opsi 'Credit (In)' via Radix Helper
selectRadixOption('Credit (In)')
WebUI.comment('✅ Selected Direction: Credit (In)')

// VALIDASI KUAT: Opsi terpilih harus tampil di trigger dropdown
TestObject selectedOptionTrigger = createDynamicObject("//button[@role='combobox' and contains(., 'Credit (In)')]")
boolean isSelectedVisible = WebUI.verifyElementPresent(selectedOptionTrigger, 5, FailureHandling.STOP_ON_FAILURE)
assert isSelectedVisible : "Validation failed: Selected option 'Credit (In)' not shown in dropdown trigger!"


// ========================================
// STEP 3: Tutup / Silang Modal Filter
// ========================================
WebUI.comment('Step 3: Close filter modal/dialog')
// Memakai Object Repository statis milikmu untuk tombol Close
WebUI.click(findTestObject('History/Page_Gopek  Digital Wallet/button_Close'))
WebUI.delay(1)

// ========================================
// STEP 4: Validasi Halaman (Mengecek Teks 'Transactions')
// ========================================
WebUI.comment('Step 4: Verify "Transactions" text on screen')
TestObject transactionsHeader = createDynamicObject("//*[contains(text(), 'Transactions') or contains(text(), 'History')]")
boolean isTransactionsPresent = WebUI.verifyElementPresent(transactionsHeader, 10, FailureHandling.OPTIONAL)

assert isTransactionsPresent : "Validation failed: Could not find 'Transactions' header or text on the page!"
// VALIDASI KUAT: List transaksi termuat setelah filter diterapkan
TestObject showingText = createDynamicObject("//*[contains(text(), 'Showing')]")
boolean isShowingPresent = WebUI.verifyElementPresent(showingText, 15, FailureHandling.STOP_ON_FAILURE)
assert isShowingPresent : "Validation failed: Transaction list did not reload after filter!"
WebUI.comment('✅ PASS: Filter Credit applied and page loaded successfully')

WebUI.takeFullPageScreenshot('Screenshots/History_Filter_Credit_Passed.png')
WebUI.comment('=== TC_History: Filter Direction CREDIT - COMPLETED ===')


// ============================================================================
// HELPER FUNCTIONS (Ditaruh di bagian PALING BAWAH skrip)
// ============================================================================

// 1. Helper membuat TestObject dinamis berbasis XPath
TestObject createDynamicObject(String xpath) {
    TestObject to = new TestObject(xpath)
    to.addProperty("xpath", ConditionType.EQUALS, xpath)
    return to
}

// 2. Helper memilih opsi Radix UI dengan Fallback JavaScript Click
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
        WebUI.comment("Native click intercepted by overlay. Falling back to JavaScript Click for: ${optionText}")
        WebElement element = WebUI.findWebElement(optionObj, 5)
        WebUI.executeJavaScript("arguments[0].click();", [element])
    }
    
    WebUI.delay(0.5)
}