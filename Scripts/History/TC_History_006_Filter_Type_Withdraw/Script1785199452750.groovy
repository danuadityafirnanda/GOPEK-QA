import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.WebElement as WebElement

WebUI.comment('=== TC_History: Filter Type WITHDRAW - START ===')

// ========================================
// STEP 1: Buka Modal Filter Pertama Kali
// ========================================
WebUI.comment('Step 1: Click Filters button')
TestObject btnFilters = createDynamicObject("//button[contains(., 'Filter') or contains(., 'Filters')]")
WebUI.waitForElementClickable(btnFilters, 10)
WebUI.click(btnFilters)
WebUI.delay(0.5)

// ========================================
// STEP 2: Reset Filters (Modal Ketutup Otomatis)
// ========================================
WebUI.comment('Step 2: Reset Filters')
TestObject btnReset = createDynamicObject("//button[contains(., 'Reset') or contains(., 'Clear')]")
if (WebUI.verifyElementPresent(btnReset, 3, FailureHandling.OPTIONAL)) {
    WebUI.click(btnReset)
    WebUI.delay(1) // Memberi waktu modal menutup sempurna
    WebUI.comment('✅ Reset Filters clicked (Modal closed automatically)')
} else {
    WebUI.comment('⚠️ Reset button not found, continuing...')
}

// ========================================
// STEP 3: Buka Modal Filter LAGI
// ========================================
WebUI.comment('Step 3: Re-open Filters modal after reset')
WebUI.waitForElementClickable(btnFilters, 10)
WebUI.click(btnFilters)
WebUI.delay(0.5)

// ========================================
// STEP 4: Buka Dropdown Type & Pilih "Top Up"
// ========================================
WebUI.comment('Step 4: Open Type Dropdown & Select Withdraw')

// Klik Dropdown Type (Dropdown Ke-1)
clickTypeDropdown()
WebUI.delay(0.5)

// Pilih opsi 'Withdraw' via Helper Radix UI
selectRadixOption('Withdraw')
WebUI.comment('✅ Selected Type: Withdraw')

// VALIDASI KUAT: Opsi terpilih harus tampil di trigger dropdown
TestObject selectedOptionTrigger = createDynamicObject("//button[@role='combobox' and contains(., 'Withdraw')]")
boolean isSelectedVisible = WebUI.verifyElementPresent(selectedOptionTrigger, 5, FailureHandling.STOP_ON_FAILURE)
assert isSelectedVisible : "Validation failed: Selected option 'Withdraw' not shown in dropdown trigger!"


// ========================================
// STEP 5: Tutup / Silang Modal Filter
// ========================================
WebUI.comment('Step 5: Close filter modal/dialog')
WebUI.click(findTestObject('History/Page_Gopek  Digital Wallet/button_Close'))
WebUI.delay(1)

// ========================================
// STEP 6: Validasi Halaman (Mengecek Teks 'Transactions')
// ========================================
WebUI.comment('Step 6: Verify "Transactions" text on screen')
TestObject transactionsHeader = createDynamicObject("//*[contains(text(), 'Transactions') or contains(text(), 'History')]")
boolean isTransactionsPresent = WebUI.verifyElementPresent(transactionsHeader, 10, FailureHandling.OPTIONAL)

assert isTransactionsPresent : "Validation failed: Could not find 'Transactions' header or text on the page!"
// VALIDASI KUAT: List transaksi termuat setelah filter diterapkan
TestObject showingText = createDynamicObject("//*[contains(text(), 'Showing')]")
boolean isShowingPresent = WebUI.verifyElementPresent(showingText, 15, FailureHandling.STOP_ON_FAILURE)
assert isShowingPresent : "Validation failed: Transaction list did not reload after filter!"
WebUI.comment('✅ PASS: Filter Type Withdraw applied successfully')

WebUI.takeFullPageScreenshot('Screenshots/History_Filter_Type_Withdraw_Passed.png')
WebUI.comment('=== TC_History: Filter Type WITHDRAW - COMPLETED ===')


// ============================================================================
// HELPER FUNCTIONS (Di Paling Bawah Skrip)
// ============================================================================

TestObject createDynamicObject(String xpath) {
    TestObject to = new TestObject(xpath)
    to.addProperty("xpath", ConditionType.EQUALS, xpath)
    return to
}

// Helper khusus klik Dropdown TYPE (Mengambil Dropdown Ke-1 di Modal Filter)
void clickTypeDropdown() {
    String typeDropdownXpath = """
        (//div[@role='dialog']//button[@role='combobox'])[1]
        | (//div[contains(@class, 'dialog') or contains(@class, 'content')]//button[contains(@class, 'select') or @role='combobox'])[1]
        | //button[contains(., 'Types') or contains(., 'Top Up') or contains(., 'Transfer') or contains(., 'Withdraw') or contains(., 'Merchant')]
    """.stripIndent().replaceAll("\n", " ")

    TestObject btnType = createDynamicObject(typeDropdownXpath)
    WebUI.waitForElementPresent(btnType, 10, FailureHandling.STOP_ON_FAILURE)
    
    try {
        WebUI.click(btnType)
    } catch (Exception e) {
        WebElement element = WebUI.findWebElement(btnType, 5)
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