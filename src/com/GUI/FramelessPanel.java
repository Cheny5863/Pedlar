package com.GUI;


import javax.swing.JFrame;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;

public class FramelessPanel extends JFrame {
    private int xOld = 0;
    private int yOld = 0;
    public RoundBtn btnLastLayer;
    public void setMenuBarHeight(int menuBarHeight) {
        this.menuBarHeight = menuBarHeight;
    }

    private int menuBarHeight = 80;
    public Color bgColor = new Color(238, 238, 238);

    public CityChessPanel paintPad;
    private boolean isPressed = false;

    public FramelessPanel(int width, int height) {
        //设置布局模式
        //container.setSize(windowWidth - 20,windowHeight-20);

        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        //处理拖动事件---去掉默认边框后，不能拖动了,所以实现鼠标的抽象函数
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                xOld = e.getX();//记录鼠标按下时的坐标
                yOld = e.getY();

                //按住的地方是标题栏才可以拖动窗口
                if (xOld > 0 && xOld < width && yOld > 0 && yOld < menuBarHeight) {
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

                if (isPressed) {
                    int xOnScreen = e.getXOnScreen();
                    int yOnScreen = e.getYOnScreen();
                    int xx = xOnScreen - xOld;
                    int yy = yOnScreen - yOld;
                    FramelessPanel.this.setLocation(xx, yy);//设置拖拽后，窗口的位置
                }
            }
        });

        setLocationRelativeTo(null);


        //设置边框圆角
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(), 38, 38));
    }
}
