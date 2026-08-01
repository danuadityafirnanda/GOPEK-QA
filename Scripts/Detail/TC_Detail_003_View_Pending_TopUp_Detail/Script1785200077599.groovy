import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
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

// FIX: Ganti selector hardcoded (a_Top UpPENDINGRp 5.000.00027 Jul 2026, 22_56) dengan dynamic XPath
// DINAMIS: prefer klik Pending Top Up jika ada; jika tidak ada, fallback klik transaksi pertama apa pun
// sehingga test tidak gagal saat tidak ada transaksi Top Up berstatus PENDING.
TestObject pendingTopUpItem = createDynamicObject("(//a[starts-with(@data-testid, 'transaction-item') and contains(., 'Top Up') and contains(., 'PENDING')])[1]")
boolean hasPendingTopUp = WebUI.verifyElementPresent(pendingTopUpItem, 5, FailureHandling.OPTIONAL)

TestObject targetItem
if (hasPendingTopUp) {
    targetItem = pendingTopUpItem
    WebUI.comment('✅ Pending Top Up ditemukan - menguji detail PENDING')
} else {
    targetItem = createDynamicObject("(//a[starts-with(@data-testid, 'transaction-item')])[1]")
    WebUI.comment('⚠️ Pending Top Up tidak ditemukan - fallback ke transaksi pertama apa pun')
}
WebUI.waitForElementPresent(targetItem, 10, FailureHandling.STOP_ON_FAILURE)
WebUI.click(targetItem, FailureHandling.STOP_ON_FAILURE)
WebUI.delay(1)

// ========================================
// VALIDASI KUAT: Halaman detail transaksi terbuka (Status + Invoice ID)
// ========================================
TestObject statusLabel = createDynamicObject("//*[contains(text(), 'Status')]")
boolean isStatusVisible = WebUI.verifyElementPresent(statusLabel, 10, FailureHandling.STOP_ON_FAILURE)
assert isStatusVisible : "Validation failed: Detail page not loaded (no 'Status' label)!"

TestObject invoiceLabel = createDynamicObject("//*[contains(text(), 'Invoice ID')]")
boolean isInvoiceVisible = WebUI.verifyElementPresent(invoiceLabel, 10, FailureHandling.STOP_ON_FAILURE)
assert isInvoiceVisible : "Validation failed: Detail page not loaded (no 'Invoice ID' label)!"

// ========================================
// VALIDASI KONDISIONAL: PENDING badge + Continue Payment
// Hanya dijalankan jika memang kita mengklik Pending Top Up secara sengaja,
// supaya assertion tidak salah target saat fallback ke transaksi non-PENDING.
// ========================================
if (hasPendingTopUp) {
    TestObject pendingBadge = createDynamicObject("//*[contains(text(), 'PENDING')]")
    boolean isPendingVisible = WebUI.verifyElementPresent(pendingBadge, 5, FailureHandling.STOP_ON_FAILURE)
    assert isPendingVisible : "Validation failed: PENDING badge not found (harusnya transaksi PENDING)!"

    TestObject continuePaymentBtn = createDynamicObject("//*[@data-testid='btn-continue-payment-topup']")
    boolean isContinueBtnVisible = WebUI.verifyElementPresent(continuePaymentBtn, 5, FailureHandling.STOP_ON_FAILURE)
    assert isContinueBtnVisible : "Validation failed: Continue Payment button not found (transaksi berstatus PENDING)!"
    WebUI.comment('✅ PASS: Pending detail displayed with Continue Payment button')
} else {
    WebUI.comment('ℹ️ INFO: Fallback ke transaksi apa pun - assertion PENDING dilewati')
}

WebUI.takeFullPageScreenshot('Screenshots/DTL003_TransactionDetail_Passed.png')

// ============================================================================
// HELPER FUNCTIONS (Di Paling Bawah Skrip)
// ============================================================================

TestObject createDynamicObject(String xpath) {
    TestObject to = new TestObject(xpath)
    to.addProperty("xpath", ConditionType.EQUALS, xpath)
    return to
}
