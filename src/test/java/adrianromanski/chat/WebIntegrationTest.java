package adrianromanski.chat;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ChatroomApplicationTests.class})
public class WebIntegrationTest {

    private static final String ROOT_URL = "http://localhost:8080";
    private static final String CHROME_DRIVER_PATH = "c:/chromedriver.exe";
    private static WebDriver driver;
    private static String username;

    @BeforeClass
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        driver = getDriver();
        username = "TestUser";
    }

    @AfterClass
    public static void tearDown() { driver.quit(); }

    @Test
    public void testLogin() {
        final String expectedUrl = ROOT_URL + "/chatroom/" + username;
        driver.get(ROOT_URL);
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.className("submit")).click();
        Assert.assertEquals(expectedUrl, driver.getCurrentUrl());
    }

    @Test
    public void testUserJoin() {
        final int userCount = driver.findElements(By.className("chat-num")).size();
        Assert.assertEquals(1, userCount);
    }

    @Test
    public void testMessage() {
        final int oldMsgSize = driver.findElements(By.className("mdui-card")).size();
        driver.findElement(By.id("msg")).sendKeys("test message");
        driver.findElement(By.id("send-msg")).click();
        final int newMsgSize = driver.findElements(By.className("mdui-card")).size();
        Assert.assertEquals(oldMsgSize + 1, newMsgSize);
    }

    @Test
    public void testUserLeave() {
        final String username2 = "TestUser2";
        final WebDriver driver2 = getDriver();
        driver2.get(ROOT_URL + "/chatroom/" + username2);
        driver2.quit();
        final int userCount = driver.findElements(By.className("chat-num")).size();
        Assert.assertEquals(1, userCount);
    }

    private static WebDriver getDriver() {
        final WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        return driver;
    }
}
