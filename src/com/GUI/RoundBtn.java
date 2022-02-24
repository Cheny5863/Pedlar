package com.GUI;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

public class RoundBtn extends JButton {
    private int arcw;
    private int arch;

    public RoundBtn(int arcw, int arch, int width, int height) {
        super();
        this.arch = arch;
        this.arcw = arcw;
        this.setPreferredSize(new Dimension(width, height));
        this.setSize(new Dimension(width, height));
        setContentAreaFilled(false);
        this.setBorderPainted(false); // 不绘制边框
        this.setFocusPainted(false); // 不绘制焦点状态
        this.setForeground(Color.white);
    }

    protected void paintComponent(Graphics g) {
        int fieldX = 0;
        int fieldY = 0;
        int fieldWeight = getSize().width;
        int fieldHeight = getSize().height;

        //设置抗锯齿
        Graphics2D g2d = (Graphics2D) g;
        //System.out.println(w.getGraphics());
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (getModel().isArmed()) {
            g2d.setColor(Color.LIGHT_GRAY); // 点击时高亮
        } else {
            g2d.setColor(new Color(103, 223, 136));
        }
        //fillRoundRect方法绘制一个圆角矩形
        g.fillRoundRect(0, 0, getSize().width - 1, getSize().height - 1, arcw, arch);//填充圆角矩形边界
        super.paintComponent(g2d);
    }


    private void setBtnTranp(JButton button) { //设置按钮背景透明
        // 隐藏按钮各属性的设置
        button.setMargin(new Insets(0, 0, 0, 0));//将边框外的上下左右空间设置为0
        button.setIconTextGap(0);//将标签中显示的文本和图标之间的间隔量设置为0
        button.setBorderPainted(false);//不打印边框
        button.setBorder(null);//除去边框
//      button.setText(null);//除去按钮的默认名称
        button.setFocusPainted(false);//除去焦点的框
        button.setContentAreaFilled(false);//除去默认的背景填充
    }

    ;
}
