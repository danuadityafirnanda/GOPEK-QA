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

WebUI.comment('=== TC_Fraud_002: TopUp Rapid Repetition Flagged (API) - START ===')

// ============================================================
// CATATAN QA:
// Aturan fraud sama di semua modul (TopUp, Transfer, Withdraw,
// Merchant Payment) - lihat FraudDetection.java.
// Seed: 2 top-up berturut-turut TANPA delay (jendela 5 detik).
// TopUp create TIDAK memotong saldo, jadi tidak perlu balance seeding.
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

// Jeda >5 detik dari aktivitas test sebelumnya agar window fraud bersih
WebUI.delay(6)

// ========================================
// STEP 2: TopUp pertama (basis aktivitas 5 detik)
// ========================================
WebUI.comment('Step 2: Send first top-up')
String topupBody = JsonOutput.toJson([amount: 10000])

ResponseObject resp1 = postJson('/api/v1/wallet/topup', topupBody, token)
assert resp1.getStatusCode() == 200 : "TopUp 1 failed: ${resp1.getStatusCode()}"
def json1 = new JsonSlurper().parseText(resp1.getResponseText())
WebUI.comment("✅ TopUp 1 success, transactionId=${json1.result.transactionId}")

// ========================================
// STEP 3: TopUp kedua LANGSUNG (dalam jendela 5 detik) -> harus ter-flag fraud
// ========================================
WebUI.comment('Step 3: Send second top-up immediately (within 5s window)')
ResponseObject resp2 = postJson('/api/v1/wallet/topup', topupBody, token)
assert resp2.getStatusCode() == 200 : "TopUp 2 failed: ${resp2.getStatusCode()}"
def json2 = new JsonSlurper().parseText(resp2.getResponseText())

// ========================================
// VALIDASI KUAT: top-up kedua mengandung fraudWarning
// ========================================
String fraudWarning = json2.result.fraudWarning
assert fraudWarning != null : "Validation failed: fraudWarning NOT present on second rapid top-up!"
assert fraudWarning.contains('Rapid transaction repetition') : "Unexpected fraud reason: ${fraudWarning}"
WebUI.comment("✅ PASS: Fraud detected on 2nd rapid top-up: ${fraudWarning}")
WebUI.comment("ℹ️ Flagged transactionId: ${json2.result.transactionId}")

WebUI.comment('=== TC_Fraud_002 - COMPLETED ===')

// ============================================================================
// HELPER FUNCTIONS (Di Paling Bawah Skrip)
// ============================================================================

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
