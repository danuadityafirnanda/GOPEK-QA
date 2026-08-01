import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import org.openqa.selenium.WebElement as WebElement

WebUI.comment('=== TC_History: Search Invoice Number - START ===')

String targetInvoice = 'INV202607300DD4A355'

// ========================================
// STEP 1: Reset Filter Terlebih Dahulu (Biar Bersih)
// ========================================
WebUI.comment('Step 1: Reset filters before search')

TestObject btnFilters = createDynamicObject("//button[contains(., 'Filter') or contains(., 'Filters')]")
if (WebUI.verifyElementPresent(btnFilters, 5, FailureHandling.OPTIONAL)) {
    WebUI.click(btnFilters)
    WebUI.delay(0.5)
    
    TestObject btnReset = createDynamicObject("//button[contains(., 'Reset') or contains(., 'Clear')]")
    if (WebUI.verifyElementPresent(btnReset, 3, FailureHandling.OPTIONAL)) {
        WebUI.click(btnReset)
        WebUI.delay(1) // Jeda waktu modal filter ketutup otomatis
        WebUI.comment('✅ Reset Filters applied successfully')
    }
}

// ========================================
// STEP 2: Input Nomor Invoice di Search Bar
// ========================================
WebUI.comment("Step 2: Searching for invoice number: ${targetInvoice}")

TestObject inputSearch = findTestObject('History/Page_Gopek  Digital Wallet/input_Search invoice number')
WebUI.waitForElementPresent(inputSearch, 10, FailureHandling.STOP_ON_FAILURE)

WebUI.click(inputSearch)
WebUI.delay(0.3)

// Clear text bawaan
WebUI.clearText(inputSearch)
WebUI.setText(inputSearch, targetInvoice)

// Kirim tombol ENTER untuk memicu pencarian
WebUI.sendKeys(inputSearch, Keys.chord(Keys.ENTER))
WebUI.delay(2.5) // Tunggu API/React selesai mengambil data

// ========================================
// STEP 3: Validasi Hasil Pencarian (Cek Row Transaksi Muncul di List)
// Catatan: Invoice number TIDAK tampil di list transaksi, hanya di halaman detail.
// Jadi di list kita cukup pastikan hasil pencarian mengembalikan setidaknya 1 row.
// ========================================
WebUI.comment('Step 3: Verify search returned at least one transaction row')
TestObject firstItem = createDynamicObject("(//a[starts-with(@data-testid, 'transaction-item')])[1]")
boolean isResultShown = WebUI.verifyElementPresent(firstItem, 15, FailureHandling.STOP_ON_FAILURE)
assert isResultShown : "Validation failed: No transaction result for invoice '${targetInvoice}'!"

// ========================================
// STEP 4: Klik Transaksi Pertama -> Masuk Halaman Detail
// ========================================
WebUI.comment('Step 4: Open transaction detail page')
WebUI.click(firstItem)
WebUI.delay(1)

// ========================================
// STEP 5: Validasi Invoice Number di Halaman Detail
// ========================================
WebUI.comment('Step 5: Verify invoice number on detail page')
TestObject invoiceLabel = createDynamicObject("//*[contains(text(), 'Invoice ID')]")
boolean isLabelVisible = WebUI.verifyElementPresent(invoiceLabel, 10, FailureHandling.STOP_ON_FAILURE)
assert isLabelVisible : "Validation failed: Detail page not loaded (no 'Invoice ID' label)!"

TestObject invoiceValue = createDynamicObject("//*[contains(text(), '${targetInvoice}')]")
boolean isInvoiceMatch = WebUI.verifyElementPresent(invoiceValue, 5, FailureHandling.STOP_ON_FAILURE)
assert isInvoiceMatch : "Validation failed: Invoice '${targetInvoice}' not found on detail page!"

WebUI.comment("✅ PASS: Invoice '${targetInvoice}' verified on transaction detail page!")
WebUI.takeFullPageScreenshot('Screenshots/History_Search_Invoice_Passed.png')

// ========================================
// STEP 6: Kembali ke History (State Bersih untuk TC Berikutnya)
// ========================================
WebUI.comment('Step 6: Navigate back to History page')
TestObject btnBack = createDynamicObject("//button[.//*[contains(@class, 'lucide-arrow-left')]] | //a[.//*[contains(@class, 'lucide-arrow-left')]]")
WebUI.waitForElementPresent(btnBack, 10, FailureHandling.STOP_ON_FAILURE)
WebUI.click(btnBack)
WebUI.delay(1)

// VALIDASI KUAT: Pastikan benar-benar kembali ke halaman History (pola sama dgn TC_Detail_002)
TestObject showingText = createDynamicObject("//*[contains(text(), 'Showing')]")
boolean isHistoryVisible = WebUI.verifyElementPresent(showingText, 10, FailureHandling.STOP_ON_FAILURE)
assert isHistoryVisible : "Validation failed: Did not return to History page (no 'Showing' text)!"

WebUI.comment('✅ PASS: Back navigation returned to History page')
WebUI.comment('=== TC_History: Search Invoice Number - COMPLETED ===')


// ============================================================================
// HELPER FUNCTIONS (Di Paling Bawah Skrip)
// ============================================================================

TestObject createDynamicObject(String xpath) {
    TestObject to = new TestObject(xpath)
    to.addProperty("xpath", ConditionType.EQUALS, xpath)
    return to
}

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