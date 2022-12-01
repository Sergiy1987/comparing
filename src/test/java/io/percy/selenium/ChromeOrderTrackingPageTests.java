package io.percy.selenium;

import com.codeborne.selenide.Selenide;
import io.percy.selenium.data.BrowserName;
import io.percy.selenium.logger.TestResultLoggerExtension;
import io.percy.selenium.testBase.AbstractTestBase;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@ExtendWith({TestResultLoggerExtension.class})
public class ChromeOrderTrackingPageTests extends AbstractTestBase {
    private TestInfo testInfo;

    @BeforeEach
    protected void init(TestInfo testInfo) {
        this.testInfo = testInfo;
    }

    private static Stream<Arguments> browserParameters() {
        return Stream.of(
                arguments(BrowserName.Chrome.name(), "Windows", "10", "latest", "1280x1024", "Chrome_latest_Windows_10_1280x1024")
//            arguments(BrowserName.Chrome.name(), "Windows", "11", "latest", "1280x1024", "Chrome_latest_Windows_11_1280x1024"),
//            arguments(BrowserName.Firefox.name(), "Windows", "10", "latest", "1280x1024", "Firefox_latest_Windows_10_1280x1024"),
//            arguments(BrowserName.Safari.name(), "OS X", "Ventura", "latest", "1280x1024", "Safari_latest_Ventura_1280x1024"),
//            arguments(BrowserName.Chrome.name(), "OS X", "Monterey", "latest", "1280x1024", "Chrome_latest_Monterey_1280x1024")
        );
    }

    @ParameterizedTest(name = "Run {index}: {1} {2}, {0}:{3}, {4}")
    @MethodSource(value = "browserParameters")
    @DisplayName(value = "takesLocalAppSnapshotWithProvidedName")
    @Step
    public void orderTrackingPageTest(String browserName, String platform,
                                                      String platformVersion, String browserVersion,
                                                      String screenResolution, String deviceName) {
        //setEnv("PERCY_TOKEN", "daa6c215ff1261f935236c4084944d7dd076d48392588c0d34210791d1d51223");
        AbstractTestBase.setUpDriver(browserName, platform, platformVersion, testInfo, browserVersion, screenResolution);
        Selenide.open("https://www.browserstack.com/");
        Selenide.sleep(5000);
        //percy = new Percy(WebDriverRunner.getWebDriver());
        percy.snapshot(deviceName, Arrays.asList(1280, 768, 375), 1024, true);
//    atOrderTrackingPage()
//            .openOrderTrackingPage(OrderTrackingElements.class)
//            .setOrderIntoInputField("12")
//            .clickOnOrderTrackingButton();
//    Selenide.sleep(5000);
    }

    //@Test
//    public void test() throws IOException {
//        //setEnv("PERCY_TOKEN", "daa6c215ff1261f935236c4084944d7dd076d48392588c0d34210791d1d51223");
//        System.out.println("PERCY_TOKEN = " + System.getenv("PERCY_TOKEN"));
//        ProcessBuilder pb = new ProcessBuilder("env");
//        pb.environment().put("PERCY_TOKEN", "daa6c215ff1261f935236c4084944d7dd076d48392588c0d34210791d1d51223");
//        Process process = pb.start();
//        Scanner sc = new Scanner(process.getInputStream());
//        while (sc.hasNext()) {
//            String line = sc.nextLine();
//            if (line.contains("PERCY_TOKEN")) {
//                //System.out.println(line);
//            }
//        }
//        System.out.println("PERCY_TOKEN = " + System.getenv("PERCY_TOKEN"));
//    }
//    String[] command = { "/bin/sh", "-C", "echo $PERCY_TOKEN" };//cmd /C %PERCY_TOKEN%
//    String[] envp = { "export PERCY_TOKEN=daa6c215ff1261f935236c4084944d7dd076d48392588c0d34210791d1d51223" };
//
//    Process p = Runtime.getRuntime().exec(command, envp);
//    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//    String s = reader.readLine();
//    System.err.println(s);
//    System.out.println("PERCY_TOKEN = " + System.getenv("PERCY_TOKEN"));

    //    ProcessBuilder pb = new ProcessBuilder("env");
//    pb.environment().put("PERCY_TOKEN", "daa6c215ff1261f935236c4084944d7dd076d48392588c0d34210791d1d51223");
//    Process process = pb.start();
//    Scanner sc = new Scanner(process.getInputStream());
//    while (sc.hasNext()) {
//      String line = sc.nextLine();
//      if (line.contains("PERCY_TOKEN")) {
//        //System.out.println(line);
//      }
//    }
//
//    for(String envName : pb.environment().keySet()) {
//      System.out.printf("%s : %s%n", envName, pb.environment().get(envName));
//    }
//    System.out.println("PERCY_TOKEN = " + System.getenv("PERCY_TOKEN"));
//  }
//    public static void setEnv(String key, String value) {
//        try {
//            Map<String, String> env = System.getenv();
//            Class<?> cl = env.getClass();
//            Field field = cl.getDeclaredField("m");
//            field.setAccessible(true);
//            Map<String, String> writableEnv = (Map<String, String>) field.get(env);
//            writableEnv.put(key, value);
//        } catch (Exception e) {
//            throw new IllegalStateException("Failed to set environment variable", e);
//        }
//    }
}
