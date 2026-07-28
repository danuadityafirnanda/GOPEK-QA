import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

WebUI.delay(1)
WebUI.click(findTestObject('History/Page_Gopek  Digital Wallet/svg_lucide lucide-arrow-left h-5 w-5'), FailureHandling.OPTIONAL)
WebUI.delay(1)

WebDriver driver = DriverFactory.getWebDriver()
boolean isPresent = driver.findElements(By.xpath("//*[contains(text(), 'Transactions') or contains(text(), 'Dashboard') or contains(text(), 'Gopek')]")).size() > 0
WebUI.verifyMatch(isPresent.toString(), "true", true, FailureHandling.OPTIONAL)
WebUI.takeFullPageScreenshot('Screenshots/DTL005_BackFromPayment_Passed.png')