package test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import spiderKc.kcBean.Count;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Administrator on 2017/3/30.
 */
public class yidongshubiao {
    public static void main(String args[]) throws InterruptedException {
        System.setProperty(Count.chrome,Count.chromepath);
        WebDriver driver=new ChromeDriver();
        driver.get("http://www.gsxt.gov.cn/index.html");
        Thread.sleep(5000);
        try
        {
            Robot myRobot = new Robot();
            myRobot.mouseMove(558, 493);                // 移动鼠标到坐标（x,y）处
            myRobot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);     // 模拟按下鼠标左键
            for(int x=559;x<2000;x++){
                myRobot.mouseMove(x, 493);
            }
            myRobot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);   // 模拟释放鼠标左键
        } catch (AWTException e)
        {
            e.printStackTrace();
        }





    }
}
