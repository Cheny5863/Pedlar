package com.GUI;

import sun.awt.windows.ThemeReader;

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
import java.util.ArrayList;
import java.util.HashMap;

public class CityChessPanel extends RoundPanel {

    private int cityNum = 0;
    private Point pointStart = new Point();
    private boolean isSettingArc = false;
    public ArrayList<CityBtn> listCityBtn = new ArrayList<>();
    public MainWindow frameMainWindow;
    private boolean isInputing = false;
    private Path pathShortest = null;

    public void setDrawingPath(boolean drawingPath) {
        isDrawingPath = drawingPath;
    }

    private boolean isDrawingPath = false;


    public Path getPathShortest() {
        return pathShortest;
    }

    public void setPathShortest(Path pathShortest) {
        this.pathShortest = pathShortest;
    }

    public void setCityNum(int cityNum) {
        this.cityNum = cityNum;
    }
    public boolean isInputing() {
        return isInputing;
    }

    public int getCityNum() {
        return cityNum;
    }

    public void setInputing(boolean inputing) {
        isInputing = inputing;
    }


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
                if (e.getButton() == 3){
                    isSettingArc = false;
                    isDrawingPath = false;
                }

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
        } else if (isInputing) {
            //如果正在输入中就不刷新了，因为一刷新输入框就没办法输入

        } else {
            for (CityBtn btnTemp : listCityBtn
            ) {
                for (ArcInfo arcInfoTemp : btnTemp.listArcInfo
                ) {
                    if (!arcInfoTemp.isIfDraw()) {//画不画
                        //System.out.println("skip");
                        continue;
                    }

                    //线的起点和终点计算算法：
                    //分两类情况：1.斜率不存在 2.斜率存在
                    //1.那么x1 = x2,假定y1>y2 那么y1在下方,那么起点和终点分别为(x1 + (getWidth()/2), y1 + (getHeight()/2)-(getHeight()/2) (x2 + (getWidth()/2), y2 + (getHeight()/2) + (getHeight()/2))
                    //2.那么x1 != x2, 假定y1 > y2 ,计算出斜率k = (y2-y1)/(x2-x1) 按钮的半径r 那么起点和终点分别为:
                    // (x1 + (getWidth()/2), y1 + (getHeight()/2)-(getHeight()/2)      (x2 + (getWidth()/2), y2 + (getHeight()/2) + (getHeight()/2)
                    int x1 = btnTemp.getX() + btnTemp.getWidth() / 2;
                    int y1 = btnTemp.getY() + btnTemp.getHeight() / 2;
                    int x2 = arcInfoTemp.getmTarget().getX() + arcInfoTemp.getmTarget().getWidth() / 2;
                    int y2 = arcInfoTemp.getmTarget().getY() + arcInfoTemp.getmTarget().getHeight() / 2;

                    double radius = btnTemp.getWidth() / 2;
                    int result[] = optimizePoint(x1,y1,x2,y2,radius); //优化点的显示
                    x1 = result[0];
                    y1 = result[1];
                    x2 = result[2];
                    y2 = result[3];

                    TextFieldOnArc textFieldOnArc = arcInfoTemp.getTextFieldOnArc();
                    textFieldOnArc.setBounds(((x1 + x2) / 2) - textFieldOnArc.getWidth() / 2, ((y1 + y2) / 2) - textFieldOnArc.getHeight() / 2 - 10, 50, 20);
                    textFieldOnArc.setText(Integer.toString((arcInfoTemp.getmDistance())));
                    graphics.drawLine(x1, y1, x2, y2);
                    this.add(textFieldOnArc, 0);

                }
            }
            if (isDrawingPath){
                int perEnhance = 255/pathShortest.listAllPoint.size();
                for (int i = 0; i < pathShortest.listAllPoint.size() - 1; i++){

                    CityBtn start = pathShortest.listAllPoint.get(i).getTarget();
                    CityBtn end =  pathShortest.listAllPoint.get(i+1).getTarget();
                    int x1 = start.getX() + start.getWidth() / 2;
                    int y1 = start.getY() + start.getHeight() / 2;
                    int x2 = end.getX() + end.getWidth() / 2;
                    int y2 = end.getY() + end.getHeight() / 2;

                    double radius = start.getWidth() / 2;
                    int result[] = optimizePoint(x1,y1,x2,y2,radius); //优化点的显示
                    x1 = result[0];
                    y1 = result[1];
                    x2 = result[2];
                    y2 = result[3];
                    graphics.setColor(new Color(255 - perEnhance*i,0,perEnhance*i));
                    graphics.drawLine(x1, y1, x2, y2);
                }
            }

            repaint();
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
                btn.setLocation(xOld, yOld);
                btn.labelCityName.setBounds(xOld - 25, yOld + btn.getHeight() / 2 + 5, btn.getWidth() + 50, btn.getHeight() + 10);

                btn.setBackground(Color.orange);
                btn.setClicked(true);
                frameMainWindow.cityBtnCurrent = btn;
                frameMainWindow.textFieldCityName.setText("");
                frameMainWindow.roundTextArea.textAreaReal.setText("");
                listCityBtn.add(btn);
                that.add(btn);
                that.add(btn.labelCityName);

                //添加城市后更新下拉框的候选项
                frameMainWindow.updateComboBox();

                that.repaint();//重新绘制 不然会出现需要鼠标滑过才显示的问题
            }
        });
        t.start();

    }

    //删除指定id的按钮
    public void deleteBtn() {
        CityBtn temp = frameMainWindow.cityBtnCurrent;
        isDrawingPath = false;
        for (ArcInfo arcInfo :
                temp.listArcInfo) { //从当前按钮的边列表里面找到边信息
            for (ArcInfo temp2 :
                    arcInfo.getmTarget().listArcInfo) { //以待删除节点为起点的边，并获取边终点
                if (arcInfo.equal(temp2)) {
                    arcInfo.getmTarget().listArcInfo.remove(temp2);//将边终点进行反向删除（即删除以待删除节点为终点的边
                    if (temp2.isIfDraw())
                        remove(temp2.getTextFieldOnArc());
                    break;
                }
            }
        }
        for (ArcInfo arcInfoTemp :
                temp.listArcInfo) {
            if (arcInfoTemp.isIfDraw())
                frameMainWindow.paintPad.remove(arcInfoTemp.getTextFieldOnArc());
        }

        this.remove(temp); //从显示面板上去掉它
        this.remove(temp.labelCityName);
        listCityBtn.remove(temp); //删除按钮对象
        frameMainWindow.updateComboBox();//删除按钮后需要更新下拉框

        if (listCityBtn.isEmpty()) {
            frameMainWindow.cityBtnCurrent = null;
            frameMainWindow.logToWindow("城市已被全部删除");
        } else {
            int count = listCityBtn.size();//删除时按添加的先后顺序删除
            frameMainWindow.cityBtnCurrent = listCityBtn.get(count - 1);
            frameMainWindow.textFieldCityName.setText(frameMainWindow.cityBtnCurrent.labelCityName.getText());
            frameMainWindow.roundTextArea.textAreaReal.setText(frameMainWindow.cityBtnCurrent.getStrCityInfo());
        }
        repaint();
    }

    private int[] optimizePoint(int x1,int y1,int x2,int y2,double radius){
        double theta = Math.PI / 2;

        if (x1 != x2) {
            double k = (double) (y1 - y2) / (double) (x2 - x1);
            theta = Math.atan(k);
        }

        if (theta > 0) {
            if (y1 >= y2) {
                y1 -= radius * Math.sin(theta);
                x1 += radius * Math.cos(theta);

                y2 += radius * Math.sin(theta);
                x2 -= radius * Math.cos(theta);
            } else {
                y2 -= radius * Math.sin(theta);
                x2 += radius * Math.cos(theta);

                y1 += radius * Math.sin(theta);
                x1 -= radius * Math.cos(theta);
            }
        } else if (theta < 0) {
            if (y1 >= y2) {
                y1 -= radius * Math.sin(-theta);
                x1 -= radius * Math.cos(-theta);

                y2 += radius * Math.sin(-theta);
                x2 += radius * Math.cos(-theta);
            } else {
                y2 -= radius * Math.sin(-theta);
                x2 -= radius * Math.cos(-theta);

                y1 += radius * Math.sin(-theta);
                x1 += radius * Math.cos(-theta);
            }

        } else {
            if (x1 > x2) {
                x1 -= radius;
                x2 += radius;
            } else {
                x1 += radius;
                x2 -= radius;
            }
        }

        int result[] = {x1,y1,x2,y2};
        return result;
    }
}
