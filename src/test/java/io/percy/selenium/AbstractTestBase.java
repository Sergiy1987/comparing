package io.percy.selenium;

import java.text.SimpleDateFormat;
import java.util.Date;


public abstract class AbstractTestBase {
    public static final String buildName = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

  /*  @Rule
    public TestName testName = new TestName() {
        public String getMethodName() {
            return String.format("%s", super.getMethodName());
        }
    };*/

//    @Rule
//    public SauceTestWatcher resultReportingTestWatcher = new SauceTestWatcher();

    public static final String USERNAME = "nedved1";
    public static final String AUTOMATE_KEY = "oTA4GGrEMQSwSLCxQM78";
    public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
/*    protected RemoteWebDriver driver;

    public class SauceTestWatcher extends TestWatcher {
        @Override
        protected void failed(Throwable e, Description description) {
            driver.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": " + "\""+e.getMessage() +"\"" +"}}");
        }

        @Override
        protected void succeeded(Description description) {
            driver.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Success\"}}");
        }

        @Override
        protected void finished(Description description) {
            driver.quit();
        }
    }*/
}
