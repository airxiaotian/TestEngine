package com.hitachi.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.hitachi.utils.ImageUtils;
import com.hitachi.utils.PropertiesUtils;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

public class ScreenShotWithXpath {

    private static ResourceBundle resource;
    private static Logger logger = Logger.getLogger(ScreenShotWithXpath.class);

    public static void main(String[] args) {
        EdgeOptions edge = new EdgeOptions();
        edge.setCapability("phantomjs.binary.path",
                "C:\\Users\\rentaladmin\\Documents\\research\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        InternetExplorerOptions ie = new InternetExplorerOptions();
        ie.setCapability("phantomjs.binary.path",
                "C:\\Users\\rentaladmin\\Documents\\research\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        try {
            FileAppender appender = new FileAppender(new SimpleLayout(),
                    PropertiesUtils.getProperties("config").getString("log"));
            logger.addAppender(appender);
            //        String name = "SCCBD0306";
            //        resource = ResourceBundle.getBundle(name, Locale.JAPANESE);
            List<ResourceBundle> resources = PropertiesUtils.getAllProperties();

            List<String> except = Arrays.asList(PropertiesUtils.getProperties("except").getString("except").split(";"));
            for (ResourceBundle res : resources) {
                //TEST
                if (except != null && except.contains(res.getBaseBundleName())) {
                    continue;
                }
                resource = res;
                System.out.println("画面番号:" + resource.getBaseBundleName());
                logger.info("画面番号:" + resource.getBaseBundleName());
                File dir = new File(
                        PropertiesUtils.getProperties("config").getString("evidence") + resource.getBaseBundleName());
                if (!dir.exists()) {
                    dir.mkdir();
                }
                Enumeration<String> keys = resource.getKeys();
                while (keys.hasMoreElements()) {
                    String key = keys.nextElement();
                    if (!PropertiesUtils.getProperties("config").getString("overwrite").equals("true")) {
                        File tmp = new File(dir.getAbsolutePath() + "\\ie_" + key + ".png");
                        if (tmp.exists()) {
                            continue;
                        } else {
//                            if (!key.equals("include"))
//                                System.out.println(key);
                        }
                    }

                    if (!key.equals("include")) {
                        System.out.println("ケース番号:" + key);
                        logger.info("ケース番号:" + key);
                        System.setProperty(PropertiesUtils.getProperties("config").getString("browser"),
                                PropertiesUtils.getProperties("config").getString("driver"));
                                                ChromeDriver driver = new ChromeDriver();
                        //                                        PhantomJSDriver driver = new PhantomJSDriver(ie);
                        //                        System.setProperty("webdriver.ie.driver", "C:\\python3.7\\IEDriverServer2.exe");
//                        InternetExplorerDriver driver = new InternetExplorerDriver();

                        driver.manage().window().setSize(new Dimension(1280, 700));
                        driver.get(PropertiesUtils.getProperties("config").getString("address"));
                        executeInculde(driver, resource);

                        if (!key.equals("layout") && resource.containsKey("layout")) {
                            gotoTargetPage(driver, resource.getString("layout").split(";"));
                        }
                        //                getImage(edge, resource.getString(key).split(";"), dir.getAbsolutePath() + "\\edge_" + key);
                        getImage(driver, resource.getString(key).split(";"), dir.getAbsolutePath() + "\\ie_" + key);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            logger.fatal(e.getMessage());
        }
    }

    public static void executeInculde(RemoteWebDriver driver, ResourceBundle resource)
            throws Exception {
        if (resource.containsKey("include")) {
            ResourceBundle resourceBundle = PropertiesUtils.getProperties(resource.getString("include"));

            executeInculde(driver, resourceBundle);

            if (resourceBundle.containsKey("layout")) {
                String[] steps = resourceBundle.getString("layout").split(";");
                gotoTargetPage(driver, steps);
            }
        }
    }

    public static void getImage(RemoteWebDriver driver, String[] steps, String fileName) throws Exception {
        try {
            gotoTargetPage(driver, steps);
            screenShot2(driver, fileName);
            if (driver.getWindowHandles().size() > 1) {
                driver = (RemoteWebDriver) driver.switchTo().window((String) driver.getWindowHandles().toArray()[0]);
            }
//            driver.findElementById("btnLogout").click();
        } catch (Exception e) {
            logger.fatal(e.getMessage());
        } finally {
            driver.quit();
        }
    }

    private static void gotoTargetPage(RemoteWebDriver driver, String[] steps)
            throws Exception {
        for (String step : steps) {
            System.out.println(step);
            logger.info(step);
            oneStep(driver, step);
        }
    }

    private static void oneStep(RemoteWebDriver driver, String step) throws Exception {
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
                        if (i == handles.length - 1) {
                            switchTo = handles[i - 1];
                        } else {
                            switchTo = handles[i + 1];
                        }
                    }
                }
                driver = (RemoteWebDriver) driver.switchTo().window(switchTo);
                driver.manage().window().setSize(new Dimension(1280, 700));
            } else if (step.startsWith("back")) {
                String[] handles = driver.getWindowHandles().toArray(new String[] {});
                driver = (RemoteWebDriver) driver.switchTo().window(handles[handles.length - 1]);
            }
            if (step.startsWith("confirm")) {
                driver.executeScript("window.confirm = function(msg){return true;}");
            }
            if (step.startsWith("sleep")) {
                Thread.sleep(1000);
            }
            if (step.startsWith("clickalllink")) {
                List<WebElement> elements = driver.findElementsByXPath("//form[@method='post']//a");
                for (int i = elements.size() - 1; i >= 0; i--) {
                    if (elements.get(i).isDisplayed()) {
                        executeOperation(elements.get(i), "click");
                    }
                }
            }
            element = null;
        }
        executeOperation(element, op);
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
        }
    }

    private static WebElement findElement(RemoteWebDriver driver, String step, int retry) throws InterruptedException {
        WebElement element = null;
        String xpath = step.split(":")[1];
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.ignoring(Exception.class);
            if (step.split(":").length == 3) {
                element = wait.until(new ExpectedCondition<WebElement>() {
                    @Override
                    public WebElement apply(WebDriver input) {
                        WebElement element = null;
                        try {
                            List<WebElement> elements = driver.findElementsByXPath(xpath);
                            if (elements.size() <= Integer.parseInt(step.split(":")[2])) {
                                throw new NoSuchElementException("No Such Index");
                            }
                            element = elements.get(Integer.parseInt(step.split(":")[2]));
                        } catch (UnhandledAlertException e) {
                            input.switchTo().alert().accept();
                        } catch (Exception e) {
                            logger.fatal(e.getMessage());
                        }
                        return element;
                    }
                });
            } else {
                element = wait.until(new ExpectedCondition<WebElement>() {
                    @Override
                    public WebElement apply(WebDriver input) {
                        WebElement element = null;
                        try {
                            element = driver.findElementByXPath(xpath);
                        } catch (UnhandledAlertException e) {
                            input.switchTo().alert().accept();
                        } catch (Exception e) {
                            logger.fatal(e.getMessage());
                        }
                        return element;
                    }
                });
            }
        } catch (ElementNotVisibleException | NoSuchElementException e) {
            Thread.sleep(2000);
            if (retry < 3) {
                element = findElement(driver, step, retry++);
            } else {
                throw e;
            }
        } catch (UnhandledAlertException e) {
            driver.switchTo().alert().accept();
            element = findElement(driver, step, retry++);
        } catch (Exception e) {
            throw e;
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

    private static void screenShot2(RemoteWebDriver driver, String fileName) throws IOException, InterruptedException {
        List<BufferedImage> imageList = new LinkedList<BufferedImage>();
        byte[] image = null;
        try {
            int height = Integer
                    .parseInt(driver.executeScript("return document.body.clientHeight; ", new Object[] {}).toString());
            int scroll = 0;
            int last = 0;
            while (height > scroll) {

                String js = "window.scrollTo(0," + scroll + ")";
                Thread.sleep(1000);
                driver.executeScript(js, new Object[] {});
                last = height - scroll;

                scroll += Integer.parseInt(PropertiesUtils.getProperties("config").getString("last"));
                image = driver.getScreenshotAs(OutputType.BYTES);

                BufferedImage buf = ImageIO.read(new ByteInputStream(image, image.length));

                if (last < Integer.parseInt(PropertiesUtils.getProperties("config").getString("last"))
                        && height > Integer.parseInt(PropertiesUtils.getProperties("config").getString("last"))) {
                    buf = ImageUtils.cutImage(0, buf.getWidth(),
                            (buf.getHeight() - last
                                    * Integer.parseInt(PropertiesUtils.getProperties("config").getString("height"))
                                    / Integer.parseInt(PropertiesUtils.getProperties("config").getString("last"))),
                            buf.getHeight(),
                            buf);
                }

                //                ImageOutputStream out = ImageIO.createImageOutputStream(new File(fileName + scroll + ".png"));
                //                ImageIO.write(buf, "png", out);
                //                out.flush();
                //                out.close();

                imageList.add(buf);
            }

        } catch (UnhandledAlertException e) {
            driver.switchTo().alert().accept();
            image = driver.getScreenshotAs(OutputType.BYTES);
        }
        BufferedImage buf = ImageUtils.mergeImage(false, imageList.toArray(new BufferedImage[] {}));
        ImageOutputStream out = ImageIO.createImageOutputStream(new File(fileName + ".png"));
        ImageIO.write(buf, "png", out);
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
        }
    }

    public static void writeLog(String content) throws IOException {
        FileWriter writer = new FileWriter(new File("D:\\test.log"), true);
        writer.write(content + "\n");
        writer.close();
    }

}
