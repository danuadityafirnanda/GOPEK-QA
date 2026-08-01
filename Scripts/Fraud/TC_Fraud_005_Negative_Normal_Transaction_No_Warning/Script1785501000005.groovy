import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent
import internal.GlobalVariable as GlobalVariable
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

WebUI.comment('=== TC_Fraud_005: Negative - Normal Transaction No Warning - START ===')

// ============================================================
// CATATAN QA:
// Negative test: transaksi TUNGGAL (tanpa repetisi dalam 5 detik)
// TIDAK boleh menghasilkan fraudWarning di response API, dan
// halaman detail TIDAK boleh menampilkan teks "Fraud Warning".
// ⚠️ Bagian UI membutuhkan browser yang sudah login (jalankan dalam
//    suite TS_Fraud_Complete).
// ============================================================

// ========================================
// STEP 1: Login via API
// ========================================
WebUI.comment('Step 1: Login via API')
ResponseObject loginResp = postJson('/api/v1/auth/login',
    JsonOutput.toJson([email: GlobalVariable.TEST_USER_EMAIL, password: GlobalVariable.TEST_USER_PASSWORD]), null)
assert loginResp.getStatusCode() == 200 : "Login API failed: ${loginResp.getStatusCode()}"
def loginJson = new JsonSlurper().parseText(loginResp.getResponseText())
String token = loginJson.result.accessToken
assert token != null : "Access token not found in login response!"
WebUI.comment('✅ Login API success - token obtained')

// ========================================
// STEP 1b: Pastikan saldo cukup (balance seeding via API topup)
// ========================================
WebUI.comment('Step 1b: Ensure sufficient balance before transfer')
ensureBalance(token, 100000)

// Jeda >5 detik dari aktivitas (login/topup seeding) agar window fraud bersih
WebUI.delay(6)

// ========================================
// STEP 2: Satu transaksi saja (tanpa repetisi)
// ========================================
WebUI.comment('Step 2: Send a single normal transfer')
String txBody = JsonOutput.toJson([
    destinationAccountNumber: GlobalVariable.TEST_DEST_ACCOUNT_1,
    amount: 10000,
    pin: GlobalVariable.TEST_USER_PIN,
    description: 'FRD005 normal single'
])

ResponseObject resp = postJson('/api/v1/transfers', txBody, token)
assert resp.getStatusCode() == 201 : "Transfer failed: ${resp.getStatusCode()}"
def json = new JsonSlurper().parseText(resp.getResponseText())
String normalTxId = json.result.transactionId
WebUI.comment("✅ Transfer success, transactionId=${normalTxId}")

// ========================================
// VALIDASI KUAT (API): TIDAK ada fraudWarning pada transaksi normal
// ========================================
String fraudWarning = json.result.fraudWarning
assert fraudWarning == null : "Validation failed: Normal transaction should NOT contain fraudWarning, but got: ${fraudWarning}"
WebUI.comment('✅ PASS (API): No fraud warning on normal (single) transaction')

// ========================================
// STEP 3: Negative UI - halaman detail TIDAK menampilkan Fraud Warning
// ========================================
WebUI.comment('Step 3: Verify Fraud Warning is ABSENT on detail page (UI)')
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/transactions/' + normalTxId)
WebUI.waitForPageLoad(10)
WebUI.delay(2)

// Sanity check POSITIF dulu: pastikan halaman detail benar-benar ter-render
// (label 'Invoice ID' terlihat). Ini mencegah false-positive pada negative
// assertion - jika halaman gagal load (404/error), test harus FAIL, bukan
// lulus karena elemen 'Fraud Warning' kebetulan tidak ada.
TestObject invoiceLabel = createDynamicObject("//*[contains(text(), 'Invoice ID')]")
boolean isDetailLoaded = WebUI.verifyElementPresent(invoiceLabel, 15, FailureHandling.STOP_ON_FAILURE)
assert isDetailLoaded : "Validation failed: Detail page did not render - negative check is not valid!"

TestObject fraudWarningText = createDynamicObject("//*[contains(text(), 'Fraud Warning')]")
boolean isFraudWarningAbsent = WebUI.verifyElementNotPresent(fraudWarningText, 10, FailureHandling.STOP_ON_FAILURE)
assert isFraudWarningAbsent : "Validation failed: 'Fraud Warning' text SHOULD NOT appear for a normal transaction!"

WebUI.comment('✅ PASS (UI): No Fraud Warning shown on normal transaction detail page')
WebUI.takeFullPageScreenshot('Screenshots/FRD005_Negative_NoFraudWarning_Passed.png')

WebUI.comment('=== TC_Fraud_005 - COMPLETED ===')

// ============================================================================
// HELPER FUNCTIONS (Di Paling Bawah Skrip)
// ============================================================================

TestObject createDynamicObject(String xpath) {
    TestObject to = new TestObject(xpath)
    to.addProperty("xpath", ConditionType.EQUALS, xpath)
    return to
}

// Seed saldo: jika balance < minBalance, topup create + pay via API
void ensureBalance(String token, long minBalance) {
    ResponseObject walletResp = getJson('/api/v1/wallet', token)
    assert walletResp.getStatusCode() == 200 : "Wallet API failed: ${walletResp.getStatusCode()}"

    def walletJson = new JsonSlurper().parseText(walletResp.getResponseText())
    BigDecimal currentBalance = walletJson.result.currentBalance as BigDecimal
    WebUI.comment("ℹ️ Current balance: ${currentBalance}, required min: ${minBalance}")

    if (currentBalance < minBalance) {
        long topupAmount = (minBalance - currentBalance.longValue()) + 50000
        ResponseObject createResp = postJson('/api/v1/wallet/topup', JsonOutput.toJson([amount: topupAmount]), token)
        assert createResp.getStatusCode() == 200 : "TopUp create failed: ${createResp.getStatusCode()}"
        def createJson = new JsonSlurper().parseText(createResp.getResponseText())
        String vaNumber = createJson.result.vaNumber
        assert vaNumber != null : "vaNumber not found in topup response!"

        ResponseObject payResp = postJson('/api/v1/wallet/' + vaNumber + '/pay', '{}', token)
        assert payResp.getStatusCode() == 200 : "TopUp pay failed: ${payResp.getStatusCode()}"
        WebUI.comment('✅ Balance seeded via API topup + pay')
    } else {
        WebUI.comment('✅ Balance is sufficient - no seeding needed')
    }
}

ResponseObject postJson(String path, String body, String token) {
    RequestObject req = new RequestObject(path)
    req.setRestUrl(GlobalVariable.BASE_URL + path)
    req.setRestRequestMethod('POST')

    List<TestObjectProperty> headers = [new TestObjectProperty('Content-Type', ConditionType.EQUALS, 'application/json')]
    if (token != null) {
        headers.add(new TestObjectProperty('Authorization', ConditionType.EQUALS, "Bearer ${token}"))
    }
    req.setHttpHeaderProperties(headers)
    req.setBodyContent(new HttpTextBodyContent(body))

    return WS.sendRequest(req)
}

ResponseObject getJson(String path, String token) {
    RequestObject req = new RequestObject(path)
    req.setRestUrl(GlobalVariable.BASE_URL + path)
    req.setRestRequestMethod('GET')

    List<TestObjectProperty> headers = [new TestObjectProperty('Content-Type', ConditionType.EQUALS, 'application/json')]
    if (token != null) {
        headers.add(new TestObjectProperty('Authorization', ConditionType.EQUALS, "Bearer ${token}"))
    }
    req.setHttpHeaderProperties(headers)

    return WS.sendRequest(req)
}
