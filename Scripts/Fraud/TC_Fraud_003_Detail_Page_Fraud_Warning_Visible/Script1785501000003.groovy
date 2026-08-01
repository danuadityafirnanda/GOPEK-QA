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

WebUI.comment('=== TC_Fraud_003: Detail Page Fraud Warning Visible - START ===')

// ============================================================
// CATATAN QA:
// Seed transaksi flagged dilakukan via API (2 transfer beruntun),
// lalu verifikasi UI: buka halaman detail transaksi /transactions/{id}
// dan pastikan teks "Fraud Warning" + reason terlihat.
// (Pola: seed via API, verify via UI - stabil & deterministik)
// ⚠️ MEMBUTUHKAN BROWSER yang sudah login - jalankan dalam suite
//    TS_Fraud_Complete (TC_000_Setup_Login membuka browser duluan).
// ============================================================

// ========================================
// STEP 1: Login via API untuk mendapat token
// ========================================
WebUI.comment('Step 1: Login via API')
ResponseObject loginResp = postJson('/api/v1/auth/login',
    JsonOutput.toJson([email: GlobalVariable.TEST_USER_EMAIL, password: GlobalVariable.TEST_USER_PASSWORD]), null)
assert loginResp.getStatusCode() == 200 : "Login API failed: ${loginResp.getStatusCode()}"
def loginJson = new JsonSlurper().parseText(loginResp.getResponseText())
String token = loginJson.result.accessToken
assert token != null : "Access token not found in login response!"

// ========================================
// STEP 1b: Pastikan saldo cukup (balance seeding via API topup)
// ========================================
WebUI.comment('Step 1b: Ensure sufficient balance before transfers')
ensureBalance(token, 100000)

// Jeda >5 detik dari aktivitas (login/topup seeding) agar window fraud bersih
WebUI.delay(6)

// ========================================
// STEP 2: Seed transaksi flagged via 2 transfer beruntun
// ========================================
WebUI.comment('Step 2: Seed flagged transaction via 2 rapid transfers')
String txBody = JsonOutput.toJson([
    destinationAccountNumber: GlobalVariable.TEST_DEST_ACCOUNT_1,
    amount: 10000,
    pin: GlobalVariable.TEST_USER_PIN,
    description: 'FRD003 seed'
])

ResponseObject resp1 = postJson('/api/v1/transfers', txBody, token)
assert resp1.getStatusCode() == 201 : "Transfer 1 failed: ${resp1.getStatusCode()}"

ResponseObject resp2 = postJson('/api/v1/transfers', txBody, token)
assert resp2.getStatusCode() == 201 : "Transfer 2 failed: ${resp2.getStatusCode()}"
def json2 = new JsonSlurper().parseText(resp2.getResponseText())
String fraudWarning = json2.result.fraudWarning
assert fraudWarning != null : "Validation failed: Second rapid transfer should contain fraudWarning!"
String flaggedTxId = json2.result.transactionId
WebUI.comment("✅ Seed done - flagged transactionId=${flaggedTxId}, reason=${fraudWarning}")

// ========================================
// STEP 3: UI - Buka halaman detail transaksi yang ter-flag fraud
// ========================================
WebUI.comment('Step 3: Open transaction detail page (UI)')
WebUI.navigateToUrl(GlobalVariable.BASE_URL + '/transactions/' + flaggedTxId)
WebUI.waitForPageLoad(10)
WebUI.delay(2)

// ========================================
// VALIDASI KUAT: Teks "Fraud Warning" + reason terlihat di halaman detail
// ========================================
WebUI.comment('Step 4: Verify Fraud Warning text on detail page')
TestObject fraudWarningText = createDynamicObject("//*[contains(text(), 'Fraud Warning')]")
boolean isFraudWarningVisible = WebUI.verifyElementPresent(fraudWarningText, 15, FailureHandling.STOP_ON_FAILURE)
assert isFraudWarningVisible : "Validation failed: 'Fraud Warning' text NOT visible on detail page!"

TestObject fraudReasonText = createDynamicObject("//*[contains(., 'Rapid transaction repetition')]")
boolean isReasonVisible = WebUI.verifyElementPresent(fraudReasonText, 5, FailureHandling.STOP_ON_FAILURE)
assert isReasonVisible : "Validation failed: Fraud reason text NOT visible on detail page!"

WebUI.comment('✅ PASS: Fraud Warning displayed on transaction detail page')
WebUI.takeFullPageScreenshot('Screenshots/FRD003_DetailFraudWarning_Passed.png')

WebUI.comment('=== TC_Fraud_003 - COMPLETED ===')

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
