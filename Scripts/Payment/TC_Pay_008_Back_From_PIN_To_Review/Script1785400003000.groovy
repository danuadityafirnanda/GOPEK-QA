import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable


WebUI.openBrowser(GlobalVariable.BASE_URL + '/login')

WebUI.setText(findTestObject('Payment/Page_Gopek  Digital Wallet/input_Enter your email'), GlobalVariable.TEST_USER_EMAIL)

WebUI.setText(findTestObject('Payment/Page_Gopek  Digital Wallet/input_Enter your password'), GlobalVariable.TEST_USER_PASSWORD)

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_Log in'))

// ========================================
// VALIDASI: Login berhasil - halaman Home termuat (badge akun terlihat)
// ========================================
TestObject homeAccountBadge = createDynamicObject("//*[@data-testid='badge-home-account-number']")
boolean isHomeLoaded = WebUI.verifyElementPresent(homeAccountBadge, 20, FailureHandling.STOP_ON_FAILURE)
assert isHomeLoaded : "Validation failed: Home page not loaded after login!"
WebUI.comment('✅ PASS: Login successful - Home page loaded')


WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/svg_lucide lucide-qr-code size-8'))

// ============================================================
// STEP QR-SCAN: KAMERA + SCAN QR CODE (BUTUH INTERVENSI MANUAL)
// 1) Setelah klik ikon QR, browser meminta izin kamera
// 2) KLIK "Allow"/"Izinkan" pada popup permission kamera
// 3) Siapkan QR code merchant dari mock service (MockPay) di depan kamera
// 4) Tunggu auto-redirect ke halaman Payment Amount
// ============================================================
WebUI.comment('🔴 TINDAKAN MANUAL: Klik "Allow" izin kamera, lalu arahkan QR code merchant (mock service) ke kamera')
WebUI.delay(10) // Jeda 10 detik: klik izin kamera + posisikan QR code

// ========================================
// VALIDASI: Halaman Payment Amount terbuka (kartu merchant / input amount tampil)
// ========================================
TestObject merchantCard = createDynamicObject("//*[@data-testid='card-payment-merchant'] | //*[@placeholder='0']")
boolean isMerchantVisible = WebUI.verifyElementPresent(merchantCard, 45, FailureHandling.STOP_ON_FAILURE)
assert isMerchantVisible : "Validation failed: Payment amount page not loaded (no merchant card / amount input)! Pastikan QR sudah ter-scan."
WebUI.comment('✅ PASS: Payment amount page loaded with merchant card')


WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_Rp 50.000'))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_Continue'))

// ========================================
// VALIDASI: Halaman Summary terbuka (kartu merchant summary tampil)
// ========================================
TestObject merchantSummaryCard = createDynamicObject("//*[@data-testid='card-payment-merchant-summary']")
boolean isSummaryVisible = WebUI.verifyElementPresent(merchantSummaryCard, 15, FailureHandling.STOP_ON_FAILURE)
assert isSummaryVisible : "Validation failed: Summary page not loaded (no merchant summary card)!"
WebUI.comment('✅ PASS: Payment summary page loaded')


// ========================================
// STEP A: Masuk ke step PIN, lalu klik Back
// (Memvalidasi tombol Back di halaman PIN)
// ========================================
WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_Continue_1'))

TestObject pinDigit1 = createDynamicObject("//*[@data-testid='btn-pin-digit-1']")
boolean isPinVisible = WebUI.verifyElementPresent(pinDigit1, 10, FailureHandling.STOP_ON_FAILURE)
assert isPinVisible : "Validation failed: PIN step not shown!"
WebUI.comment('✅ PASS: PIN keypad displayed (sebelum klik Back)')

// Klik tombol Back di step PIN
TestObject btnBackPin = createDynamicObject("//*[@data-testid='btn-back-payment-summary']")
WebUI.waitForElementPresent(btnBackPin, 10, FailureHandling.STOP_ON_FAILURE)
WebUI.click(btnBackPin)
WebUI.delay(1)

// VALIDASI: Kembali ke step Review (kartu merchant summary tampil lagi, keypad PIN hilang)
boolean isBackAtReview = WebUI.verifyElementPresent(merchantSummaryCard, 10, FailureHandling.STOP_ON_FAILURE)
assert isBackAtReview : "Validation failed: Did not return to Review step after clicking Back!"
boolean isPinGone = !WebUI.verifyElementPresent(pinDigit1, 5, FailureHandling.OPTIONAL)
assert isPinGone : "Validation failed: PIN keypad still visible after clicking Back!"
WebUI.comment('✅ PASS: Back button returned to Review step (PIN keypad hidden)')


// ========================================
// STEP B: Lanjutkan lagi ke PIN dan selesaikan pembayaran
// ========================================
WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_Continue_1'))

boolean isPinVisible2 = WebUI.verifyElementPresent(pinDigit1, 10, FailureHandling.STOP_ON_FAILURE)
assert isPinVisible2 : "Validation failed: PIN step not shown after re-continue!"
WebUI.comment('✅ PASS: PIN keypad displayed (setelah Back & Continue ulang)')

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_1'))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_2'))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_3'))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_4'))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_5'))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_6'))

WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_Confirm  Pay'))

// ========================================
// VALIDASI: Pembayaran berhasil - status sukses tampil
// ========================================
TestObject successText = createDynamicObject("//*[contains(., 'Payment Successful')]")
boolean isSuccessVisible = WebUI.verifyElementPresent(successText, 20, FailureHandling.STOP_ON_FAILURE)
assert isSuccessVisible : "Validation failed: Payment success status not displayed!"
WebUI.comment('✅ PASS: Payment successful - status page displayed')


WebUI.click(findTestObject('Payment/Page_Gopek  Digital Wallet/button_Back to Home'))

// ========================================
// VALIDASI: Kembali ke Home berhasil
// ========================================
TestObject homeBadgeAfter = createDynamicObject("//*[@data-testid='badge-home-account-number']")
boolean isHomeAfter = WebUI.verifyElementPresent(homeBadgeAfter, 10, FailureHandling.STOP_ON_FAILURE)
assert isHomeAfter : "Validation failed: Did not return to Home page!"
WebUI.comment('✅ PASS: Back to Home navigation successful')


// ============================================================================
// HELPER FUNCTIONS
// ============================================================================
TestObject createDynamicObject(String xpath) {
    TestObject to = new TestObject(xpath)

    to.addProperty('xpath', ConditionType.EQUALS, xpath)

    return to
}
