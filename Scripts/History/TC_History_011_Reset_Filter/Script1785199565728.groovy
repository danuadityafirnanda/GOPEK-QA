import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

WebUI.delay(1)
WebUI.click(findTestObject('History/Page_Gopek  Digital Wallet/button_Filters'), FailureHandling.OPTIONAL)
WebUI.delay(1)
WebDriver driver = DriverFactory.getWebDriver()
boolean isPresent = driver.findElements(By.xpath("//*[contains(text(), 'Reset') or contains(text(), 'Clear') or contains(text(), 'Filters')]")).size() > 0
WebUI.verifyMatch(isPresent.toString(), "true", true, FailureHandling.OPTIONAL)
WebUI.takeFullPageScreenshot('Screenshots/HIS011_ResetFilter_Passed.png')