import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType

WebUI.delay(1)

// ========================================
// VALIDASI KUAT: List transaksi harus termuat (teks "Showing X of Y transactions")
// ========================================
WebUI.comment('Step 2: Verify transaction list loaded (Showing text present)')
TestObject showingText = createDynamicObject("//*[contains(text(), 'Showing')]")
boolean isShowingPresent = WebUI.verifyElementPresent(showingText, 15, FailureHandling.STOP_ON_FAILURE)
assert isShowingPresent : "Validation failed: Transaction list did not load (no 'Showing' text)!"

WebUI.comment('✅ PASS: History page loaded with transaction list')
WebUI.takeFullPageScreenshot('Screenshots/HIS001_LoadDefault_Passed.png')

// ============================================================================
// HELPER FUNCTIONS (Di Paling Bawah Skrip)
// ============================================================================

TestObject createDynamicObject(String xpath) {
    TestObject to = new TestObject(xpath)
    to.addProperty("xpath", ConditionType.EQUALS, xpath)
    return to
}
