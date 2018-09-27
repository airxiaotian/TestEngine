package com.hitachi.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.hitachi.utils.PropertiesUtils;

public class ScreenShotWithXpath {
    private static ResourceBundle resource;

    public static void main(String[] args) {

        EdgeOptions edge = new EdgeOptions();
        edge.setCapability("phantomjs.binary.path",
                "C:\\Users\\rentaladmin\\Documents\\research\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        InternetExplorerOptions ie = new InternetExplorerOptions();
        ie.setCapability("phantomjs.binary.path",
                "C:\\Users\\rentaladmin\\Documents\\research\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        try {
            //        String name = "SCCBD0306";
            //        resource = ResourceBundle.getBundle(name, Locale.JAPANESE);
            List<ResourceBundle> resources = PropertiesUtils.getAllProperties();
            for (ResourceBundle res : resources) {
                resource = res;
                System.out.println(resource.getBaseBundleName());
                File dir = new File("D:\\evidence_pankuzu\\" + resource.getBaseBundleName());
                if (!dir.exists()) {
                    dir.mkdir();
                }
                Enumeration<String> keys = resource.getKeys();
                while (keys.hasMoreElements()) {
                    String key = keys.nextElement();
                    System.out.println(key);
                    System.setProperty("webdriver.chrome.driver", "C:\\python3.7\\chromedriver.exe");
                    ChromeDriver driver = new ChromeDriver();
                    //         		    PhantomJSDriver driver = new PhantomJSDriver(cap);
                    //                 System.setProperty("webdriver.ie.driver", "C:\\python3.7\\IEDriverServer.exe");
                    //                 InternetExplorerDriver driver = new InternetExplorerDriver();
                    driver.manage().window().setSize(new Dimension(1920, 8000));

                    //                getImage(edge, resource.getString(key).split(";"), dir.getAbsolutePath() + "\\edge_" + key);
                    getImage(driver, resource.getString(key).split(";"), dir.getAbsolutePath() + "\\ie_" + key);
                }
            }
        } catch (Exception e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }

    public static void getImage(RemoteWebDriver driver, String[] steps, String fileName) throws Exception {
        int size = 0;
        try {
            login(driver, "JC00000013", "Passw0rd");
            gotoTargetPage(driver, steps, fileName);
            size = driver.findElementsByXPath("//td[@class='pankuzu']//a").size();
            logout(driver);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            driver.quit();
        }
        for (int i = 0; i < size; i++) {
            ChromeDriver driver1 = new ChromeDriver();
            driver1.manage().window().setSize(new Dimension(1920, 8000));
            getImage(driver1, steps, fileName, i);
        }
    }

    public static void getImage(RemoteWebDriver driver, String[] steps, String fileName, int count) throws Exception {

        try {
            login(driver, "JC00000013", "Passw0rd");
            gotoTargetPage2(driver, steps, fileName, count);
            logout(driver);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            driver.quit();
        }
    }

    private static void logout(RemoteWebDriver driver) throws InterruptedException {
        driver.findElementById("btnLogout").click();
        Thread.sleep(2000);
        driver.findElementByXPath("//input[@value='ログアウト']").click();
    }

    private static void login(RemoteWebDriver driver, String userName, String password) throws InterruptedException {
        driver.get("http://10.216.238.12/jcis/aa/aa110Prev");
        driver.findElementById("txtUserId_label").sendKeys(userName);
        driver.findElementByName("userItem.password").sendKeys(password);
        driver.findElementById("btnLogin").click();

        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
        }
        Thread.sleep(2000);
        driver.findElementByXPath("//input[@value='コリンズ']").click();
    }

    private static void gotoTargetPage2(RemoteWebDriver driver, String[] steps, String fileName, int index)
            throws InterruptedException, IOException {
        for (String step : steps) {
            WebDriverWait wait = new WebDriverWait(driver, 10);
             wait.until(new ExpectedCondition<WebElement>() {

                 @Override
                 public WebElement apply(WebDriver input) {
                     // TODO 自動生成されたメソッド・スタブ
                     WebElement element = null;
                     try {
                         element = input.findElements(By.xpath("//a")).get(0);
                     } catch (UnhandledAlertException e) {
                         input.switchTo().alert().accept();
                     }
                     return element;
                 }
             });

            oneStep(driver, step);
        }

        driver.findElementsByXPath("//td[@class='pankuzu']//a").get(index).click();
        ;
        screenShot(driver, fileName + "_" + index);

        if (driver.getWindowHandles().size() > 1) {
            driver = (RemoteWebDriver) driver.switchTo().window((String) driver.getWindowHandles().toArray()[0]);
        }
    }

    private static void gotoTargetPage(RemoteWebDriver driver, String[] steps, String fileName)
            throws InterruptedException, IOException {
        for (String step : steps) {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(new ExpectedCondition<WebElement>() {

                @Override
                public WebElement apply(WebDriver input) {
                    // TODO 自動生成されたメソッド・スタブ
                    WebElement element = null;
                    try {
                        element = input.findElements(By.xpath("//a")).get(0);
                    } catch (UnhandledAlertException e) {
                        input.switchTo().alert().accept();
                    }
                    return element;
                }

            });
            //            while (driver.getPageSource().equals("")) {
            //                Thread.sleep(300);
            //            }
            oneStep(driver, step);
        }
        screenShot(driver, fileName);

        if (driver.getWindowHandles().size() > 1) {
            driver = (RemoteWebDriver) driver.switchTo().window((String) driver.getWindowHandles().toArray()[0]);
        }
    }

    private static void oneStep(RemoteWebDriver driver, String step) throws InterruptedException {
        WebElement element;
        String op = step.split(":")[0];
        if (step.split(":").length > 1) {
            element = findElement(driver, step, 0);
        } else {
            if (step.startsWith("switch")) {
                String switchTo = "";
                String[] handles = driver.getWindowHandles().toArray(new String[] {});
                for (int i = 0; i < handles.length; i++) {
                    if (handles[i].equals(driver.getWindowHandle())) {
                        switchTo = handles[i + 1];
                    }
                }
                driver = (RemoteWebDriver) driver.switchTo().window(switchTo);
                driver.manage().window().setSize(new Dimension(1920, 10000));
            }
            if (step.startsWith("confirm")) {
                driver.executeScript("window.confirm = function(msg){return true;}");
            }
            if (step.startsWith("alert")) {
//                driver.switchTo().alert().accept();
            }
            element = null;
        }
        executeOperation(element, op);
    }

    private static WebElement findElement(RemoteWebDriver driver, String step, int retry) throws InterruptedException {
        WebElement element = null;
        String xpath = step.split(":")[1];
        try {
            if (step.split(":").length == 3) {
                element = driver.findElementsByXPath(xpath).get(Integer.parseInt(step.split(":")[2]));
            } else {
                element = driver.findElementByXPath(xpath);
            }
        } catch (ElementNotVisibleException | NoSuchElementException e) {
            Thread.sleep(2000);
            if (retry < 3) {
                findElement(driver, step, retry++);
            } else {
                throw e;
            }
        }
        return element;
    }

    private static void screenShot(RemoteWebDriver driver, String fileName) throws IOException {
        byte[] image = null;
        try {
            image = driver.getScreenshotAs(OutputType.BYTES);
        } catch (UnhandledAlertException e) {
            driver.switchTo().alert().accept();
            image = driver.getScreenshotAs(OutputType.BYTES);
        }
        ImageOutputStream out = ImageIO.createImageOutputStream(new File(fileName + ".png"));
        out.write(image);
        out.flush();
        out.close();

    }

    private static void executeOperation(WebElement element, String op) throws InterruptedException {
        if (element != null) {
            if (op.equals("click")) {
                element.click();
            }
            if (op.startsWith("send")) {
                element.sendKeys(op.substring(op.indexOf("(") + 1, op.indexOf(")")).replace("{colon}", ":"));
            }
            if (op.startsWith("select")) {
                Select select = new Select(element);
                select.selectByIndex(Integer.parseInt(op.substring(op.indexOf("(") + 1, op.indexOf(")"))));
            }
            //            Thread.sleep(2000);
        }
    }

    public static void writeLog(String content) throws IOException {
        FileWriter writer = new FileWriter(new File("D:\\test.log"), true);
        writer.write(content + "\n");
        writer.close();
    }

}
