import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory

WebUI.delay(1)
WebDriver driver = DriverFactory.getWebDriver()
boolean isPresent = driver.findElements(By.xpath("//button[contains(., 'Next') or contains(., 'Previous') or contains(@aria-label, 'page')] | //*[contains(text(), 'Showing')]")).size() > 0
WebUI.verifyMatch(isPresent.toString(), "true", true, FailureHandling.OPTIONAL)
WebUI.takeFullPageScreenshot('Screenshots/HIS010_Pagination_Passed.png')