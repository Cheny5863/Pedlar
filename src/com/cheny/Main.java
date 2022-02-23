package com.cheny;
import com.GUI.*;
import com.sun.awt.AWTUtilities;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

public class Main {

    public static void main(String[] args) {

	    // write your code here
        FramelessWindow w = new FramelessWindow();
        w.setBackground(w.bgColor);

        //左右两个容器
        JPanel leftPanel = new JPanel();
        RoundPanel rightPanel = new RoundPanel();

        //leftPanel.setSize(771,646);
        leftPanel.setBackground(w.bgColor);
        // rightPanel.setSize(184,646);
        rightPanel.setBackground(Color.white);
        rightPanel.setPreferredSize(new Dimension(200,w.getHeight()-100));

        w.container.add(leftPanel, BorderLayout.CENTER);
        w.container.add(rightPanel,BorderLayout.EAST);

        //左容器内容 = 上（画板） + 下（控制台）
        //画板
        w.paintPad = new RoundPanel();
        w.paintPad.setBackground(Color.white);
        w.paintPad.setPreferredSize(new Dimension(leftPanel.getWidth(), 700));
        JFrame.setDefaultLookAndFeelDecorated(true);


        //画板内的画纸
        JPanel paper = new JPanel();
        paper.setBounds(0,0,1000,1000);
        paper.setLayout(null);
        //paper.setBackground();


        //控制台
        RoundPanel controller = new RoundPanel();
        controller.setBackground(new Color(116,169,181));
        controller.setPreferredSize(new Dimension(leftPanel.getWidth(), 250));
        //添加路径的方式，双击起点单击终点

        //控制台的内容
        //创建控制台panel 并添加到controller内
        controller.setLayout(new BoxLayout(controller,BoxLayout.Y_AXIS));
        JPanel controlerTop,controlerBottom;
        controlerTop = new JPanel();
        controlerBottom = new JPanel();
        controller.add(controlerTop);
        controller.add(controlerBottom);

        //设置控制台上下两部分的属性
        controlerTop.setBackground(null);
        controlerBottom.setBackground(Color.black);
        controlerTop.setPreferredSize(new Dimension(controller.getWidth() ,60));
        controlerBottom.setPreferredSize(new Dimension(controller.getWidth(),400));

        //设置标题和文字大小
        controlerTop.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleController = new JLabel("控制台");
        titleController.setFont(new Font("微软雅黑",Font.BOLD,24));
        titleController.setForeground(Color.white);

        //将标题添加至panel
        controlerTop.add(titleController);

        //控制台实际操作部位
        //JPanel


        //上绘图板与下控制台的间隙
        JPanel middleGap = new JPanel();
        middleGap.setPreferredSize(new Dimension(leftPanel.getWidth(),10));
        middleGap.setBackground(w.bgColor);

        BoxLayout boxLayout = new BoxLayout(leftPanel,BoxLayout.Y_AXIS);
        leftPanel.setLayout(boxLayout);//设置盒子布局
        leftPanel.add(w.paintPad);
        leftPanel.add(middleGap);
        leftPanel.add(controller);

        //右容器内容 = Log窗口
        rightPanel.setBackground(new Color(	175,217,209));

        //Log窗的内容
        rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.Y_AXIS));
        JPanel logTop,logBottom;
        logTop = new JPanel();
        logBottom = new JPanel();
        rightPanel.add(logTop);
        rightPanel.add(logBottom);

        //设置控制台上下两部分的属性
        logTop.setBackground(null);
        logBottom.setBackground(Color.black);
        logTop.setPreferredSize(new Dimension(rightPanel.getWidth(),50));
        logBottom.setPreferredSize(new Dimension(rightPanel.getWidth(),760));

        logTop.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLog = new JLabel("Log");
        titleLog.setFont(new Font("微软雅黑",Font.BOLD,32));
        titleLog.setForeground(Color.white);

        logTop.add(titleLog);

        w.setVisible(true);


        //设置抗锯齿
        Graphics2D g2d = (Graphics2D) w.getGraphics();
        //System.out.println(w.getGraphics());
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    }
}
