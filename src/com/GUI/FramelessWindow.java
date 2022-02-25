package com.GUI;
import com.GUI.ShadowBorder;
import com.sun.awt.AWTUtilities;
import com.sun.xml.internal.messaging.saaj.soap.JpegDataContentHandler;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class FramelessWindow extends JFrame {

    //背景图片bgImage
    //private ImageIcon bgImage = new ImageIcon("images/bg.png");
    //用于处理拖动事件，表示鼠标按下时的坐标，相对于JFrame
    private ImageIcon miniBtnIcon = new ImageIcon("images/max.png");
    private ImageIcon closeBtnIcon = new ImageIcon("images/close.png");
    private int xOld = 0;
    private int yOld = 0;
    private int windowHeight = 700;
    private int windowWidth = 1000;
    private int menuBarHeight = 80;
    public JPanel container = new JPanel();
    public Color bgColor = new Color(238,238,238);

    private boolean isPressed = false;
    private void setBtnTranp(JButton button){ //设置按钮背景透明
        // 隐藏按钮各属性的设置
        button.setMargin(new Insets(0,0,0,0));//将边框外的上下左右空间设置为0
        button.setIconTextGap(0);//将标签中显示的文本和图标之间的间隔量设置为0
        button.setBorderPainted(false);//不打印边框
        button.setBorder(null);//除去边框
//      button.setText(null);//除去按钮的默认名称
        button.setFocusPainted(false);//除去焦点的框
        button.setContentAreaFilled(false);//除去默认的背景填充
    }

    public  FramelessWindow(){
        //设置布局模式
        //container.setSize(windowWidth - 20,windowHeight-20);
        container.setLayout(new BorderLayout(10,10));

        getContentPane().setLayout(null);
        getContentPane().add(container,CENTER_ALIGNMENT);
        container.setBounds(10,0,windowWidth - 20,windowHeight-15);
        this.setSize(windowWidth, windowHeight);
        this.setLocationRelativeTo(null);
        //处理拖动事件---去掉默认边框后，不能拖动了,所以实现鼠标的抽象函数
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                xOld = e.getX();//记录鼠标按下时的坐标
                yOld = e.getY();

                //按住的地方是标题栏才可以拖动窗口
                if(xOld > 0&& xOld < windowWidth&& yOld > 0 && yOld < menuBarHeight){
                    isPressed = true;
                    //System.out.println(isPressed);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
            }

        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if(isPressed){
                    int xOnScreen = e.getXOnScreen();
                    int yOnScreen = e.getYOnScreen();
                    int xx = xOnScreen - xOld;
                    int yy = yOnScreen - yOld;
                    FramelessWindow.this.setLocation(xx, yy);//设置拖拽后，窗口的位置
                }
            }
        });

        JPanel mainPanel = new JPanel();
        //mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainPanel.setSize(windowWidth, menuBarHeight);
        mainPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        //最小化按钮
        JButton miniBtn = new JButton(miniBtnIcon);
        mainPanel.add(miniBtn);
        setBtnTranp(miniBtn);

        //关闭按钮
        JButton closeBtn = new  JButton(closeBtnIcon);
        mainPanel.add(closeBtn);
        setBtnTranp(closeBtn);

        container.add(mainPanel, BorderLayout.NORTH);

        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        miniBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setExtendedState(JFrame.ICONIFIED);//最小化窗体
            }
        });

        setLocationRelativeTo(null);


        //设置边框圆角
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(), 38, 38));
    }


}