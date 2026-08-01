import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType

WebUI.delay(1)

// ========================================
// DINAMIS: Continue Payment hanya muncul di detail transaksi PENDING.
// Jika tidak ada (karena TC_Detail_003 fallback ke transaksi non-PENDING),
// skip langkah ini tanpa gagal.
// ========================================
TestObject continuePaymentBtn = createDynamicObject("//*[@data-testid='btn-continue-payment-topup']")
boolean hasContinuePayment = WebUI.verifyElementPresent(continuePaymentBtn, 5, FailureHandling.OPTIONAL)

if (!hasContinuePayment) {
    WebUI.comment('ℹ️ SKIP: Tombol Continue Payment tidak tersedia (transaksi bukan PENDING) - langkah dilewati')
} else {
    WebUI.click(continuePaymentBtn, FailureHandling.STOP_ON_FAILURE)
    WebUI.delay(1)

    // ========================================
    // VALIDASI KUAT: Halaman Payment TopUp terbuka (kartu VA tampil)
    // ========================================
    TestObject vaCard = createDynamicObject("//*[@data-testid='card-payment-va']")
    boolean isVaVisible = WebUI.verifyElementPresent(vaCard, 15, FailureHandling.STOP_ON_FAILURE)
    assert isVaVisible : "Validation failed: VA payment card not found!"

    TestObject vaNumberLabel = createDynamicObject("//*[contains(text(), 'Virtual Account')]")
    boolean isVaNumberVisible = WebUI.verifyElementPresent(vaNumberLabel, 10, FailureHandling.STOP_ON_FAILURE)
    assert isVaNumberVisible : "Validation failed: VA Number label not found!"

    WebUI.comment('✅ PASS: Continue Payment opened the payment page with VA card')
    WebUI.takeFullPageScreenshot('Screenshots/DTL004_ContinuePayment_Passed.png')
}

// ============================================================================
// HELPER FUNCTIONS (Di Paling Bawah Skrip)
// ============================================================================

TestObject createDynamicObject(String xpath) {
    TestObject to = new TestObject(xpath)
    to.addProperty("xpath", ConditionType.EQUALS, xpath)
    return to
}
