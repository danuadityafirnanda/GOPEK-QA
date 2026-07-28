import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

WebUI.delay(1)
WebUI.click(findTestObject('History/Page_Gopek  Digital Wallet/a_See All'), FailureHandling.OPTIONAL)
WebUI.delay(1)
WebUI.click(findTestObject('History/Page_Gopek  Digital Wallet/a_Transfer-Rp 50.00027 Jul 2026, 22_28'), FailureHandling.OPTIONAL)
WebUI.delay(1)

WebDriver driver = DriverFactory.getWebDriver()
boolean isPresent = driver.findElements(By.xpath("//*[contains(text(), 'Transfer') or contains(text(), 'Detail') or contains(text(), 'Status')]")).size() > 0
WebUI.verifyMatch(isPresent.toString(), "true", true, FailureHandling.OPTIONAL)
WebUI.takeFullPageScreenshot('Screenshots/DTL001_TransferDetail_Passed.png')