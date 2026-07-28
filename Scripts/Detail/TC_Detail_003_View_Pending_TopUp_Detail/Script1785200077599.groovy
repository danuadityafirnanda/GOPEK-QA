import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

WebUI.delay(1)
WebUI.click(findTestObject('History/Page_Gopek  Digital Wallet/a_Top UpPENDINGRp 5.000.00027 Jul 2026, 22_56'), FailureHandling.OPTIONAL)
WebUI.delay(1)

WebDriver driver = DriverFactory.getWebDriver()
boolean isPresent = driver.findElements(By.xpath("//*[contains(text(), 'PENDING') or contains(text(), 'Top Up') or contains(text(), 'Continue Payment')]")).size() > 0
WebUI.verifyMatch(isPresent.toString(), "true", true, FailureHandling.OPTIONAL)
WebUI.takeFullPageScreenshot('Screenshots/DTL003_PendingTopUpDetail_Passed.png')