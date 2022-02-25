package com.GUI;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

public class CityChessPanel extends RoundPanel {
    private int cityNum = 0;
    private Point pointStart = new Point();
    private boolean isSettingArc = false;
    private ArrayList<CityBtn> listCityBtn = new ArrayList<>();
    public MainWindow frameMainWindow;


    private Point pointEndLast = new Point();

    public Point getPointEndLast() {
        return pointEndLast;
    }

    public void setPointEndLast(Point pointEndLast) {
        this.pointEndLast = pointEndLast;
    }

    public Point getPointStart() {
        return pointStart;
    }

    public void setPointStart(Point pointStart) {
        this.pointStart = pointStart;
    }

    public boolean isSettingArc() {
        return isSettingArc;
    }

    public void setSettingArc(boolean settingArc) {
        isSettingArc = settingArc;
    }

    public CityChessPanel(int arcw, int arch, MainWindow frameMainWindow) {
        super(arcw, arch);
        this.frameMainWindow = frameMainWindow;

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if (isSettingArc) {
                    pointEndLast = e.getPoint();
                }

            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == 3)
                    isSettingArc = false;
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        BasicStroke stroke = new BasicStroke(2);
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setStroke(stroke);
        graphics.setColor(new Color(103, 223, 136));
        if (isSettingArc) {
            graphics.drawLine(pointStart.x, pointStart.y, pointEndLast.x, pointEndLast.y);
            repaint();
        } else {
            for (CityBtn btnTemp : listCityBtn
            ) {
                for (ArcInfo arcInfoTemp : btnTemp.listArcInfo
                ) {
                    graphics.drawLine(btnTemp.getX() + btnTemp.getWidth() / 2, btnTemp.getY() + btnTemp.getHeight() / 2, arcInfoTemp.getmTarget().getX() + arcInfoTemp.getmTarget().getWidth() / 2, arcInfoTemp.getmTarget().getY() + arcInfoTemp.getmTarget().getHeight() / 2);
                    repaint();
                }
            }
        }


    }

    @Override
    public void doubleClick() {
        CityChessPanel that = this;

        Thread t = new Thread(new Runnable() {
            public void run() {
                // run方法具体重写
                //画板内双击，事件
                cityNum++;

                /*加按钮*/
                CityBtn btn = new CityBtn(frameMainWindow, cityNum);
                btn.setBounds(xOld, yOld, btn.getWidth(), btn.getHeight());//设置按钮位置
                //btn.setLocation(xOld,yOld); //直接设置位置不生效
                btn.labelCityName.setBounds(xOld - 25, yOld + btn.getHeight() / 2 + 5, btn.getWidth() + 50, btn.getHeight() + 10);

                btn.setBackground(Color.orange);
                btn.setClicked(true);
                frameMainWindow.cityBtnCurrent = btn;
                frameMainWindow.textFieldCityName.setText("");
                frameMainWindow.roundTextArea.textAreaCityInfo.setText("");
                listCityBtn.add(btn);
                that.add(btn);
                that.add(btn.labelCityName);
                that.repaint();//重新绘制 不然会出现需要鼠标滑过才显示的问题
            }
        });
        t.start();

    }

}
