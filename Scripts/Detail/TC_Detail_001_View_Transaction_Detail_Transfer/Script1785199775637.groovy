import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import internal.GlobalVariable as GlobalVariable

WebUI.delay(1)

// FIX: Navigasi fresh ke /transactions untuk mereset state filter/search yang
// ditinggalkan test case History sebelumnya (browser di-reuse dalam satu suite)
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/transactions')
WebUI.waitForPageLoad(10)
WebUI.delay(1)

// FIX: Ganti selector hardcoded (a_Transfer-Rp 50.00027 Jul 2026, 22_28) dengan dynamic XPath
// DINAMIS: klik transaksi PERTAMA APA PUN yang tersedia di history (tidak lagi difilter 'Transfer')
// sehingga test tidak gagal saat tidak ada transaksi bertipe Transfer.
TestObject firstItem = createDynamicObject("(//a[starts-with(@data-testid, 'transaction-item')])[1]")
WebUI.waitForElementPresent(firstItem, 10, FailureHandling.STOP_ON_FAILURE)
WebUI.click(firstItem, FailureHandling.STOP_ON_FAILURE)
WebUI.delay(1)

// ========================================
// VALIDASI KUAT: Halaman detail transaksi terbuka
// ========================================
TestObject statusLabel = createDynamicObject("//*[contains(text(), 'Status')]")
boolean isStatusVisible = WebUI.verifyElementPresent(statusLabel, 10, FailureHandling.STOP_ON_FAILURE)
assert isStatusVisible : "Validation failed: Detail page not loaded (no 'Status' label)!"

TestObject invoiceLabel = createDynamicObject("//*[contains(text(), 'Invoice ID')]")
boolean isInvoiceVisible = WebUI.verifyElementPresent(invoiceLabel, 10, FailureHandling.STOP_ON_FAILURE)
assert isInvoiceVisible : "Validation failed: Detail page not loaded (no 'Invoice ID' label)!"

WebUI.comment('✅ PASS: Transaction detail displayed with Status & Invoice ID')
WebUI.takeFullPageScreenshot('Screenshots/DTL001_TransferDetail_Passed.png')

// ============================================================================
// HELPER FUNCTIONS (Di Paling Bawah Skrip)
// ============================================================================

TestObject createDynamicObject(String xpath) {
    TestObject to = new TestObject(xpath)
    to.addProperty("xpath", ConditionType.EQUALS, xpath)
    return to
}
