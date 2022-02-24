package com.GUI;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;

public class CityBtn extends JButton{
    private int xOld = 0;
    private int yOld = 0;
    private boolean isPressed = false;
    private int btnWidth = 30;
    private int btnHeight = 30;
    private RoundPanel parent;
    private int startX = 0;
    private int startY = 0;
    private int lastX = 0;
    private int lastY = 0;
    public JLabel label;

    CityBtn(String str){
        super();
        parent = (RoundPanel) this.getParent();
        label = new JLabel(str);

        setPreferredSize(new Dimension(btnWidth, btnHeight));
        setSize(btnWidth, btnHeight);
        Font font = new Font("微软雅黑", Font.BOLD, 16);
        label.setFont(font);
        label.setForeground(new Color(116, 169, 181));
        label.setVisible(true);
        setContentAreaFilled(false);
        this.setBorderPainted(false); // 不绘制边框
        this.setFocusPainted(false); // 不绘制焦点状态
        label.setHorizontalAlignment(SwingConstants.CENTER);
        //鼠标单击点击事件
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Component cp = (Component)e.getSource(); //获取事件e的触发源
                //当鼠标点下的时候记录组件当前的坐标与鼠标当前在屏幕的位置
                //System.out.println(cp.getX() + "  "+cp.getY());
                if (getParent().getBounds().contains(xOld,yOld)){
                    xOld = cp.getX();
                    yOld = cp.getY();
                    startX = e.getXOnScreen();
                    startY = e.getYOnScreen();
                    isPressed = true;
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
                //System.out.println(isPressed);
                if(isPressed){
                    int xOnScr = e.getXOnScreen();
                    int yOnScr = e.getYOnScreen();
                    int xx = (xOnScr - startX) + xOld;
                    int yy = (yOnScr - startY) + yOld;

                    //如果太靠近边界应该触发保护，防止城市丢失
                    Rectangle r = getParent().getBounds();
                    int shrinkPixel = 10;//缩小一周的宽度
                    r.x += shrinkPixel;
                    r.y += shrinkPixel;
                    r.width -= 2 * shrinkPixel;
                    r.height -= 2 * shrinkPixel;
                    if (!r.contains(xx + getWidth() / 2, yy + getHeight() / 2)) {
                        CityBtn.this.setLocation(lastX, lastY);

                        return;
                    }

                    CityBtn.this.setLocation(xx, yy);//设置拖拽后，窗口的位置
                    label.setLocation(xx, yy + getHeight() / 2 + 5);//设置文本拖拽后的位置
                    lastX = xx;
                    lastY = yy;
                }

            }
        });
    }


    protected void paintComponent(Graphics g) {
        //设置抗锯齿
        Graphics2D g2d = (Graphics2D) g;
        //System.out.println(w.getGraphics());
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (getModel().isArmed()) {
            g.setColor(Color.lightGray); // 点击时高亮
        } else {
            g.setColor(getBackground());
        }
        // fillOval方法画一个矩形的内切椭圆，并且填充这个椭圆，
        // 当矩形为正方形时，画出的椭圆便是圆
        g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);

        super.paintComponent(g);
    }

    // 用简单的弧画按钮的边界。
    protected void paintBorder(Graphics g) {
        g.setColor(Color.white);
        // drawOval方法画矩形的内切椭圆，但不填充。只画出一个边界
        g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
    }

    // shape对象用于保存按钮的形状，有助于侦听点击按钮事件
    Shape shape;

    public boolean contains(int x, int y) {

        if ((shape == null) || (!shape.getBounds().equals(getBounds()))) {
            // 构造一个椭圆形对象
            shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        }
        // 判断鼠标的x、y坐标是否落在按钮形状内。
        return shape.contains(x, y);
    }

}
