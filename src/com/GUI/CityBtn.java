package com.GUI;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class CityBtn extends JButton {
    private int xOld = 0;
    private int yOld = 0;
    public Timer timer;
    private int clickTimes = 0;
    private boolean isPressed = false;
    private int btnWidth = 30;
    private int btnHeight = 30;
    private RoundPanel parent;
    private int startX = 0;
    private int startY = 0;
    private int lastX = 0;
    private int lastY = 0;


    private int id = 0;
    private String strCityInfo = new String();
    public ArrayList<ArcInfo> listArcInfo = new ArrayList<>();
    public MainWindow frameMainWindow;
    public JLabel labelCityName;

    public String getStrCityInfo() {
        return strCityInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStrCityInfo(String strCityInfo) {
        this.strCityInfo = strCityInfo;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    private boolean isClicked = false;


    ActionListener taskPerformer = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println("time on");
            if (clickTimes < 2) {
                clickTimes = 0;//如果时间到了还没双击那就不算了，此次双击失效
                timer.stop();
            }
        }
    };

    CityBtn(MainWindow frameMainWindow, int id) {
        super();
        //初始化定时器
        this.id = id;
        timer = new Timer(500, taskPerformer);
        this.frameMainWindow = frameMainWindow;
        parent = (RoundPanel) this.getParent();
        labelCityName = new JLabel();

        setPreferredSize(new Dimension(btnWidth, btnHeight));
        setSize(btnWidth, btnHeight);
        Font font = new Font("微软雅黑", Font.BOLD, 14);
        labelCityName.setFont(font);
        labelCityName.setForeground(new Color(116, 169, 181));
        labelCityName.setVisible(true);
        setContentAreaFilled(false);
        this.setBorderPainted(false); // 不绘制边框
        this.setFocusPainted(false); // 不绘制焦点状态
        this.setSelected(true);
        labelCityName.setHorizontalAlignment(SwingConstants.CENTER);
        CityBtn that = this;
        //鼠标单击点击事件
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    clickTimes++;//每点一次点击次数加1
                    xOld = e.getX();//记录鼠标按下时的坐标
                    yOld = e.getY();
                    if (!timer.isRunning()) ;//如果定时器未启动就启动
                    timer.start();
                    if (frameMainWindow.paintPad.isSettingArc()) {//在设置边的情况下按钮被点击了说明被点击的这个按钮是终点
                        if (that.id == frameMainWindow.cityBtnCurrent.id) {
                            frameMainWindow.logToWindow("不能设置自己到自己");
                            frameMainWindow.paintPad.setSettingArc(false);
                            return;
                        }
                        //添加边到当前城市
                        if (frameMainWindow.cityBtnCurrent.addArcToVel(that, 0, true)) {
                            frameMainWindow.logToWindow("添加成功");
                            that.addArcToVel(frameMainWindow.cityBtnCurrent, 0, false);//添加成功则添加反向的边
                        } else {
                            frameMainWindow.logToWindow("边重复");
                        }
                        ;

                        frameMainWindow.paintPad.setSettingArc(false);
                    } else {
                        frameMainWindow.cityBtnCurrent = that;
                        frameMainWindow.textFieldCityName.setText(labelCityName.getText());
                        frameMainWindow.roundTextArea.textAreaReal.setText(strCityInfo);
                        Component cp = (Component) e.getSource(); //获取事件e的触发源
                        //当鼠标点下的时候记录组件当前的坐标与鼠标当前在屏幕的位置
                        //System.out.println(cp.getX() + "  "+cp.getY());
                        if (getParent().getBounds().contains(xOld, yOld)) {
                            xOld = cp.getX();
                            yOld = cp.getY();
                            startX = e.getXOnScreen();
                            startY = e.getYOnScreen();
                            isPressed = true;
                        }
                        frameMainWindow.paintPad.repaint();

                        if (clickTimes == 2) {//如果检测到双击
                            //这里执行双击事件
                            //System.out.println("检测到双击");
                            //System.out.println(getBounds());
                            //System.out.println(xOld + " : " + yOld);
                            if (getBounds().contains(xOld, yOld)) {
                                //检测到画板内双击
                                doubleClick();

                            }

                            timer.stop();
                            clickTimes = 0;
                        }
                    }
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
                    labelCityName.setLocation(xx - 25, yy + getHeight() / 2 + 5);//设置文本拖拽后的位置
                    lastX = xx;
                    lastY = yy;
                }

            }
        });
    }

    public void doubleClick() {
        frameMainWindow.paintPad.setSettingArc(true);
        frameMainWindow.paintPad.setPointStart(new Point(getX() + getWidth() / 2, getY() + getHeight() / 2));
        frameMainWindow.paintPad.setPointEndLast(new Point(getX() + getWidth() / 2, getY() + getHeight() / 2));
    }

    protected void paintComponent(Graphics g) {
        //设置抗锯齿
        Graphics2D g2d = (Graphics2D) g;
        //System.out.println(w.getGraphics());
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (getModel().isArmed()) {
            g.setColor(Color.lightGray); // 点击时高亮
        } else if (frameMainWindow.cityBtnCurrent == this) {
            g.setColor(Color.YELLOW);
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

    private boolean addArcToVel(CityBtn target, int distance, boolean ifDraw) { //将info无重复地加到btn内
        ArcInfo arcInfo = new ArcInfo(this, target, ifDraw, 0);
        //将画板的指针给到输入框这样输入框输入时画板不会刷新
        if (ifDraw)
            arcInfo.setTextFieldOnArc(new TextFieldOnArc(frameMainWindow, arcInfo));
        for (ArcInfo tempArc :
                this.listArcInfo) {
            if (tempArc.equal(arcInfo)) {//已经添加过这条边，就不加了
                System.out.println("已经添加过这条边");
                frameMainWindow.paintPad.setSettingArc(false);
                frameMainWindow.paintPad.setInputing(true);

                return false;
            }
        }
        this.listArcInfo.add(arcInfo);
        return true;
    }
}
