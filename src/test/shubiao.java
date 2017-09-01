package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Created by Administrator on 2017/3/30.
 */
class myMouseListener    implements MouseMotionListener
{
    public void mouseMoved(MouseEvent e){
        int x=e.getX();
        int y=e.getY();
        String s="当前鼠标坐标:"+x+','+y;
        System.out.println(s);
        shubiao.lab.setText(s);
    }
    public void mouseDragged(MouseEvent e){};
}
public class shubiao extends JFrame {
    public static JLabel lab=new JLabel();
    public shubiao() {
    }
    public static void main(String [] args)
    {
        //  MouseMove fm=new MouseMove("鼠标坐标测试");
        JFrame fm=new JFrame("鼠标坐标测试");
        JPanel fp=new JPanel();
        fp.addMouseMotionListener(new myMouseListener());//对在面板上的鼠标移动进行监听。
        Container con=fm.getContentPane();
        fp.add(lab);
        con.add(fp);
        fm.setSize(500,400);
        fm.setVisible(true);
        fm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
