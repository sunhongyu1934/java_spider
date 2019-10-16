package java_spider;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import spiderKc.kcBean.Count;

import javax.crypto.spec.OAEPParameterSpec;

public class test2 {
    public static void main(String args[]) throws InterruptedException {
        System.setProperty(Count.chrome,Count.chromepath);
        ChromeOptions options=new ChromeOptions();
        options.addArguments("user-agent= Mozilla/5.0 (iPhone; CPU iPhone OS 13_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/7.0.8(0x17000820) NetType/4G Language/zh_CN");
        options.addArguments("Accept=text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        options.addArguments("Accept-Encoding=gzip, deflate, br");
        options.addArguments("Accept-Language=zh-cn");
        options.addArguments("Host=pacd.cdxunting.com");
        options.addArguments("Referer=https://pacd.cdxunting.com/app/mypage.php");

        ChromeDriver driver=new ChromeDriver(options);
        driver.get("https://pacd.cdxunting.com/volunteer/realname.php?backurl=../app/mypage.php");
        driver.manage().deleteAllCookies();
        driver.manage().addCookie(new Cookie("PHPSESSID","r5r5n69tj0ht2426g0r4mb75f0"));
        Thread.sleep(2000);
        driver.get("https://pacd.cdxunting.com/volunteer/realname.php?backurl=../app/mypage.php");
    }

}
