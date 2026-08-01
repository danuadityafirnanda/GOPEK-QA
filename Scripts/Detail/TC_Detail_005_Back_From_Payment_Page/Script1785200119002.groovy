import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType

WebUI.delay(1)

// FIX: Ganti selector berbasis class (svg_lucide lucide-arrow-left h-5 w-5) dengan dynamic XPath
// Menargetkan button/link yang berisi ikon arrow-left (struktur MobileHeader di frontend)
TestObject btnBack = createDynamicObject("//button[.//*[contains(@class, 'lucide-arrow-left')]] | //a[.//*[contains(@class, 'lucide-arrow-left')]]")
WebUI.waitForElementPresent(btnBack, 10, FailureHandling.STOP_ON_FAILURE)
WebUI.click(btnBack, FailureHandling.STOP_ON_FAILURE)
WebUI.delay(1)

// ========================================
// VALIDASI KUAT: Kembali ke halaman utama (Home/History terdeteksi)
// ========================================
TestObject backTarget = createDynamicObject("//*[@data-testid='link-see-all-transactions'] | //*[contains(text(), 'Showing')]")
boolean isBackVisible = WebUI.verifyElementPresent(backTarget, 10, FailureHandling.STOP_ON_FAILURE)
assert isBackVisible : "Validation failed: Did not return to Home/History page!"

WebUI.comment('✅ PASS: Back from payment returned to Home/History page')
WebUI.takeFullPageScreenshot('Screenshots/DTL005_BackFromPayment_Passed.png')

// ============================================================================
// HELPER FUNCTIONS (Di Paling Bawah Skrip)
// ============================================================================

TestObject createDynamicObject(String xpath) {
    TestObject to = new TestObject(xpath)
    to.addProperty("xpath", ConditionType.EQUALS, xpath)
    return to
}
