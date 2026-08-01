import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent
import internal.GlobalVariable as GlobalVariable
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

WebUI.comment('=== TC_Fraud_001: Transfer Rapid Repetition Flagged (API) - START ===')

// ============================================================
// CATATAN QA:
// Aturan fraud saat ini = "Rapid transaction repetition"
// (2+ aksi dalam jendela 5 detik). Jadi seed dilakukan dengan
// mengirim 2 transaksi berturut-turut TANPA delay di antaranya.
// Balance di-seed via API terlebih dahulu agar test idempoten
// (tidak gagal karena saldo menipis setelah suite dijalankan ulang).
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
WebUI.comment('✅ Login API success - token obtained')

// ========================================
// STEP 1b: Pastikan saldo cukup (balance seeding via API topup)
// ========================================
WebUI.comment('Step 1b: Ensure sufficient balance before transfers')
ensureBalance(token, 100000)

// Jeda >5 detik dari aktivitas (login/topup seeding) agar window fraud bersih
WebUI.delay(6)

// ========================================
// STEP 2: Transfer pertama (basis aktivitas 5 detik)
// ========================================
WebUI.comment('Step 2: Send first transfer (creates 5s window activity)')
String txBody = JsonOutput.toJson([
    destinationAccountNumber: GlobalVariable.TEST_DEST_ACCOUNT_1,
    amount: 10000,
    pin: GlobalVariable.TEST_USER_PIN,
    description: 'FRD001 first transfer'
])

ResponseObject resp1 = postJson('/api/v1/transfers', txBody, token)
assert resp1.getStatusCode() == 201 : "Transfer 1 failed: ${resp1.getStatusCode()}"
def json1 = new JsonSlurper().parseText(resp1.getResponseText())
WebUI.comment("✅ Transfer 1 success, transactionId=${json1.result.transactionId}")

// ========================================
// STEP 3: Transfer kedua LANGSUNG (dalam jendela 5 detik) -> harus ter-flag fraud
// ========================================
WebUI.comment('Step 3: Send second transfer immediately (within 5s window)')
ResponseObject resp2 = postJson('/api/v1/transfers', txBody, token)
assert resp2.getStatusCode() == 201 : "Transfer 2 failed: ${resp2.getStatusCode()}"
def json2 = new JsonSlurper().parseText(resp2.getResponseText())

// ========================================
// VALIDASI KUAT: transaksi kedua mengandung fraudWarning
// ========================================
String fraudWarning = json2.result.fraudWarning
assert fraudWarning != null : "Validation failed: fraudWarning NOT present on second rapid transfer!"
assert fraudWarning.contains('Rapid transaction repetition') : "Unexpected fraud reason: ${fraudWarning}"
WebUI.comment("✅ PASS: Fraud detected on 2nd rapid transfer: ${fraudWarning}")
WebUI.comment("ℹ️ Flagged transactionId: ${json2.result.transactionId}")

WebUI.comment('=== TC_Fraud_001 - COMPLETED ===')

// ============================================================================
// HELPER FUNCTIONS (Di Paling Bawah Skrip)
// ============================================================================

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
