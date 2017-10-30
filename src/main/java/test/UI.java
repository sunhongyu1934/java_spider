package test;

/**
 * Created by Administrator on 2017/4/6.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("serial")
public class UI extends JFrame implements ActionListener{
    JPanel panel;
    JLabel oneLabel;
    JButton genButton;
    JTextField oneTextField;
    JTextField threeTextField;

    public UI() {
        super("组织机构代码生成器");
        panel = new JPanel();
        panel.setBackground(new Color(192, 192, 192));
        panel.setLayout(new FlowLayout());
        this.add(panel);
        oneLabel = new JLabel("请输入长8位的本体代码！");

        oneTextField = new JTextField(15);

        threeTextField = new JTextField(15);
        genButton = new JButton("生成校验码");
        oneTextField.addActionListener(this);
        genButton.addActionListener(this);
        panel.add(oneLabel);
        panel.add(oneTextField);
        panel.add(genButton);
        panel.add(threeTextField);
        this.setBounds(200, 100, 200, 240);
        this.setResizable(true);
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        new UI();

    }
    @Override
    public void actionPerformed(ActionEvent e) {

        String one = oneTextField.getText().trim();
        char[] a = new char[8];
        for (int i = 0; i<8 ; i++)
            a[i] = one.charAt(i);
        String all = new G_Code().jy(a);
        threeTextField.setText(String.valueOf(all));
    }

}
